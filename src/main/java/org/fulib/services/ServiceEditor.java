package org.fulib.services;

import org.fulib.StrUtil;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Attribute;
import org.fulib.classmodel.Clazz;
import org.fulib.classmodel.FMethod;
import org.fulib.yaml.Yaml;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import java.util.*;

import static org.fulib.builder.Type.*;

public class ServiceEditor
{
   // =============== Fields ===============

   private final ClassModelManager mm = new ClassModelManager();
   private final Map<String, Clazz> dataClasses = new LinkedHashMap<>();
   private final Map<String, Clazz> commandClasses = new LinkedHashMap<>();
   private final Set<Clazz> commandPrototypeClasses = new LinkedHashSet<>();
   private final Map<Clazz, Collection<String>> dataclassAttachedRoles = new LinkedHashMap<>();
   private final STGroupFile group;

   private Clazz modelCommand;
   private Clazz editor;
   private String serviceName;
   private Clazz service;

   // =============== Constructors ===============

   public ServiceEditor()
   {
      group = new STGroupFile(this.getClass().getResource("templates/servicemodel.stg"));
      group.registerRenderer(String.class, new StringRenderer());
  }

   // =============== Properties ===============

   public String getServiceName()
   {
      return serviceName;
   }

   public ClassModelManager getClassModelManager()
   {
      return this.mm;
   }

   public Clazz getModelCommand()
   {
      return modelCommand;
   }

   public Clazz getEditor()
   {
      return editor;
   }

   // =============== Methods ===============

   private void havePatterns()
   {
      Clazz pattern = mm.haveClass("Pattern");

      Clazz patternObject = mm.haveClass("PatternObject");
      mm.haveAttribute(patternObject, "poId", STRING);
      mm.haveAttribute(patternObject, "handleObjectClass", "Class<?>");
      mm.haveAttribute(patternObject, "handleObject", "Object");
      mm.haveAttribute(patternObject, "kind", STRING);

      Clazz patternAttribute = mm.haveClass("PatternAttribute");
      mm.haveAttribute(patternAttribute, "handleAttrName", STRING);
      mm.haveAttribute(patternAttribute, "commandParamName", STRING);

      Clazz patternLink = mm.haveClass("PatternLink");
      mm.haveAttribute(patternLink, "handleLinkName", STRING);
      Attribute kind = mm.haveAttribute(patternLink, "kind", STRING);
      kind.setInitialization("\"core\"");

      mm.associate(pattern, "objects", MANY, patternObject, "pattern", ONE);
      mm.associate(patternObject, "attributes", MANY, patternAttribute, "object", ONE);
      mm.associate(patternObject, "links", MANY, patternLink, "source", ONE);
      mm.associate(patternLink, "target", ONE, patternObject, "incommingLinks", MANY);
   }

   private void haveRemoveCommand()
   {
      final Clazz removeCommand = this.haveCommand("RemoveCommand");
      mm.haveMethod(removeCommand, "@Override public Object run(" + this.editor.getName() + " editor)",
                    group.getInstanceOf("removeCommandRun").render());
   }

   private void haveModelCommand()
   {
      modelCommand = mm.haveClass("ModelCommand").setPropertyStyle(POJO);
      mm.haveAttribute(modelCommand, "id", STRING);
      mm.haveAttribute(modelCommand, "time", STRING);

      mm.haveMethod(modelCommand, "public Object run(" + this.editor.getName() + " editor)",
                    group.getInstanceOf("modelCommandRun").render());

      mm.haveMethod(modelCommand, "public void undo(" + this.editor.getName() + " editor)",
                    group.getInstanceOf("modelCommandUndo").render());

      mm.haveMethod(modelCommand, "public ModelCommand parse(Object currentObject)",
                    group.getInstanceOf("modelCommandParse").render());

      mm.haveMethod(modelCommand,
                    "public void matchAttributesAndLinks(Pattern pattern, PatternObject currentPatternObject, PathTable pathTable)",
                    group.getInstanceOf("modelCommandMatchAttributesAndLinks").render());

      mm.haveMethod(modelCommand, "public Set getSetOfTargetHandles(Map map, String poId, String linkName)",
                    group.getInstanceOf("modelCommandGetSetOfTargetHandles").render());

      mm.haveMethod(modelCommand, "public Pattern havePattern()", "      return null;\n");

      mm.haveMethod(modelCommand,
                    "private Object getHandleObjectAttributeValue(PatternObject patternObject, String handleAttributeName)",
                    group.getInstanceOf("modelCommandGetHandleValue").render());

      modelCommand.withImports("org.fulib.yaml.Reflector");
      modelCommand.withImports("org.fulib.yaml.StrUtil");
      modelCommand.withImports("java.lang.reflect.Method");
      modelCommand.withImports("org.fulib.tables.PathTable");
      modelCommand.withImports("java.util.*");
   }

   public Clazz haveEditor(String modelName)
   {
      this.serviceName = modelName;
      editor = this.mm.haveClass(modelName + "Editor");

      this.editorHaveMapFor("activeCommands", "ModelCommand");
      this.editorHaveMapFor("commandListeners", "List<CommandStream>");
      this.editorHaveMapFor("mapOfFrames", "Object");
      this.editorHaveMapFor("mapOfModelObjects", "Object");
      this.editorHaveMapFor("mapOfParsedObjects", "Object");
      haveGetOrCreate();

      Attribute dateFormat = mm.haveAttribute(editor, "isoDateFormat", "DateFormat");
      dateFormat.setInitialization("new SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\")");
      Attribute lastTime = mm.haveAttribute(editor, "lastTime", STRING);
      lastTime.setInitialization("isoDateFormat.format(new Date())");
      Attribute timeDelta = mm.haveAttribute(editor, "timeDelta", LONG);
      timeDelta.setInitialization("1");

      mm.haveMethod(editor, "public String getTime()", group.getInstanceOf("getTime").render());
      editor.withImports("java.text.DateFormat");
      editor.withImports("java.text.SimpleDateFormat");
      editor.withImports("java.util.Date");

      mm.haveMethod(editor, "public void fireCommandExecuted(ModelCommand command)",
                    group.getInstanceOf("editorFireCommandExecuted").render());
      editor.withImports("java.util.List");
      editor.withImports("java.util.ArrayList");

      mm.haveMethod(editor,
                    "public " + serviceName + "Editor addCommandListener(String commandName, CommandStream stream)",
                    group.getInstanceOf("editorAddCommandListener").render());

      haveModelCommand();
      haveRemoveCommand();
      havePatterns();
      this.haveLoadYaml(this.mm.getClassModel().getPackageName());

      haveExecuteAndParsingMethods();

      return editor;
   }

   private void haveExecuteAndParsingMethods()
   {
      mm.haveMethod(editor, "public void execute(ModelCommand command)", group.getInstanceOf("editorExecute").render());

      mm.haveMethod(editor, "public void parse(Collection<?> allObjects)", group.getInstanceOf("editorParse").render());

      mm.haveMethod(editor,
                    "private ModelCommand findCommands(List<ModelCommand> allCommands, Object currentObject)",
                    group.getInstanceOf("editorFindCommands").render());

      mm.haveMethod(editor, "private ModelCommand getFromAllCommands(List<ModelCommand> allCommands, String id)",
                    group.getInstanceOf("editorGetFromAllCommands").render());

      mm.haveMethod(editor, "public boolean equalsButTime(ModelCommand oldCommand, ModelCommand newCommand)",
                    group.getInstanceOf("editorEqualsButTime").render());

      mm.haveAttribute(editor, "commandPrototypes", "List<ModelCommand>");

      editor.withImports("java.util.*");
      editor.withImports("org.fulib.yaml.Reflector");

   }

   private void haveCommandPrototypes(Clazz commandClass)
   {
      if (editor == null
            || "RemoveCommand".equals(commandClass.getName())
            || "AddStreamCommand".equals(commandClass.getName())) {
         return;
      }

      commandPrototypeClasses.add(commandClass);

      mm.haveMethod(editor, "private List<ModelCommand> haveCommandPrototypes()", group
          .getInstanceOf("editorHaveCommandPrototypes")
          .add("classes", commandPrototypeClasses)
          .render());
   }

   public void haveService(String serviceName)
   {
      this.serviceName = serviceName;
      service = this.mm.haveClass(serviceName + "Service");
      mm.haveAttribute(service, "myPort", INT);
      mm.associate(service, "modelEditor", ONE, editor, "service", ONE);
      mm.haveAttribute(service, "reflectorMap", "ReflectorMap");
      mm.haveAttribute(service, "currentSession", STRING);
      mm.haveAttribute(service, "executor", "ExecutorService");
      mm.haveAttribute(service, "spark", "Service");

      Attribute sessionToAppMap = mm.haveAttribute(service, "sessionToAppMap",
            String.format("Map<String, %sApp>", serviceName));
      sessionToAppMap.setInitialization("new LinkedHashMap<>()");

      haveCommandStream();
      haveAddStreamCommand();

      service.withImports("java.util.Map");
      service.withImports("java.util.LinkedHashMap");
      service.withImports("spark.Service");
      service.withImports("org.fulib.yaml.ReflectorMap");
      service.withImports("java.util.concurrent.ExecutorService");

      haveStartMethod(serviceName, "// there are no streams\n");

      mm.haveMethod(service, "public String getFirstRoot(Request req, Response res)",
                    "      currentSession = \"\" + (sessionToAppMap.size() + 1);\n" + "      return root(req, res);\n");
      service.withImports("spark.Request");
      service.withImports("spark.Response");

      mm.haveMethod(service, "public String root(Request req, Response res)",
                    group.getInstanceOf("rootBody").add("serviceName", serviceName).render());

      mm.haveMethod(service, "public String cmd(Request req, Response res)",
                    group.getInstanceOf("cmdBody").add("serviceName", serviceName).render());
      service.withImports("org.json.JSONObject");
      service.withImports("org.fulib.yaml.Reflector");
      service.withImports("java.lang.reflect.Method");

      mm.haveMethod(service, "public String connect(Request req, Response res)",
                    group.getInstanceOf("serviceConnect").render());

      mm.haveMethod(service, "public CommandStream getStream(String streamName)",
                    group.getInstanceOf("serviceGetStream").render());

      service.withImports("java.util.ArrayList");
   }

   private void haveAddStreamCommand()
   {
      Clazz addStream = haveCommand("AddStreamCommand");
      mm.haveAttribute(addStream, "incommingRoute", STRING);
      mm.haveAttribute(addStream, "outgoingUrl", STRING);

      mm.haveMethod(addStream, "@Override public Object run(" + serviceName + "Editor editor)",
                    group.getInstanceOf("AddStreamCommandRun").render());
   }

   public void haveCommandStream()
   {
      Clazz commandStream = mm.haveClass("CommandStream");
      mm.haveAttribute(commandStream, "name", STRING);
      Attribute targetUrlList = mm.haveAttribute(commandStream, "targetUrlList", "List<String>");
      targetUrlList.setInitialization("new ArrayList<>()");
      Attribute oldCommands = mm.haveAttribute(commandStream, "oldCommands", "List<ModelCommand>");
      oldCommands.setInitialization("new ArrayList<>()");
      mm.associate(service, "streams", MANY, commandStream, "service", ONE);

      mm.haveMethod(commandStream, "public void publish(ModelCommand cmd)",
                    group.getInstanceOf("CommandStreamPublish").render());

      mm.haveMethod(commandStream, "public void send()", group.getInstanceOf("CommandStreamSend").render());

      mm.haveMethod(commandStream, "public void executeCommands(Collection<?> values)",
                    group.getInstanceOf("CommandStreamExecuteCommands").render());

      mm.haveMethod(commandStream, "public CommandStream start()", group.getInstanceOf("CommandStreamStart").render());

      mm.haveMethod(commandStream, "private String handlePostRequest(Request req, Response res)",
                    group.getInstanceOf("CommandStreamHandlePostRequest").render());

      Attribute attribute = mm.haveAttribute(commandStream, "activeCommands", "Map<String, ModelCommand>");
      attribute.setInitialization("new LinkedHashMap<>()");

      commandStream.withImports("org.fulib.yaml.Yaml");
      commandStream.withImports("java.net.URL");
      commandStream.withImports("java.net.HttpURLConnection");
      commandStream.withImports("java.io.*");
      commandStream.withImports("java.util.*");
      commandStream.withImports("spark.Request");
      commandStream.withImports("spark.Response");
   }

   public void haveStartMethod(String serviceName, String streamInit)
   {
      mm.haveMethod(service, "public void start()", group
          .getInstanceOf("serviceInit")
          .add("serviceName", serviceName)
          .add("streamInit", streamInit)
          .render());
   }

   public void haveMockupGUI()
   {
      Clazz appClass = mm.haveClass(this.serviceName + "App");
      mm.haveAttribute(appClass, "modelEditor", this.serviceName + "Editor");
      mm.haveAttribute(appClass, "id", STRING);
      mm.haveAttribute(appClass, "description", STRING);
      mm.haveMethod(appClass, String.format("public %1$sApp init(%1$sEditor editor)", this.serviceName),
            String.format("" +
                  "      this.modelEditor = editor;\n" +
                  "      this.setId(\"root\");\n" +
                  "      this.setDescription(\"%s App\");\n" +
                  "      return this;\n",
                  this.serviceName));

      Clazz page = mm.haveClass("Page");
      mm.haveAttribute(page, "id", STRING);
      mm.haveAttribute(page, "description", STRING);

      Clazz line = mm.haveClass("Line");
      mm.haveAttribute(line, "id", STRING);
      mm.haveAttribute(line, "description", STRING);
      mm.haveAttribute(line, "action", STRING);
      mm.haveAttribute(line, "value", STRING);

      mm.associate(appClass, "content", ONE, page,"app", ONE);
      mm.associate(page, "content", MANY, line,"page", ONE);
   }

   public Clazz haveDataClass(String dataClassName)
   {
      Clazz dataClass = mm.haveClass(serviceName + dataClassName);
      dataClasses.put(dataClassName, dataClass);
      mm.haveAttribute(dataClass, "id", STRING);
      Clazz commandClass = mm.haveClass("Have" + dataClassName + "Command");
      commandClasses.put(dataClassName, commandClass);
      commandClass.setSuperClass(this.modelCommand);

      this.editorHaveMapFor(serviceName + dataClassName);
      this.editorHaveGetOrCreateFor(serviceName + dataClassName);

      return dataClass;
   }

   @Deprecated
   private void editorHaveGetOrCreateFor(String dataClassName)
   {
      mm.haveMethod(this.editor, String.format("public %1$s getOrCreate%1$s(String id)", dataClassName),
                    group.getInstanceOf("editorGetOrCreateBody").add("dataClazz", dataClassName).render());
   }

   private void editorHaveMapFor(String entryClassName)
   {
      String mapName = StrUtil.downFirstChar(entryClassName) + "s";
      this.editorHaveMapFor(mapName, entryClassName);
   }

   private void editorHaveMapFor(String mapName, String entryClassName)
   {
      String mapType = String.format("Map<String, %s>", entryClassName);
      Attribute attribute = mm.haveAttribute(this.editor, mapName, mapType);
      attribute.setInitialization("new LinkedHashMap<>()");
   }

   private FMethod haveGetOrCreate()
   {
      FMethod fMethod = mm.haveMethod(editor, "public Object getOrCreate(Class<?> clazz, String id)",
                                      group.getInstanceOf("editorGetOrCreateBody").render());

      mm.haveMethod(editor, "public Object getObjectFrame(Class<?> clazz, String id)",
                    group.getInstanceOf("editorGetFrameBody").render());

      editor.withImports("java.lang.reflect.Method");

      mm.haveMethod(editor, "public Object getModelObject(String id)", "return mapOfModelObjects.get(id);\n");

      mm.haveMethod(editor, "public Object removeModelObject(String id)",
                    group.getInstanceOf("editorRemoveModelObject").render());

      return fMethod;
   }

   public FMethod haveLoadYaml(String modelPackageName) {
      FMethod fMethod = mm.haveMethod(this.editor, "public void loadYaml(String yamlString)",
                                      group.getInstanceOf("loadYaml").add("packageName", modelPackageName).render());
      this.editor.withImports(Yaml.class.getName());
      return fMethod;
   }

   public Attribute haveAttribute(Clazz dataClass, String attrName, String attrType)
   {
      Attribute attribute = mm.haveAttribute(dataClass, attrName, attrType);

      String dataClassName = dataClass.getName();
      String commandClassKey = dataClassName.substring(this.serviceName.length());
      Clazz commandClass = commandClasses.get(commandClassKey);
      mm.haveAttribute(commandClass, attrName, attrType);

      haveDataCommandRunMethod(dataClass, dataClassName, commandClass);

      return attribute;
   }

   public void haveAssociationWithOwnCommands(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      // have assoc
      mm.associate(sourceClass, sourceRoleName, sourceCard, targetClass, targetRoleName, targetCard);

      // HaveLinkCommand
      String sourceClassName = sourceClass.getName();
      String commandKey = sourceClassName.substring(this.serviceName.length());
      String commandName = String.format("Have%s%sLink", commandKey, StrUtil.cap(sourceRoleName));
      Clazz haveLinkCommand = this.haveCommand(commandName);
      mm.haveAttribute(haveLinkCommand, "source", STRING);
      mm.haveAttribute(haveLinkCommand, "target", STRING);
      mm.haveMethod(haveLinkCommand, "@Override public Object run(" + this.editor.getName() + " editor)", group
          .getInstanceOf("runHaveLink")
          .add("sourceClassName", sourceClass.getName())
          .add("linkName", StrUtil.cap(sourceRoleName))
          .add("targetClassName", targetClass.getName())
          .render());

      Clazz removeLinkCommand = this.haveCommand("Remove" + commandKey + StrUtil.cap(sourceRoleName) + "Link");
      mm.haveAttribute(removeLinkCommand, "source", STRING);
      mm.haveAttribute(removeLinkCommand, "target", STRING);
      mm.haveMethod(removeLinkCommand, "@Override public Object run(" + this.editor.getName() + " editor)", group
          .getInstanceOf("runRemoveLink")
          .add("sourceClassName", sourceClass.getName())
          .add("linkName", StrUtil.cap(sourceRoleName))
          .add("targetClassName", targetClass.getName())
          .render());
   }

   public void haveAssociationOwnedByDataClass(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      if (sourceCard != ONE) {
         throw new RuntimeException("haveAssociationOwnedByDataClass requires to-one cardinality for source role");
      }
      this.mm.associate(sourceClass, sourceRoleName, sourceCard, targetClass, targetRoleName, targetCard);
      String dataClassName = sourceClass.getName();
      Collection<String> roleNames = dataclassAttachedRoles.computeIfAbsent(sourceClass, k -> new ArrayList<>());
      roleNames.add(sourceRoleName);
      String commandClassName = dataClassName.substring(this.serviceName.length());
      Clazz commandClass = commandClasses.get(commandClassName);
      mm.haveAttribute(commandClass, sourceRoleName, STRING);
      haveDataCommandRunMethod(sourceClass, dataClassName, commandClass);
   }

   private void haveDataCommandRunMethod(Clazz dataClass, String dataClassName, Clazz commandClass)
   {
      String declaration = String.format("@Override public %s run(%s editor)", dataClassName, this.editor.getName());

      StringBuilder attributes = new StringBuilder();

      for (Attribute attr : dataClass.getAttributes()) {
         if ("id".equals(attr.getName())) {
            continue;
         }
         String oneAttr = String.format("dataObject.set%1$s(this.get%1$s());\n", StrUtil.cap(attr.getName()));
         attributes.append(oneAttr);
      }

      Collection<String> roleNames = dataclassAttachedRoles.get(dataClass);
      if (roleNames != null) {
         for (String attr : roleNames) {
            String getObject = String.format("%2$s %1$s = editor.getOrCreate%2$s(this.get%3$s());\n", attr, this.serviceName + StrUtil.cap(attr), StrUtil.cap(attr));
            // Product product ;
            attributes.append(getObject);
            String oneAttr = String.format("dataObject.set%1$s(%2$s);\n", StrUtil.cap(attr), attr);
            attributes.append(oneAttr);
         }
      }

      mm.haveMethod(commandClass, declaration, group
          .getInstanceOf("run")
          .add("dataClazz", dataClassName)
          .add("attributes", attributes.toString())
          .render());
   }

   public Clazz haveCommand(String className)
   {
      Clazz commandClass = mm.haveClass(className).setPropertyStyle(POJO);
      commandClasses.put(className, commandClass);
      commandClass.setSuperClass(this.modelCommand);

      haveCommandPrototypes(commandClass);

      return commandClass;
   }
}

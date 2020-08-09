package org.fulib.servicegenerator;

import org.fulib.StrUtil;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Attribute;
import org.fulib.classmodel.Clazz;
import org.fulib.classmodel.FMethod;
import org.fulib.yaml.Yaml;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import java.util.*;

import static org.fulib.builder.ClassModelBuilder.*;

public class ServiceEditor
{
   private ClassModelManager mm = new ClassModelManager();
   private Clazz modelCommand;
   private Clazz editor;
   private LinkedHashMap<String, Clazz> dataClasses = new LinkedHashMap<>();
   private LinkedHashMap<String, Clazz> commandClasses = new LinkedHashMap<>();
   private STGroupFile group;
   private Clazz removeCommand;
   private String serviceName;
   private Clazz service;

   public ServiceEditor()
   {
      group = new STGroupFile(this.getClass().getResource("templates/servicemodel.stg"));
      group.registerRenderer(String.class, new StringRenderer());
  }

   public String getServiceName()
   {
      return serviceName;
   }


   private void havePatterns()
   {
      Clazz pattern = mm.haveClass("Pattern");

      Clazz patternObject = mm.haveClass("PatternObject");
      mm.haveAttribute(patternObject, "poId", STRING);
      mm.haveAttribute(patternObject, "handleObjectClass", "Class");
      mm.haveAttribute(patternObject, "handleObject", "Object");
      mm.haveAttribute(patternObject, "kind", STRING);

      Clazz patternAttribute = mm.haveClass("PatternAttribute");
      mm.haveAttribute(patternAttribute, "handleAttrName", STRING);
      mm.haveAttribute(patternAttribute, "commandParamName", STRING);

      Clazz patternLink = mm.haveClass("PatternLink");
      mm.haveAttribute(patternLink, "handleLinkName", STRING);

      mm.haveRole(pattern, "objects", patternObject, MANY, "pattern", ONE);
      mm.haveRole(patternObject, "attributes", patternAttribute, MANY, "object", ONE);
      mm.haveRole(patternObject, "links", patternLink, MANY, "source", ONE);
      mm.haveRole(patternLink, "target", patternObject, ONE, "incommingLinks", MANY);

   }


   private void haveRemoveCommand()
   {
      removeCommand = this.haveCommand("RemoveCommand");
      String declaration = String.format("public Object run(%s editor)", this.editor.getName());
      ST st = group.getInstanceOf("removeCommandRun");
      String body = st.render();
      mm.haveMethod(removeCommand, declaration, body);

      LinkedHashSet<String> importList = removeCommand.getImportList();
      importList.add("import org.fulib.yaml.Reflector;");
      importList.add("import org.fulib.yaml.ReflectorMap;");
      importList.add("import java.lang.reflect.Method;");
   }

   private void haveModelCommand()
   {
      modelCommand = mm.haveClass("ModelCommand");
      mm.haveAttribute(modelCommand, "id", STRING);
      mm.haveAttribute(modelCommand, "time", STRING);

      String declaration = String.format("public Object run(%s editor)", this.editor.getName());
      ST st = group.getInstanceOf("modelCommandRun");
      String body = st.render();
      mm.haveMethod(modelCommand, declaration, body);

      declaration = String.format("public void undo(%s editor)", this.editor.getName());
      st = group.getInstanceOf("modelCommandUndo");
      body = st.render();
      mm.haveMethod(modelCommand, declaration, body);

      declaration = "public ModelCommand parse(Object currentObject)";
      body =  "      return null;\n";
      mm.haveMethod(modelCommand, declaration, body);

      declaration = "public Pattern havePattern()";
      body =  "      return null;\n";
      mm.haveMethod(modelCommand, declaration, body);

      declaration = "private Object getHandleObjectAttributeValue(PatternObject patternObject, String handleAttributeName)";
      st = group.getInstanceOf("modelCommandGetHandleValue");
      body = st.render();
      mm.haveMethod(modelCommand, declaration, body);

      modelCommand.getImportList().add("import org.fulib.yaml.Reflector;");
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

   public Clazz haveEditor(String modelName)
   {
      this.serviceName = modelName;
      editor = this.mm.haveClass(modelName + "Editor");

      this.editorHaveMapFor("activeCommands", "ModelCommand");
      this.editorHaveMapFor("commandListeners", "ArrayList<CommandStream>");
      this.editorHaveMapFor("mapOfFrames", "Object");
      this.editorHaveMapFor("mapOfModelObjects", "Object");
      haveGetOrCreate();

      Attribute dateFormat = this.getClassModelManager().haveAttribute(editor, "isoDateFormat", "DateFormat");
      dateFormat.setInitialization("new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\")");
      Attribute lastTime = this.getClassModelManager().haveAttribute(editor, "lastTime", STRING);
      lastTime.setInitialization("isoDateFormat.format(new Date())");
      Attribute timeDelta = this.getClassModelManager().haveAttribute(editor, "timeDelta", LONG);
      timeDelta.setInitialization("1");

      String declaration = "public String getTime()";
      ST st = group.getInstanceOf("getTime");
      String body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

      editor.getImportList().add("import java.text.DateFormat;");
      editor.getImportList().add("import java.util.Date;");


      declaration = "public void fireCommandExecuted(ModelCommand command)";
      st = group.getInstanceOf("editorFireCommandExecuted");
      body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);
      editor.getImportList().add("import java.util.ArrayList;");

      declaration = String.format("public %sEditor addCommandListener(String commandName, CommandStream stream)", serviceName);
      st = group.getInstanceOf("editorAddCommandListener");
      body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

      haveModelCommand();
      haveRemoveCommand();
      havePatterns();
      this.haveLoadYaml(this.mm.getClassModel().getPackageName());

      haveExecuteAndParsingMethods();

      return editor;
   }

   private void haveExecuteAndParsingMethods()
   {
      String declaration = "public void execute(ModelCommand command)";
      ST st = group.getInstanceOf("editorExecute");
      String body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

      declaration = "public void parse(Collection allObjects)";
      st = group.getInstanceOf("editorParse");
      body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

      declaration = "private ModelCommand findCommands(ArrayList<ModelCommand> allCommands, Object currentObject)";
      st = group.getInstanceOf("editorFindCommands");
      body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

      declaration = "private ModelCommand getFromAllCommands(ArrayList<ModelCommand> allCommands, String id)";
      st = group.getInstanceOf("editorGetFromAllCommands");
      body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

      declaration = "public boolean equalsButTime(ModelCommand oldCommand, ModelCommand newCommand)";
      st = group.getInstanceOf("editorEqualsButTime");
      body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

      this.getClassModelManager().haveAttribute(editor, "commandPrototypes", "ArrayList<ModelCommand>");

      editor.getImportList().add("import java.util.*;");
      editor.getImportList().add("import org.fulib.yaml.Reflector;");

   }

   private LinkedHashSet<Clazz> commandPrototypeClasses = new LinkedHashSet<>();

   private void haveCommandPrototypes(Clazz commandClass)
   {
      if (editor == null
            || commandClass.getName().equals("RemoveCommand")
            || commandClass.getName().equals("AddStreamCommand")) {
         return;
      }

      commandPrototypeClasses.add(commandClass);

      //
      String declaration = "private ArrayList<ModelCommand> haveCommandPrototypes()";
      ST st = group.getInstanceOf("editorHaveCommandPrototypes");
      st.add("classes", commandPrototypeClasses);
      String body = st.render();
      this.getClassModelManager().haveMethod(editor, declaration, body);

   }


   public void haveService(String serviceName)
   {
      this.serviceName = serviceName;
      service = this.mm.haveClass(serviceName + "Service");
      mm.haveAttribute(service, "myPort", INT);
      mm.haveRole(service, "modelEditor", editor, ONE, "service", ONE);
      mm.haveAttribute(service, "reflectorMap", "ReflectorMap");
      mm.haveAttribute(service, "currentSession", STRING);
      mm.haveAttribute(service, "executor", "ExecutorService ");
      mm.haveAttribute(service, "spark", "Service");

      Attribute sessionToAppMap = mm.haveAttribute(service, "sessionToAppMap",
            String.format("LinkedHashMap<String, %sApp>", serviceName));
      sessionToAppMap.setInitialization("new LinkedHashMap()");

      haveCommandStream();
      haveAddStreamCommand();

      LinkedHashSet<String> importList = service.getImportList();
      importList.add("import java.util.LinkedHashMap;");
      importList.add("import spark.Service;");
      importList.add("import org.fulib.yaml.ReflectorMap;");
      importList.add("import java.util.concurrent.ExecutorService;");

      haveStartMethod(serviceName, "// there are no streams\n");

      String body;
      ST st;

      body = "      currentSession = \"\" + (sessionToAppMap.size() + 1);\n" +
            "      return root(req, res);\n";
      String declaration = "public String getFirstRoot(Request req, Response res)";
      mm.haveMethod(service, declaration, body);
      importList.add("import spark.Request;");
      importList.add("import spark.Response;");

      declaration = "public String root(Request req, Response res)";
      st = group.getInstanceOf("rootBody");
      st.add("serviceName", serviceName);
      body = st.render();
      mm.haveMethod(service, declaration, body);
      importList.add("import org.fulib.scenarios.MockupTools;");

      declaration = "public String cmd(Request req, Response res)";
      st = group.getInstanceOf("cmdBody");
      st.add("serviceName", serviceName);
      body = st.render();
      mm.haveMethod(service, declaration, body);
      importList.add("import org.json.JSONObject;");
      importList.add("import org.fulib.yaml.Reflector;");
      importList.add("import java.lang.reflect.Method;");

      // connect
      declaration = "public String connect(Request req, Response res)";
      st = group.getInstanceOf("serviceConnect");
      body = st.render();
      mm.haveMethod(service, declaration, body);

      // getStream
      declaration = "public CommandStream getStream(String streamName)";
      st = group.getInstanceOf("serviceGetStream");
      body = st.render();
      mm.haveMethod(service, declaration, body);


      importList.add("import java.util.ArrayList;");
      importList.add("import java.net.URL;");
      importList.add("import java.net.HttpURLConnection;");
      importList.add("import java.io.DataOutputStream;");
      importList.add("import java.io.InputStream;");
      importList.add("import java.io.InputStreamReader;");
      importList.add("import java.io.BufferedReader;");
   }

   private void haveAddStreamCommand()
   {
      Clazz addStream = haveCommand("AddStreamCommand");
      mm.haveAttribute(addStream, "incommingRoute", STRING);
      mm.haveAttribute(addStream, "outgoingUrl", STRING);

      String declaration = String.format("public Object run(%sEditor editor)", serviceName);
      ST st = group.getInstanceOf("AddStreamCommandRun");
      String body = st.render();
      mm.haveMethod(addStream, declaration, body);
   }

   public void haveCommandStream()
   {
      Clazz commandStream = mm.haveClass("CommandStream");
      mm.haveAttribute(commandStream, "name", STRING);
      Attribute targetUrlList = mm.haveAttribute(commandStream, "targetUrlList", "ArrayList<String>");
      targetUrlList.setInitialization("new ArrayList<>()");
      Attribute oldCommands = mm.haveAttribute(commandStream, "oldCommands", "ArrayList<ModelCommand>");
      oldCommands.setInitialization("new ArrayList<>()");
      mm.haveRole(service, "streams", commandStream, MANY, "service", ONE);

      String declaration = "public void publish(ModelCommand cmd)";
      ST st = group.getInstanceOf("CommandStreamPublish");
      String body = st.render();
      mm.haveMethod(commandStream, declaration, body);

      declaration = "public void send()";
      st = group.getInstanceOf("CommandStreamSend");
      body = st.render();
      mm.haveMethod(commandStream, declaration, body);

      declaration = "public void executeCommands(Collection values)";
      st = group.getInstanceOf("CommandStreamExecuteCommands");
      body = st.render();
      mm.haveMethod(commandStream, declaration, body);

      declaration = String.format("public CommandStream start()",
            serviceName);
      st = group.getInstanceOf("CommandStreamStart");
      body = st.render();
      mm.haveMethod(commandStream, declaration, body);

      declaration = String.format("private String handlePostRequest(Request req, Response res)",
            serviceName);
      st = group.getInstanceOf("CommandStreamHandlePostRequest");
      body = st.render();
      mm.haveMethod(commandStream, declaration, body);

      Attribute attribute = mm.haveAttribute(commandStream, "activeCommands", "java.util.Map<String, ModelCommand>");
      attribute.setInitialization("new java.util.LinkedHashMap<>()");

      declaration = "public void addCommandsToBeStreamed(String... commandList)";
      st = group.getInstanceOf("CommandStreamAddCommandsToBeStreamed");
      body = st.render();
      mm.haveMethod(commandStream, declaration, body);

      LinkedHashSet<String> importList = commandStream.getImportList();
      importList.add("import org.fulib.yaml.Yaml;");
      importList.add("import java.net.URL;");
      importList.add("import java.net.HttpURLConnection;");
      importList.add("import java.io.*;");
      importList.add("import java.util.*;");
      importList.add("import spark.Service;");
      importList.add("import spark.Request;");
      importList.add("import spark.Response;");

   }

   public void haveStartMethod(String serviceName, String streamInit)
   {
      ST st = group.getInstanceOf("serviceInit");
      st.add("serviceName", serviceName);
      st.add("streamInit", streamInit);
      String body = st.render();
      mm.haveMethod(service, "public void start()", body);
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

      mm.haveRole(appClass, "content", page, ONE,"app", ONE);
      mm.haveRole(page, "content", line, ClassModelBuilder.MANY,"page", ONE);


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
      String declaration = String.format("public %1$s getOrCreate%1$s(String id)", dataClassName);
      ST st = group.getInstanceOf("editorGetOrCreateBody");
      st.add("dataClazz", dataClassName);
      String body = st.render();
      mm.haveMethod(this.editor, declaration, body);
   }

   private void editorHaveMapFor(String entryClassName)
   {
      String mapName = StrUtil.downFirstChar(entryClassName) + "s";
      this.editorHaveMapFor(mapName, entryClassName);
   }

   private void editorHaveMapFor(String mapName, String entryClassName)
   {
      String mapType = String.format("java.util.Map<String, %s>", entryClassName);
      Attribute attribute = mm.haveAttribute(this.editor, mapName, mapType);
      attribute.setInitialization("new java.util.LinkedHashMap<>()");
   }


   private FMethod haveGetOrCreate()
   {
      String declaration = "public Object getOrCreate(Class clazz, String id)";
      ST st = group.getInstanceOf("editorGetOrCreateBody");
      String body = st.render();
      FMethod fMethod = mm.haveMethod(editor, declaration, body);

      declaration = "public Object getObjectFrame(Class clazz, String id)";
      st = group.getInstanceOf("editorGetFrameBody");
      body = st.render();
      mm.haveMethod(editor, declaration, body);

      editor.getImportList().add("import java.lang.reflect.Method;");

      // and
      declaration = "public Object getModelObject(String id)";
      body = "   return mapOfModelObjects.get(id);\n";
      mm.haveMethod(editor, declaration, body);

      declaration = "public Object removeModelObject(String id)";
      st = group.getInstanceOf("editorRemoveModelObject");
      body = st.render();
      mm.haveMethod(editor, declaration, body);

      return fMethod;
   }



   public FMethod haveLoadYaml(String modelPackageName) {
      String declaration = "public void loadYaml(String yamlString)";
      ST st = group.getInstanceOf("loadYaml");
      st.add("packageName", modelPackageName);
      String body = st.render();
      FMethod fMethod = mm.haveMethod(this.editor, declaration, body);
      LinkedHashSet<String> importList = this.editor.getImportList();
      importList.add("import " + Yaml.class.getName() + ";");
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
      mm.haveRole(sourceClass, sourceRoleName, targetClass, sourceCard, targetRoleName, targetCard);

      // HaveLinkCommand
      String sourceClassName = sourceClass.getName();
      String commandKey = sourceClassName.substring(this.serviceName.length());
      String commandName = String.format("Have%s%sLink", commandKey, StrUtil.cap(sourceRoleName));
      Clazz haveLinkCommand = this.haveCommand(commandName);
      mm.haveAttribute(haveLinkCommand, "source", STRING);
      mm.haveAttribute(haveLinkCommand, "target", STRING);
      String declaration = String.format("public Object run(%s editor)", this.editor.getName());
      // runHaveLink(sourceClassName, linkName, targetClassName)
      ST st = group.getInstanceOf("runHaveLink");
      st.add("sourceClassName", sourceClass.getName());
      st.add("linkName", StrUtil.cap(sourceRoleName));
      st.add("targetClassName", targetClass.getName());
      String body = st.render();
      FMethod fMethod = mm.haveMethod(haveLinkCommand, declaration, body);
      fMethod.setAnnotations("@Override");

      // RemoveLinkCommand
      commandName = String.format("Remove%s%sLink", commandKey, StrUtil.cap(sourceRoleName));
      Clazz removeLinkCommand = this.haveCommand(commandName);
      mm.haveAttribute(removeLinkCommand, "source", STRING);
      mm.haveAttribute(removeLinkCommand, "target", STRING);
      declaration = String.format("public Object run(%s editor)", this.editor.getName());
      // runHaveLink(sourceClassName, linkName, targetClassName)
      st = group.getInstanceOf("runRemoveLink");
      st.add("sourceClassName", sourceClass.getName());
      st.add("linkName", StrUtil.cap(sourceRoleName));
      st.add("targetClassName", targetClass.getName());
      body = st.render();
      fMethod = mm.haveMethod(removeLinkCommand, declaration, body);
      fMethod.setAnnotations("@Override");
   }


   private Map<Clazz, Collection<String>> dataclassAttachedRoles = new LinkedHashMap<>();

   public void haveAssociationOwnedByDataClass(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      if (sourceCard != ONE) {
         throw new RuntimeException("haveAssociationOwnedByDataClass requires to-one cardinality for source role");
      }
      this.mm.haveRole(sourceClass, sourceRoleName, targetClass, sourceCard, targetRoleName, targetCard);
      String dataClassName = sourceClass.getName();
      Collection<String> roleNames = dataclassAttachedRoles.get(sourceClass);
      if (roleNames == null) {
         roleNames = new ArrayList<>();
         dataclassAttachedRoles.put(sourceClass, roleNames);
      }
      roleNames.add(sourceRoleName);
      String commandClassName = dataClassName.substring(this.serviceName.length());
      Clazz commandClass = commandClasses.get(commandClassName);
      mm.haveAttribute(commandClass, sourceRoleName, STRING);
      haveDataCommandRunMethod(sourceClass, dataClassName, commandClass);
   }


   private void haveDataCommandRunMethod(Clazz dataClass, String dataClassName, Clazz commandClass)
   {
      String declaration = String.format("public %s run(%s editor)", dataClassName, this.editor.getName());

      String attributes = "";

      for (Attribute attr : dataClass.getAttributes()) {
         if (attr.getName().equals("id")) {
            continue;
         }
         String oneAttr = String.format("dataObject.set%1$s(this.get%1$s());\n", StrUtil.cap(attr.getName()));
         attributes += oneAttr;
      }

      Collection<String> roleNames = dataclassAttachedRoles.get(dataClass);
      if (roleNames != null) {
         for (String attr : roleNames) {
            String getObject = String.format("%2$s %1$s = editor.getOrCreate%2$s(this.get%3$s());\n", attr, this.serviceName + StrUtil.cap(attr), StrUtil.cap(attr));
            // Product product ;
            attributes += getObject;
            String oneAttr = String.format("dataObject.set%1$s(%2$s);\n", StrUtil.cap(attr), attr);
            attributes += oneAttr;
         }
      }

      ST st = group.getInstanceOf("run");
      st.add("dataClazz", dataClassName);
      st.add("attributes", attributes);
      String body = st.render();
      FMethod runMethod = mm.haveMethod(commandClass, declaration, body);
      runMethod.setAnnotations("@Override");
   }


   public Clazz haveCommand(String className)
   {
      Clazz commandClass = mm.haveClass(className);
      commandClasses.put(className, commandClass);
      commandClass.setSuperClass(this.modelCommand);

      haveCommandPrototypes(commandClass);

      return commandClass;
   }




}

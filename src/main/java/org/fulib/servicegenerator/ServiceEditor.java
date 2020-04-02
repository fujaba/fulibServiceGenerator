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

   private void haveRemoveCommand()
   {
      removeCommand = this.haveCommand("RemoveCommand");
      mm.haveAttribute(removeCommand, "targetClassName", STRING);
      ST st = group.getInstanceOf("removeCommandRun");
      String declaration = String.format("public ModelCommand run(%s editor)", this.editor.getName());
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
      String declaration = String.format("public ModelCommand run(%s editor)", this.editor.getName());
      mm.haveMethod(modelCommand, declaration, "      return null;\n");
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
      this.editorHaveMapFor("RemoveCommand");

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
      body = "// st.render();\n";
      this.getClassModelManager().haveMethod(editor, declaration, body);

      haveModelCommand();
      haveRemoveCommand();
      this.haveLoadYaml(this.mm.getClassModel().getPackageName());

      return editor;
   }

   public void haveService(String serviceName)
   {
      this.serviceName = serviceName;
      service = this.mm.haveClass(serviceName + "Service");
      mm.haveAttribute(service, "myPort", INT);
      mm.haveAttribute(service, "modelEditor", serviceName + "Editor");
      mm.haveAttribute(service, "reflectorMap", "ReflectorMap");
      mm.haveAttribute(service, "currentSession", STRING);
      mm.haveAttribute(service, "executor", "ExecutorService ");

      Attribute sessionToAppMap = mm.haveAttribute(service, "sessionToAppMap",
            String.format("LinkedHashMap<String, %sApp>", serviceName));
      sessionToAppMap.setInitialization("new LinkedHashMap()");

      Clazz commandStream = mm.haveClass("CommandStream");

      mm.haveRole(service, "streams", commandStream, MANY, "service", ONE);

      LinkedHashSet<String> importList = service.getImportList();
      importList.add("import java.util.LinkedHashMap;");
      importList.add("import static spark.Spark.*;");
      importList.add("import org.fulib.yaml.ReflectorMap;");
      importList.add("import java.util.concurrent.ExecutorService;");

      haveStartMethod(serviceName, "// no streams\n");

      String body;
      ST st;

      body = "      currentSession = \"\" + (sessionToAppMap.size() + 1);\n" +
            "      return root(req, res);\n";
      mm.haveMethod(service, "public String getFirstRoot(Request req, Response res)", body);
      importList.add("import spark.Request;");
      importList.add("import spark.Response;");

      st = group.getInstanceOf("rootBody");
      st.add("serviceName", serviceName);
      body = st.render();
      mm.haveMethod(service, "public String root(Request req, Response res)", body);
      importList.add("import org.fulib.scenarios.MockupTools;");

      st = group.getInstanceOf("cmdBody");
      st.add("serviceName", serviceName);
      body = st.render();
      mm.haveMethod(service, "public String cmd(Request req, Response res)", body);
      importList.add("import org.json.JSONObject;");
      importList.add("import org.fulib.yaml.Reflector;");
      importList.add("import java.lang.reflect.Method;");
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
      this.havePreCheck(dataClassName);

      this.editorHaveMapFor(serviceName + dataClassName);
      this.editorHaveGetOrCreateFor(serviceName + dataClassName);

      return dataClass;
   }

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

   private FMethod havePreCheck(String className)
   {
      Clazz commandClass = this.commandClasses.get(className);
      String declaration = String.format("public boolean preCheck(%s editor)", this.editor.getName());
      ST st = group.getInstanceOf("preCheck");
      st.add("dataClazz", this.serviceName + className);
      String body = st.render();
      FMethod fMethod = mm.haveMethod(commandClass, declaration, body);

      return fMethod;
   }

   private FMethod haveGetOrCreate(String className)
   {
      Clazz commandClass = this.commandClasses.get(className);
      String declaration = String.format("public %s getOrCreate(%s sme)", className, this.editor.getName());
      ST st = group.getInstanceOf("getOrCreateBody");
      st.add("dataClazz", className);
      String body = st.render();
      FMethod fMethod = mm.haveMethod(commandClass, declaration, body);

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

      return commandClass;
   }



}

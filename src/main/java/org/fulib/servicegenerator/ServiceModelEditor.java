package org.fulib.servicegenerator;

import org.fulib.StrUtil;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Attribute;
import org.fulib.classmodel.Clazz;
import org.fulib.classmodel.FMethod;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import java.util.LinkedHashMap;

import static org.fulib.builder.ClassModelBuilder.STRING;

public class ServiceModelEditor
{
   private ClassModelManager mm = new ClassModelManager();
   private Clazz modelCommand;
   private Clazz editor;
   private LinkedHashMap<String, Clazz> dataClasses = new LinkedHashMap<>();
   private LinkedHashMap<String, Clazz> commandClasses = new LinkedHashMap<>();
   private STGroupFile group;
   private Clazz removeCommand;

   public ServiceModelEditor()
   {
      group = new STGroupFile("org.fulib.templates/servicemodel.stg");
      group.registerRenderer(String.class, new StringRenderer());
  }

   private void haveRemoveCommand()
   {
      removeCommand = this.haveCommand("RemoveCommand");
      mm.haveAttribute(removeCommand, "targetClassName", STRING);
      ST st = group.getInstanceOf("removeCommandRun");
      String body = st.render();
      mm.haveMethod(removeCommand, "public ModelCommand run(StoreModelEditor editor)", body);
   }

   private void haveModelCommand()
   {
      modelCommand = mm.haveClass("ModelCommand");
      mm.haveAttribute(modelCommand, "id", STRING);
      mm.haveAttribute(modelCommand, "time", STRING);
      mm.haveMethod(modelCommand, "public ModelCommand run(StoreModelEditor sme)", "      return null;\n");
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
      editor = this.mm.haveClass(modelName + "Editor");

      this.editorHaveMapFor("RemoveCommand");

      haveModelCommand();
      haveRemoveCommand();
      this.haveLoadYaml(this.mm.getClassModel().getPackageName());



      return editor;
   }

   public Clazz haveDataClass(String dataClassName)
   {
      Clazz dataClass = mm.haveClass(dataClassName);
      dataClasses.put(dataClassName, dataClass);
      mm.haveAttribute(dataClass, "id", STRING);
      Clazz commandClass = mm.haveClass("Have" + dataClassName + "Command");
      commandClasses.put(dataClassName, commandClass);
      commandClass.setSuperClass(this.modelCommand);
      this.haveGetOrCreate(dataClassName);
      this.havePreCheck(dataClassName);

      this.editorHaveMapFor(dataClassName);

      return dataClass;
   }

   private void editorHaveMapFor(String dataClassName)
   {
      String mapName = StrUtil.downFirstChar(dataClassName) + "s";
      String mapType = String.format("Map<String, %s>", dataClassName);
      Attribute attribute = mm.haveAttribute(this.editor, mapName, mapType);
      attribute.setInitialization("new LinkedHashMap<>()");
   }

   private FMethod havePreCheck(String className)
   {
      Clazz commandClass = this.commandClasses.get(className);
      String declaration = "public boolean preCheck(StoreModelEditor editor)";
      ST st = group.getInstanceOf("preCheck");
      st.add("dataClazz", className);
      String body = st.render();
      FMethod fMethod = mm.haveMethod(commandClass, declaration, body);

      return fMethod;
   }

   private FMethod haveGetOrCreate(String className)
   {
      Clazz commandClass = this.commandClasses.get(className);
      String declaration = String.format("public %s getOrCreate(StoreModelEditor sme)", className);
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
      return fMethod;
   }

   public Attribute haveAttribute(Clazz dataClass, String attrName, String attrType)
   {
      Attribute attribute = mm.haveAttribute(dataClass, attrName, attrType);

      String dataClassName = dataClass.getName();
      Clazz commandClass = commandClasses.get(dataClassName);
      mm.haveAttribute(commandClass, attrName, attrType);

      haveDataCommandRunMethod(dataClass, dataClassName, commandClass);

      return attribute;
   }

   private void haveDataCommandRunMethod(Clazz dataClass, String dataClassName, Clazz commandClass)
   {
      String declaration = String.format("public %s run(StoreModelEditor sme)", dataClassName);

      String attributes = "";

      for (Attribute attr : dataClass.getAttributes()) {
         if (attr.getName().equals("id")) {
            continue;
         }
         String oneAttr = String.format("dataObject.set%1$s(this.get%1$s());\n", StrUtil.cap(attr.getName()));
         attributes += oneAttr;
      }

      ST st = group.getInstanceOf("run");
      st.add("dataClazz", dataClassName);
      st.add("attributes", attributes);
      String body = st.render();
      FMethod runMethod = mm.haveMethod(commandClass, declaration, body);
      runMethod.setAnnotations("@Override");
   }

   public void associate(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      this.mm.haveRole(sourceClass, sourceRoleName, targetClass, sourceCard, targetRoleName, targetCard);
   }

   public Clazz haveCommand(String className)
   {
      Clazz commandClass = mm.haveClass(className);
      commandClasses.put(className, commandClass);
      commandClass.setSuperClass(this.modelCommand);

      return commandClass;
   }
}

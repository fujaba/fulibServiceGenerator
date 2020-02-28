package org.fulib.servicegenerator;

import org.fulib.StrUtil;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Attribute;
import org.fulib.classmodel.Clazz;
import org.fulib.classmodel.FMethod;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.fulib.builder.ClassModelBuilder.ONE;
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
      String declaration = String.format("public ModelCommand run(%s editor)", this.editor.getName());
      String body = st.render();
      mm.haveMethod(removeCommand, declaration, body);
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
      editor = this.mm.haveClass(modelName + "Editor");

      this.editorHaveMapFor("activeCommands", "ModelCommand");
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
      this.editorHaveGetOrCreateFor(dataClassName);

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
      String mapType = String.format("Map<String, %s>", entryClassName);
      Attribute attribute = mm.haveAttribute(this.editor, mapName, mapType);
      attribute.setInitialization("new LinkedHashMap<>()");
   }

   private FMethod havePreCheck(String className)
   {
      Clazz commandClass = this.commandClasses.get(className);
      String declaration = String.format("public boolean preCheck(%s editor)", this.editor.getName());
      ST st = group.getInstanceOf("preCheck");
      st.add("dataClazz", className);
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


   public void haveAssociationWithOwnCommands(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      // have assoc
      mm.haveRole(sourceClass, sourceRoleName, targetClass, sourceCard, targetRoleName, targetCard);

      // HaveLinkCommand
      String sourceClassName = sourceClass.getName();
      String commandName = String.format("Have%s%sLink", sourceClassName, StrUtil.cap(sourceRoleName));
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
      commandName = String.format("Remove%s%sLink", sourceClassName, StrUtil.cap(sourceRoleName));
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
      Clazz commandClass = commandClasses.get(dataClassName);
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
            String getObject = String.format("%2$s %1$s = editor.getOrCreate%2$s(this.get%2$s());\n", attr, StrUtil.cap(attr));
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

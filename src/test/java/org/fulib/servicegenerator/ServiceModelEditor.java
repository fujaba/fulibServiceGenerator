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
   private final Clazz modelCommand;
   private Clazz editor;
   private LinkedHashMap<String, Clazz> dataClasses = new LinkedHashMap<>();
   private LinkedHashMap<String, Clazz> commandClasses = new LinkedHashMap<>();

   public ServiceModelEditor()
   {
      modelCommand = mm.haveClass("ModelCommand");
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
      return editor;
   }

   public Clazz haveDataClass(String className)
   {
      Clazz dataClass = mm.haveClass(className);
      dataClasses.put(className, dataClass);
      mm.haveAttribute(dataClass, "id", STRING);
      Clazz commandClass = mm.haveClass("Have" + className + "Command");
      commandClasses.put(className, commandClass);
      commandClass.setSuperClass(this.modelCommand);
      this.haveGetOrCreate(className);
      this.havePreCheck(className);

      return dataClass;
   }

   private FMethod havePreCheck(String className)
   {
      Clazz commandClass = this.commandClasses.get(className);
      String declaration = "public boolean preCheck(StoreModelEditor editor)";
      STGroupFile group = new STGroupFile("org.fulib.templates/servicemodel.stg");
      group.registerRenderer(String.class, new StringRenderer());
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
      STGroupFile group = new STGroupFile("org.fulib.templates/servicemodel.stg");
      group.registerRenderer(String.class, new StringRenderer());
      ST st = group.getInstanceOf("getOrCreateBody");
      st.add("dataClazz", className);
      String body = st.render();
      FMethod fMethod = mm.haveMethod(commandClass, declaration, body);

      return fMethod;
   }

   public Attribute haveAttribute(Clazz dataClass, String attrName, String attrType)
   {
      Attribute attribute = mm.haveAttribute(dataClass, attrName, attrType);

      String dataClassName = dataClass.getName();
      Clazz commandClass = commandClasses.get(dataClassName);
      mm.haveAttribute(commandClass, attrName, attrType);

      String declaration = String.format("public %s run(StoreModelEditor sme)", dataClassName);

      String attributes = "";

      for (Attribute attr : dataClass.getAttributes()) {
         if (attr.getName().equals("id")) {
            continue;
         }
         String oneAttr = String.format("dataObject.set%1$s(this.get%1$s());\n", StrUtil.cap(attr.getName()));
         attributes += oneAttr;
      }

      STGroupFile group = new STGroupFile("org.fulib.templates/servicemodel.stg");
      group.registerRenderer(String.class, new StringRenderer());
      ST st = group.getInstanceOf("run");
      st.add("dataClazz", dataClassName);
      st.add("attributes", attributes);
      String body = st.render();
      FMethod runMethod = mm.haveMethod(commandClass, declaration, body);
      runMethod.setAnnotations("@Override");

      return attribute;
   }
}

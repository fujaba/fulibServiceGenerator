package org.fulib.services;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Attribute;
import org.fulib.classmodel.ClassModel;
import org.fulib.classmodel.Clazz;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.fulib.builder.Type.*;

public class SystemEditor
{
   // =============== Fields ===============

   private final Map<String, ServiceEditor> serviceMap = new LinkedHashMap<>();
   private final ClassModelManager sharedModelManager;
   private final ServiceEditor sharedEditor;

   private String mainJavaDir;
   private String packageName;
   private boolean patternSupport;

   // =============== Constructors ===============

   public SystemEditor()
   {
      sharedEditor = new ServiceEditor();
      sharedModelManager = sharedEditor.getClassModelManager();
   }

   // =============== Properties ===============

   public boolean isPatternSupport()
   {
      return patternSupport;
   }

   public void setPatternSupport(boolean patternSupport)
   {
      this.patternSupport = patternSupport;
   }

   public SystemEditor haveMainJavaDir(String mainJavaDir)
   {
      this.mainJavaDir = mainJavaDir;
      this.sharedModelManager.setMainJavaDir(this.mainJavaDir);
      return this;
   }

   public SystemEditor havePackageName(String packageName)
   {
      this.packageName = packageName;
      this.sharedModelManager.setPackageName(this.packageName);
      return this;
   }

   // =============== Methods ===============

   public void havePatterns()
   {
      ClassModelManager mm = new ClassModelManager();
      mm.setMainJavaDir(this.mainJavaDir);
      mm.setPackageName("org.fulib.patterns");

      Clazz rule = mm.haveClass("TGGRule");
      mm.haveAttribute(rule, "name", STRING);
      Clazz pattern = mm.haveClass("Pattern");

      mm.associate(rule, "left", ONE, pattern, "lparent", ONE);
      mm.associate(rule, "right", ONE, pattern, "rparent", ONE);

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

      Fulib.generator().generate(mm.getClassModel());
      FulibTools.classDiagrams().dumpSVG(mm.getClassModel(), "tmp/PatternsClassDiagram.svg");
   }

   public ServiceEditor haveService(String serviceName)
   {
      ServiceEditor serviceEditor = new ServiceEditor();
      ClassModelManager classModel = serviceEditor.getClassModelManager();
      classModel.setMainJavaDir(this.mainJavaDir);
      classModel.setPackageName(this.packageName + "." + serviceName);

      serviceEditor.haveEditor(serviceName);
      serviceEditor.haveService(serviceName);
      serviceEditor.haveMockupGUI();

      serviceMap.put(serviceName, serviceEditor);

      return serviceEditor;
   }

   public Clazz haveSharedClass(String className)
   {
      Clazz clazz = this.sharedModelManager.haveClass(className);
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         serviceEditor.haveDataClass(className);
      }
      return clazz;
   }

   public Clazz haveSharedCommand(String commandName)
   {
      Clazz clazz = this.sharedEditor.haveCommand(commandName);
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         serviceEditor.haveCommand(commandName);
      }
      return clazz;
   }

   public SystemEditor haveParameter(Clazz sharedCommand, String paramName, String attrType)
   {
      this.sharedModelManager.haveAttribute(sharedCommand, paramName, attrType);
      String commandName = sharedCommand.getName();
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         Clazz clazz = serviceEditor.haveCommand(commandName);
         serviceEditor.getClassModelManager().haveAttribute(clazz, paramName, attrType);
      }
      return this;
   }

   public SystemEditor haveAttribute(Clazz sharedClass, String attrName, String attrType)
   {
      this.sharedModelManager.haveAttribute(sharedClass, attrName, attrType);
      String className = sharedClass.getName();
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         Clazz clazz = serviceEditor.haveDataClass(className);
         serviceEditor.haveAttribute(clazz, attrName, attrType);
      }
      return this;
   }

   public void haveAssociationOwnedByDataClass(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      this.sharedModelManager.associate(sourceClass, sourceRoleName, sourceCard, targetClass, targetRoleName, targetCard);
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         Clazz localSourceClass = serviceEditor.haveDataClass(sourceClass.getName());
         Clazz localTargetClass = serviceEditor.haveDataClass(targetClass.getName());
         serviceEditor.haveAssociationOwnedByDataClass(localSourceClass, sourceRoleName, sourceCard, targetRoleName, targetCard, localTargetClass);
      }
   }

   public void haveAssociationWithOwnCommands(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      this.sharedModelManager.associate(sourceClass, sourceRoleName, sourceCard, targetClass, targetRoleName, targetCard);
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         Clazz localSourceClass = serviceEditor.haveDataClass(sourceClass.getName());
         Clazz localTargetClass = serviceEditor.haveDataClass(targetClass.getName());
         serviceEditor.haveAssociationWithOwnCommands(localSourceClass, sourceRoleName, sourceCard, targetRoleName, targetCard, localTargetClass);
      }
   }

   public void generate()
   {
      FulibTools.classDiagrams().dumpSVG(this.sharedModelManager.getClassModel(), "tmp/sharedClasses.svg");
      for (Map.Entry<String, ServiceEditor> entry : this.serviceMap.entrySet()) {
         String serviceName = entry.getKey();
         ServiceEditor serviceEditor = entry.getValue();
         ClassModel classModel = serviceEditor.getClassModelManager().getClassModel();
         Fulib.generator().generate(classModel);
         FulibTools.classDiagrams().dumpSVG(serviceEditor.getClassModelManager().getClassModel(), "tmp/" + serviceName + "ClassDiag.svg");
      }
   }
}

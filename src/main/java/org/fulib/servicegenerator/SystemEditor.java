package org.fulib.servicegenerator;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.ClassModel;
import org.fulib.classmodel.Clazz;

import java.util.LinkedHashMap;
import java.util.Map;

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

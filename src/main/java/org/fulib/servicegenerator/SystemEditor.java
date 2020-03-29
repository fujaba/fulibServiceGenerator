package org.fulib.servicegenerator;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.StrUtil;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Attribute;
import org.fulib.classmodel.ClassModel;
import org.fulib.classmodel.Clazz;
import org.fulib.classmodel.FMethod;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import java.util.*;

import static org.fulib.builder.ClassModelBuilder.ONE;
import static org.fulib.builder.ClassModelBuilder.STRING;

public class SystemEditor
{
   private String mainJavaDir;
   private Map<String, ServiceEditor> serviceMap = new LinkedHashMap<>();
   private Map<ServiceEditor, Map<ServiceEditor, LinkedHashSet<Clazz>>> messagesMap = new LinkedHashMap<>();
   private String packageName;
   private final ClassModelManager sharedModelManager;

   public SystemEditor()
   {
      sharedModelManager = new ClassModelManager();
   }

   public SystemEditor haveMainJavaDir(String mainJavaDir)
   {
      this.mainJavaDir = mainJavaDir;
      this.sharedModelManager.haveMainJavaDir(this.mainJavaDir);
      return this;
   }

   public SystemEditor havePackageName(String packageName)
   {
      this.packageName = packageName;
      this.sharedModelManager.havePackageName(this.packageName);
      return this;
   }

   public ServiceEditor haveService(String serviceName)
   {
      ServiceEditor serviceEditor = new ServiceEditor();
      ClassModelManager classModel = serviceEditor.getClassModelManager();
      classModel.haveMainJavaDir(this.mainJavaDir);
      classModel.havePackageName(this.packageName + "." + serviceName);

      serviceEditor.haveEditor(serviceName);
      serviceEditor.haveService(serviceName);
      serviceEditor.haveMockupGUI();

      serviceMap.put(serviceName, serviceEditor);


      return serviceEditor;
   }

   public void generate()
   {
      FulibTools.classDiagrams().dumpSVG(this.sharedModelManager.getClassModel(), "tmp/sharedClasse.svg");
      for (Map.Entry<String, ServiceEditor> entry : this.serviceMap.entrySet()) {
         String serviceName = entry.getKey();
         ServiceEditor serviceEditor = entry.getValue();
         ClassModel classModel = serviceEditor.getClassModelManager().getClassModel();
         Fulib.generator().generate(classModel);
         FulibTools.classDiagrams().dumpSVG(serviceEditor.getClassModelManager().getClassModel(), "tmp/" + serviceName + "ClassDiag.svg");
      }
   }

   public Clazz haveSharedClass(String className)
   {
      Clazz clazz = this.sharedModelManager.haveClass(className);
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         serviceEditor.haveDataClass(className);
      }
      return clazz;
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
      this.sharedModelManager.haveRole(sourceClass, sourceRoleName, targetClass, sourceCard, targetRoleName, targetCard);
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         Clazz localSourceClass = serviceEditor.haveDataClass(sourceClass.getName());
         Clazz localTargetClass = serviceEditor.haveDataClass(targetClass.getName());
         serviceEditor.haveAssociationOwnedByDataClass(localSourceClass, sourceRoleName, sourceCard, targetRoleName, targetCard, localTargetClass);
      }
   }

   public void haveAssociationWithOwnCommands(Clazz sourceClass, String sourceRoleName, int sourceCard, String targetRoleName, int targetCard, Clazz targetClass)
   {
      this.sharedModelManager.haveRole(sourceClass, sourceRoleName, targetClass, sourceCard, targetRoleName, targetCard);
      for (ServiceEditor serviceEditor : this.serviceMap.values()) {
         Clazz localSourceClass = serviceEditor.haveDataClass(sourceClass.getName());
         Clazz localTargetClass = serviceEditor.haveDataClass(targetClass.getName());
         serviceEditor.haveAssociationWithOwnCommands(localSourceClass, sourceRoleName, sourceCard, targetRoleName, targetCard, localTargetClass);
      }
   }

   public void haveMessages(ServiceEditor source, ServiceEditor target, Clazz data)
   {
      // sender
      Map<ServiceEditor, LinkedHashSet<Clazz>> targetMessages = messagesMap.computeIfAbsent(source, s -> new LinkedHashMap<>());
      LinkedHashSet<Clazz> clazzes = targetMessages.computeIfAbsent(target, t -> new LinkedHashSet<>());
      clazzes.add(data);

      String sourceName = source.getServiceName();
      String targetName = target.getServiceName();

      StringBuilder streamCode = new StringBuilder();

      for (Map.Entry<ServiceEditor, LinkedHashSet<Clazz>> entry : targetMessages.entrySet()) {
         ServiceEditor streamTarget = entry.getKey();
         String streamTargetName = streamTarget.getServiceName();
         LinkedHashSet<Clazz> streamClazzes = entry.getValue();
         streamCode.append("// stream from ").append(sourceName).append(" to ").append(streamTargetName).append("\n");
         streamCode.append("CommandStream stream = new CommandStream().setService(this);\n");
         String sourceToTarget = sourceName + "To" + streamTargetName;
         String targetToSource = streamTargetName + "To" + sourceName;
         streamCode.append(String.format("stream.start(\"%s\", \"http://localhost:22010/%s\", this);\n",
               targetToSource, sourceToTarget));
         for (Clazz streamClazz : streamClazzes) {
            streamCode.append(String.format("modelEditor.addCommandListener(Have%sCommand.class.getSimpleName(), stream);\n",
                  streamClazz.getName()));
         }
      }

      source.haveStartMethod(sourceName, streamCode.toString());

      // receiver
      Map<ServiceEditor, LinkedHashSet<Clazz>> sourceMessages = messagesMap.computeIfAbsent(target, t -> new LinkedHashMap<>());
      sourceMessages.computeIfAbsent(source, s -> new LinkedHashSet<>());
      target.haveStartMethod(targetName, String.format("// stream from %s to %s\n", sourceName, targetName));
   }
}

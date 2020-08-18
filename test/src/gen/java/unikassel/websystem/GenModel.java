package unikassel.websystem;

import org.fulib.FulibTools;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Clazz;
import org.fulib.servicegenerator.ServiceEditor;
import org.fulib.servicegenerator.SystemEditor;

import static org.fulib.builder.ClassModelBuilder.*;

public class GenModel
{
   public static void main(String[] args)
   {
      // genTGGPackageToDocDir();

      genTTC2020Model();

//      genBPMNToWorkFlows();
//      genQSExample();
//      genStoreShop();
   }

   private static void genTGGPackageToDocDir()
   {
      SystemEditor sysEdit = new SystemEditor();
      sysEdit.setPatternSupport(true);
      sysEdit.haveMainJavaDir("test/src/main/java");
      sysEdit.havePackageName("javaPackagesToJavaDoc");

      ServiceEditor javaPackagesEditor = sysEdit.haveService("JavaPackages");
      haveJavaPackageService(javaPackagesEditor);

      ServiceEditor javaPackagesWithPatternsEditor = sysEdit.haveService("JavaPackagesWithPatterns");
      haveJavaPackageService(javaPackagesWithPatternsEditor);

      ServiceEditor javaDocEditor = sysEdit.haveService("JavaDoc");
      haveJavaDocService(javaDocEditor);

      ServiceEditor javaDocWithPatternsEditor = sysEdit.haveService("JavaDocWithPatterns");
      haveJavaDocService(javaDocWithPatternsEditor);

      sysEdit.haveSharedCommand("HaveRoot");

      Clazz haveSubUnit = sysEdit.haveSharedCommand("HaveSubUnit");
      sysEdit.haveParameter(haveSubUnit, "parent", STRING);

      Clazz haveLeaf = sysEdit.haveSharedCommand("HaveLeaf");
      sysEdit.haveParameter(haveLeaf, "parent", STRING);
      sysEdit.haveParameter(haveLeaf, "vTag", STRING);

      sysEdit.generate();
   }


   private static ClassModelManager haveJavaDocService(ServiceEditor javaDocEditor)
   {
      ClassModelManager javaDocManager = javaDocEditor.getClassModelManager();

      Clazz folder = javaDocManager.haveClass("Folder");
      javaDocManager.haveAttribute(folder, "id", STRING);

      Clazz docFile = javaDocManager.haveClass("DocFile");
      javaDocManager.haveAttribute(docFile, "id", STRING);
      javaDocManager.haveAttribute(docFile, "version", STRING);
      javaDocManager.haveAttribute(docFile, "content", STRING);

      javaDocManager.haveRole(folder, "subFolders", folder, MANY, "up", ONE);
      javaDocManager.haveRole(folder, "files", docFile, MANY, "up", ONE);

      Clazz haveContent = javaDocEditor.haveCommand("HaveContent");
      javaDocManager.haveAttribute(haveContent, "owner", STRING);
      javaDocManager.haveAttribute(haveContent, "content", STRING);

      return javaDocManager;
   }

   private static void haveJavaPackageService(ServiceEditor javaPackagesEditor)
   {
      ClassModelManager javaPackagesManager = javaPackagesEditor.getClassModelManager();
      Clazz javaPackage = javaPackagesManager.haveClass("JavaPackage");
      javaPackagesManager.haveAttribute(javaPackage, "id", STRING);

      Clazz javaClass = javaPackagesManager.haveClass("JavaClass");
      javaPackagesManager.haveAttribute(javaClass, "id", STRING);
      javaPackagesManager.haveAttribute(javaClass, "vTag", STRING);

      javaPackagesManager.haveRole(javaPackage, "subPackages", javaPackage, MANY, "up", ONE);
      javaPackagesManager.haveRole(javaPackage, "classes", javaClass, MANY, "up", ONE);

      FulibTools.classDiagrams().dumpSVG(javaPackagesManager.getClassModel(), "tmp/JavaPackagesClassModel");
   }

   private static void genTTC2020Model()
   {
      SystemEditor sysEdit = new SystemEditor();
      sysEdit.haveMainJavaDir("test/src/main/java");
      sysEdit.havePackageName("de.hub.mse.ttc2020.solution");

      ServiceEditor m1Service = sysEdit.haveService("M1");
      ClassModelManager m1Manager = m1Service.getClassModelManager();
      Clazz person = m1Manager.haveClass("Person");
      m1Manager.haveAttribute(person, "id", STRING);
      m1Manager.haveAttribute(person, "name", STRING);
      m1Manager.haveAttribute(person, "age", INT);

      Clazz dog = m1Manager.haveClass("Dog");
      m1Manager.haveAttribute(dog, "id", STRING);
      m1Manager.haveAttribute(dog, "name", STRING);
      m1Manager.haveAttribute(dog, "age", INT);
      //      m1Manager.haveAttribute(dog, "owner", "Person");
      m1Manager.haveRole(dog, "owner", person, ONE, "dog", ONE);

      ServiceEditor m2Service = sysEdit.haveService("M2");
      ClassModelManager m2Manager = m2Service.getClassModelManager();
      person = m2Manager.haveClass("Person");
      m2Manager.haveAttribute(person, "id", STRING);
      m2Manager.haveAttribute(person, "name", STRING);
      m2Manager.haveAttribute(person, "ybirth", INT);

      dog = m2Manager.haveClass("Dog");
      m2Manager.haveAttribute(dog, "id", STRING);
      m2Manager.haveAttribute(dog, "name", STRING);
      m2Manager.haveAttribute(dog, "owner", "Person");

      Clazz havePerson = sysEdit.haveSharedCommand("HavePerson");
      sysEdit.haveParameter(havePerson, "name", STRING);
      sysEdit.haveParameter(havePerson, "age", INT);

      Clazz haveDog = sysEdit.haveSharedCommand("HaveDog");
      sysEdit.haveParameter(haveDog, "name", STRING);
      sysEdit.haveParameter(haveDog, "age", INT);
      sysEdit.haveParameter(haveDog, "owner", STRING);

      sysEdit.generate();



   }

   private static void genBPMNToWorkFlows()
   {
      SystemEditor sysEdit = new SystemEditor();
      sysEdit.haveMainJavaDir("src/main/java");
      sysEdit.havePackageName("unikassel.bpmn2wf");
      ServiceEditor bpmn = sysEdit.haveService("BPMN");
      ServiceEditor workFlows = sysEdit.haveService("WorkFlows");

      Clazz addStep = sysEdit.haveSharedCommand("AddStep");
      sysEdit.haveParameter(addStep, "taskId", STRING);
      sysEdit.haveParameter(addStep, "taskText", STRING);

      Clazz addParallel = sysEdit.haveSharedCommand("AddParallel");
      sysEdit.haveParameter(addParallel, "gateId", STRING);
      sysEdit.haveParameter(addParallel, "gateKind", STRING);

      Clazz addFlow = sysEdit.haveSharedCommand("AddFlow");
      sysEdit.haveParameter(addFlow, "source", STRING);
      sysEdit.haveParameter(addFlow, "target", STRING);

      ClassModelManager bpmnM = bpmn.getClassModelManager();
      Clazz task = bpmnM.haveClass("Task");
      bpmnM.haveAttribute(task, "id", STRING);
      bpmnM.haveAttribute(task, "text", STRING);
      bpmnM.haveAttribute(task, "kind", STRING);
      Clazz flow = bpmnM.haveClass("Flow");
      bpmnM.haveRole(flow, "source", task, ONE, "outgoing", MANY);
      bpmnM.haveRole(flow, "target", task, ONE, "incomming", MANY);
      bpmnM.haveRole(task, "kids", task, MANY, "parent", ONE);

      ClassModelManager wfM = workFlows.getClassModelManager();
      Clazz step = wfM.haveClass("Step");
      wfM.haveAttribute(step, "id", STRING);
      wfM.haveAttribute(step, "text", STRING);
      wfM.haveAttribute(step, "kind", STRING);
      wfM.haveAttribute(step, "finalFlag", BOOLEAN);

      Clazz workFlow = wfM.haveClass("Flow");
      wfM.haveAttribute(workFlow, "kind", STRING);
      wfM.haveRole(step, "next", step, MANY, "prev", MANY);
      wfM.haveRole(workFlow, "steps", step, MANY, "parent", ONE);
      wfM.haveRole(workFlow, "finalFlow", step, ONE, "finalFlow", ONE);
      wfM.haveRole(step, "invokedFlows", workFlow, MANY, "invoker", ONE);

      Clazz parse = workFlows.haveCommand("Parse");
      wfM.haveAttribute(parse, "text", STRING);

      sysEdit.generate();
   }

   private static void genQSExample()
   {
      SystemEditor sysEdit = new SystemEditor();
      sysEdit.haveMainJavaDir("src/main/java");
      sysEdit.havePackageName("unikassel.qsexample");
      ServiceEditor ramp = sysEdit.haveService("Ramp");
      ServiceEditor laboratory = sysEdit.haveService("Laboratory");
      ServiceEditor accounting = sysEdit.haveService("Accounting");
      ServiceEditor Racking = sysEdit.haveService("Storage");

      Clazz product = sysEdit.haveSharedClass("Product");
      sysEdit.haveAttribute(product, "description", STRING);

      Clazz supplier = sysEdit.haveSharedClass("Supplier");
      sysEdit.haveAttribute(supplier, "name", STRING);
      sysEdit.haveAttribute(supplier, "address", STRING);

      Clazz supply = sysEdit.haveSharedClass("Supply");
      sysEdit.haveAssociationOwnedByDataClass(supply, "supplier", ONE, "deliveries", MANY, supplier);
      sysEdit.haveAssociationOwnedByDataClass(supply, "product", ONE, "supplies", MANY, product);
      sysEdit.haveAttribute(supply, "items", DOUBLE);
      sysEdit.haveAttribute(supply, "state", STRING);

      Clazz customer = sysEdit.haveSharedClass("Customer");
      sysEdit.haveAttribute(customer, "name", STRING);
      sysEdit.haveAttribute(customer, "address", STRING);

      Clazz addSupply = accounting.haveCommand("AddSupply");
      accounting.getClassModelManager().haveAttribute(addSupply, "supplier", STRING);
      accounting.getClassModelManager().haveAttribute(addSupply, "product", STRING);
      accounting.getClassModelManager().haveAttribute(addSupply, "items", STRING);
      accounting.getClassModelManager().haveAttribute(addSupply, "_app", "AccountingApp");

      Clazz openAddPalette = ramp.haveCommand("OpenAddPalette");
      ramp.getClassModelManager().haveAttribute(openAddPalette, "supply", STRING);
      ramp.getClassModelManager().haveAttribute(openAddPalette, "_app", "RampApp");

      Clazz rampApp = ramp.getClassModelManager().haveClass("RampApp");
      ramp.getClassModelManager().haveAttribute(rampApp, "supplyId", STRING);


      sysEdit.generate();
   }

   private static void genStoreShop()
   {
      SystemEditor sysEdit = new SystemEditor();
      sysEdit.haveMainJavaDir("src/main/java");
      sysEdit.havePackageName("unikassel.websystem");
      ServiceEditor shop = sysEdit.haveService("Shop");
      ServiceEditor store = sysEdit.haveService("Store");

      Clazz product = sysEdit.haveSharedClass("Product");
      sysEdit.haveAttribute(product, "description", STRING);
      sysEdit.haveAttribute(product, "items", DOUBLE);

      Clazz customer = sysEdit.haveSharedClass("Customer");
      sysEdit.haveAttribute(customer, "name", STRING);
      sysEdit.haveAttribute(customer, "address", STRING);

      Clazz offer = sysEdit.haveSharedClass("Offer");
      sysEdit.haveAttribute(offer, "price", DOUBLE);
      sysEdit.haveAssociationOwnedByDataClass(offer, "product", ONE, "offers", MANY, product);
      sysEdit.haveAttribute(offer, "startTime", STRING);
      sysEdit.haveAttribute(offer, "endTime", STRING);

      Clazz order = sysEdit.haveSharedClass("Order");
      sysEdit.haveAssociationOwnedByDataClass(order, "customer", ONE, "orders", MANY, customer);
      sysEdit.haveAttribute(order, "date", STRING);
      sysEdit.haveAttribute(order, "state", STRING);

      Clazz orderPosition = sysEdit.haveSharedClass("OrderPosition");
      sysEdit.haveAssociationOwnedByDataClass(orderPosition, "order", ONE, "positions", MANY, order);
      sysEdit.haveAssociationOwnedByDataClass(orderPosition, "offer", ONE, "orders", MANY, offer);
      sysEdit.haveAttribute(orderPosition, "amount", DOUBLE);
      sysEdit.haveAttribute(orderPosition, "state", STRING);

      sysEdit.haveAssociationWithOwnCommands(customer, "products", MANY, "customers", MANY, product);

      Clazz shopApp = shop.getClassModelManager().haveClass("ShopApp");
      Clazz shopOrder = shop.getClassModelManager().haveClass("ShopOrder");
      shop.getClassModelManager().haveRole(shopApp, "shoppingCart", shopOrder, ONE);
      shop.getClassModelManager().haveAttribute(shopApp, "customer", "ShopCustomer");

      Clazz addToCart = shop.haveCommand("AddToCart");
      shop.getClassModelManager().haveAttribute(addToCart, "offer", STRING);
      shop.getClassModelManager().haveAttribute(addToCart, "_app", "ShopApp");

      Clazz orderAction = shop.haveCommand("OrderAction");
      shop.getClassModelManager().haveAttribute(orderAction, "order", STRING);
      shop.getClassModelManager().haveAttribute(orderAction, "name", STRING);
      shop.getClassModelManager().haveAttribute(orderAction, "address", STRING);
      shop.getClassModelManager().haveAttribute(orderAction, "_app", "ShopApp");

      Clazz customerAccount = shop.haveCommand("CustomerAccount");
      shop.getClassModelManager().haveAttribute(customerAccount, "name", STRING);
      shop.getClassModelManager().haveAttribute(customerAccount, "address", STRING);
      shop.getClassModelManager().haveAttribute(customerAccount, "_app", "ShopApp");

      sysEdit.generate();
   }

}

package org.fulib.servicegenerator;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Clazz;
import org.junit.Test;

import static org.fulib.builder.ClassModelBuilder.*;
import static org.hamcrest.CoreMatchers.is;

public class GenModel
{
   @Test
   public void genExample()
   {
      ServiceModelEditor me = new ServiceModelEditor();
      ClassModelManager mm = me.getClassModelManager();
      mm.haveMainJavaDir("src/test/java");
      mm.havePackageName("unikassel.shop.model");
      Clazz storeEditor = me.haveEditor("Store");

      Clazz product = me.haveDataClass("Product");
      me.haveAttribute(product, "description", STRING);

      Clazz customer = me.haveDataClass("Customer");
      me.haveAttribute(customer, "name", STRING);
      me.haveAttribute(customer, "address", STRING);

      Clazz offer = me.haveDataClass("Offer");
      me.haveAttribute(offer, "price", DOUBLE);
      me.haveAttribute(offer, "startTime", STRING);
      me.haveAttribute(offer, "endTime", STRING);

      Clazz removeCommand = me.haveCommand("RemoveCommand");
      mm.haveAttribute(removeCommand, "targetClassName", STRING);

      me.haveAssociationOwnedByDataClass(offer, "product", ONE, "offers", MANY, product);

      me.haveAssociationWithOwnCommands(customer, "products", MANY, "customers", MANY, product);

      Fulib.generator().generate(mm.getClassModel());
      FulibTools.classDiagrams().dumpSVG(mm.getClassModel(), "tmp/storeClasses.svg");
   }

}

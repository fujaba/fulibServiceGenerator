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
      SystemEditor sysEdit = new SystemEditor();
      sysEdit.haveMainJavaDir("src/test/java");
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
      shop.getClassModelManager().haveRole(shopApp, "shoppingCard", shopOrder, ONE);
      Clazz addToCard = shop.haveCommand("AddToCard");
      shop.getClassModelManager().haveAttribute(addToCard, "offer", STRING);
      shop.getClassModelManager().haveAttribute(addToCard, "_app", "ShopApp");

//      sysEdit.haveStream(store, product, shop);
//      sysEdit.haveStream(shop, order, store);
//      sysEdit.haveStream(store, picked, shop);



      sysEdit.generate();
   }

}

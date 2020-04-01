package unikassel.websystem;

import org.fulib.classmodel.Clazz;
import org.fulib.servicegenerator.ServiceEditor;
import org.fulib.servicegenerator.SystemEditor;

import static org.fulib.builder.ClassModelBuilder.*;

public class GenModel
{
   public static void main(String[] args)
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
      shop.getClassModelManager().haveRole(shopApp, "shoppingCard", shopOrder, ONE);
      shop.getClassModelManager().haveAttribute(shopApp, "customer", "ShopCustomer");

      Clazz addToCard = shop.haveCommand("AddTobCard");
      shop.getClassModelManager().haveAttribute(addToCard, "offer", STRING);
      shop.getClassModelManager().haveAttribute(addToCard, "_app", "ShopApp");

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

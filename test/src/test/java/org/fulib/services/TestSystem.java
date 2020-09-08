package org.fulib.services;

import org.junit.Test;
import unikassel.websystem.Shop.*;
import unikassel.websystem.Store.StoreApp;
import unikassel.websystem.Store.StoreService;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

import static com.codeborne.selenide.Selenide.*;

public class TestSystem
{

   private ShopService shopService;
   private StoreService storeService;

   @Test
   public void testStoreServiceStream() throws IOException, InterruptedException
   {
      // start shop and store
      storeService = new StoreService();
      storeService.setMyPort(22010).start();

      shopService = new ShopService();
      shopService.setMyPort(22011).start();

//      storeService.connectTo("Store", "http://localhost:22010","Shop", "http://localhost:22011",
//            HaveProductCommand.class.getSimpleName(),
//            "<->",
//            HaveCustomerCommand.class.getSimpleName(),
//            HaveOfferCommand.class.getSimpleName(),
//            HaveOrderCommand.class.getSimpleName(),
//            HaveOrderPositionCommand.class.getSimpleName());

      FulibScenarioDiagram scene1 = new FulibScenarioDiagram();
      scene1.setHtmlFileName("tmp/scene1.html");
      scene1.addServices("Store", "Shop");



      open("http://localhost:22010/Store");
      $("#idIn").$("input").setValue("p1");
      $("#descriptionIn").$("input").setValue("Pumps");
      $("#itemsIn").$("input").setValue("40");
      StoreApp storeApp = storeService.getSessionToAppMap().values().iterator().next();
      scene1.addScreen("Store", "9:00", storeApp, "[add]");
      $("#addButton").$("button").click();

      scene1.addMessages("9:01", storeService, 100);

      $("#idIn").$("input").setValue("b2");
      $("#descriptionIn").$("input").setValue("Boots");
      $("#itemsIn").$("input").setValue("30");
      scene1.addScreen("9:02", storeApp, "[add]");
      $("#addButton").$("button").click();

      Thread.sleep(200);
      scene1.addMessages("9:03", storeService);

      scene1.addData("9:04", "Store", storeService.getModelEditor().getStoreProducts().values());
      scene1.addData("9:06", 300, "Shop", shopService.getModelEditor().getShopProducts().values());

//      // find product in the shop
//      ShopEditor shopEditor = shopService.getModelEditor();
//      assertThat(shopEditor.getShopProducts().size(), is(2));
//      ShopProduct next = shopEditor.getShopProducts().values().iterator().next();
//      assertThat(next.getItems(), is(40.0));
//
//      // order pumps
//      open("http://localhost:22011/Shop");
//      scene1.addScreen("9:20", shopService.getSessionToAppMap().values().iterator().next(),
//            "[buy p1 Pumps 9.99]", "[buy b2 Boots 9.99]", "[cart]");
//      $("#buy_offer_p1_1").$("button").click();
//      scene1.addMessages("9:21", shopService, 200);
//
//      $("#buy_offer_b2_1").$("button").click();
//      scene1.addMessages("9:23", shopService);
//
//      SelenideElement cart = $(By.xpath("//button[text()='cart']"));// .$("button").click();
//      cart.click();
//
//      $("#nameIn").$("input").setValue("Alice");
//      $("#addressIn").$("input").setValue("Wonderland 1");
//      scene1.addScreen("9:24", shopService.getSessionToAppMap().values().iterator().next(), "[Buy]");
//      $(By.xpath("//button[text()='Buy']")).click();
//      scene1.addScreen("9:26", shopService.getSessionToAppMap().values().iterator().next());
//      scene1.addMessages("9:27", shopService);
//
//      scene1.addData("9:28", 500, "Store", storeService.getModelEditor().getActiveCommands().values());
//
//      scene1.dump();
      System.out.println("the end");
   }
}

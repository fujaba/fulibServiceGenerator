package org.fulib.servicegenerator;

import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;
import unikassel.websystem.Shop.ShopEditor;
import unikassel.websystem.Shop.ShopProduct;
import unikassel.websystem.Shop.ShopService;
import unikassel.websystem.Store.StoreApp;
import unikassel.websystem.Store.StoreService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

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
      storeService.addStreamUrl("ShopToStore", "http://localhost:22010/StoreToShop");
      storeService.setMyPort(22010).start();

      shopService = new ShopService();
      shopService.addStreamUrl("StoreToShop", "http://localhost:22010/ShopToStore");
      shopService.setMyPort(22010).start();

      FulibScenarioDiagram scene1 = new FulibScenarioDiagram();
      scene1.setHtmlFileName("tmp/scene1.html");
      scene1.addServices(storeService, shopService);


      open("http://localhost:22010/Store");
      $("#idIn").$("input").setValue("p1");
      $("#descriptionIn").$("input").setValue("Pumps");
      $("#itemsIn").$("input").setValue("40");
      StoreApp storeApp = storeService.getSessionToAppMap().values().iterator().next();
      scene1.addScreen("9:00", storeApp, "[add]");
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

      // find product in the shop
      ShopEditor shopEditor = shopService.getModelEditor();
      assertThat(shopEditor.getShopProducts().size(), is(2));
      ShopProduct next = shopEditor.getShopProducts().values().iterator().next();
      assertThat(next.getItems(), is(40.0));

      // order pumps
      open("http://localhost:22010/Shop");
      scene1.addScreen("9:20", shopService.getSessionToAppMap().values().iterator().next(),
            "[buy p1 Pumps 9.99]", "[buy b2 Boots 9.99]", "[card]");
      $("#buy_offer_p1_1").$("button").click();
      scene1.addMessages("9:21", shopService, 200);

      $("#buy_offer_b2_1").$("button").click();
      scene1.addMessages("9:23", shopService);

      SelenideElement card = $(By.xpath("//button[text()='card']"));// .$("button").click();
      card.click();

      $("#nameIn").$("input").setValue("Alice");
      $("#addressIn").$("input").setValue("Wonderland 1");
      scene1.addScreen("9:24", shopService.getSessionToAppMap().values().iterator().next(), "[Buy]");
      $(By.xpath("//button[text()='Buy']")).click();
      scene1.addScreen("9:26", shopService.getSessionToAppMap().values().iterator().next());
      scene1.addMessages("9:27", shopService);

      scene1.addData("9:28", 500, "Store", storeService.getModelEditor().getActiveCommands().values());

      scene1.dump();
      System.out.println("the end");
   }
}

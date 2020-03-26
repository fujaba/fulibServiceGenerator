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
      $("#idIn").$("input").setValue("pumps");
      $("#descriptionIn").$("input").setValue("Pumps");
      $("#itemsIn").$("input").setValue("40");
      StoreApp storeApp = storeService.getSessionToAppMap().values().iterator().next();
      scene1.addScreen("9:00", storeApp);
      $("#addButton").$("button").click();

      $("#idIn").$("input").setValue("boots");
      $("#descriptionIn").$("input").setValue("Boots");
      $("#itemsIn").$("input").setValue("30");
      scene1.addScreen("9:01", storeApp);
      $("#addButton").$("button").click();



      // find product in the shop
      Thread.sleep(200);
      ShopEditor shopEditor = shopService.getModelEditor();
      assertThat(shopEditor.getShopProducts().size(), is(2));
      ShopProduct next = shopEditor.getShopProducts().values().iterator().next();
      assertThat(next.getItems(), is(40.0));

      // order pumps
      open("http://localhost:22010/Shop");
      scene1.addScreen("9:20", shopService.getSessionToAppMap().values().iterator().next());
      $("#buy_offer_pumps_1").$("button").click();
      $("#buy_offer_boots_1").$("button").click();
      SelenideElement card = $(By.xpath("//button[text()='card']"));// .$("button").click();
      card.click();

      $("#nameIn").$("input").setValue("Alice");
      $("#addressIn").$("input").setValue("Wonderland 1");
      scene1.addScreen("9:25", shopService.getSessionToAppMap().values().iterator().next());
      $(By.xpath("//button[text()='Buy']")).click();

      scene1.dump();
      System.out.println("the end");
   }
}

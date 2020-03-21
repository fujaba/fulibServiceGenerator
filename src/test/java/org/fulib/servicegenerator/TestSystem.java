package org.fulib.servicegenerator;

import com.codeborne.selenide.SelenideElement;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.openqa.selenium.By;
import unikassel.websystem.Shop.ShopEditor;
import unikassel.websystem.Shop.ShopProduct;
import unikassel.websystem.Shop.ShopService;
import unikassel.websystem.Store.StoreService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

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

      URL url = new URL("http://localhost:" + 22010);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();

      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();
      assertThat(responseCode, is(200) );

      open("http://localhost:22010/Store");
      $("#idIn").$("input").setValue("sel1");
      $("#descriptionIn").$("input").setValue("Stilettos");
      $("#itemsIn").$("input").setValue("23");
      $("#addButton").$("button").click();

      // book a product at the store
      String cmd = "{\"_session\":\"1\",\"_cmd\":\"HaveProductCommand\",\"_newPage\":\"supplyPage\",\"idIn\":\"p1\",\"descriptionIn\":\"pumps\",\"itemsIn\":\"42\"}\n";
      url = new URL("http://localhost:" + 22010 + "/Storecmd");
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      DataOutputStream out = new DataOutputStream(connection.getOutputStream());
      out.writeBytes(cmd);
      out.flush();
      out.close();

      responseCode = connection.getResponseCode();

      assertThat(responseCode, is(200) );

      // find product in the shop
      ShopEditor shopEditor = shopService.getModelEditor();
      assertThat(shopEditor.getShopProducts().size(), is(2));
      ShopProduct next = shopEditor.getShopProducts().values().iterator().next();
      assertThat(next.getItems(), is(23.0));

      // order pumps
      open("http://localhost:22010/Shop");
      $("#buy_offer_p1_1").$("button").click();
      $("#buy_offer_sel1_1").$("button").click();
      SelenideElement card = $(By.xpath("//button[text()='card']"));// .$("button").click();
      card.click();

      System.out.println("the end");
   }
}

package org.fulib.servicegenerator;

import org.fulib.FulibTools;
import org.fulib.yaml.Yaml;
import org.junit.Assert;
import org.junit.Test;
import unikassel.shop.model.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

public class TestStore
{
   @Test
   public void testService() throws IOException
   {
      int port = 4599;
      StoreService service = new StoreService();
      service.setMyPort(port);
      service.init(null);

      System.out.println("Store Serice is listening on port " + port);

      URL url = new URL("http://localhost:" + port);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      System.out.println(responseCode);
   }



   @Test
   public void testNewYamlForDataWithAssocs()
   {
      Product tShirt = new Product().setId("tShirt").setDescription("Shirt 42");
      Product hoodie = new Product().setId("hoodie").setDescription("Uni Kassel Hoodie");
      Customer alice = new Customer().setId("alice").setName("Alice A.").setAddress("Wonderland 1");
      Customer bob = new Customer().setId("bob").setName("Bob A.").setAddress("Wonderland 1");

      Offer marchSpecial = new Offer().setId("marchSpecial").setProduct(tShirt).setPrice(19.99)
            .setStartTime("2020.03.01").setEndTime("2020.03.31");
      Order aliceShirt = new Order().setId("aliceShirtOrder").setCustomer(alice).setDate("2020.03.02")
            .setState("just ordered");

      alice.withProducts(tShirt, hoodie);
      bob.withProducts(tShirt);

      FulibTools.objectDiagrams().dumpSVG("tmp/yamlObjectsOriginals.svg", alice);

      String yamlString = Yaml.encode(tShirt);

      System.out.println(yamlString);

      Assert.assertThat(yamlString.contains("just ordered"), is(true));
      Assert.assertThat(yamlString, containsString("Uni Kassel Hoodie"));

      LinkedHashMap<String, Object> resultMap = Yaml.forPackage(tShirt.getClass().getPackage().getName())
            .decode(yamlString);
      Customer alice2 = (Customer) resultMap.get("alice");
      Product tShirt2 = (Product) resultMap.get("tShirt");

      FulibTools.objectDiagrams().dumpSVG("tmp/yamlObjectsPastYaml.svg", alice2);

      Assert.assertThat(alice2, is(not(alice)));
      Assert.assertThat(alice2.getProducts().size(), is(2));
      Assert.assertThat(tShirt2, is(not(tShirt)));
      Assert.assertThat(tShirt2.getCustomers().size(), is(2));
      // seems to work.
   }



   @Test
   public void testNewYaml4Commands()
   {
      StoreEditor se = new StoreEditor();
      Product tShirt = new HaveProductCommand().setId("tShirt").setTime("09:01")
            .setDescription("Cool T-Shirt")
            .run(se);
      new HaveProductCommand().setId("hoodie").setTime("09:02").setDescription("Hoodie XL").run(se);

      String yamlString = Yaml.encode(se.getActiveCommands());

      System.out.println(yamlString);

      Assert.assertThat(yamlString.startsWith("- "), is(true));
      Assert.assertThat(yamlString.contains("hoodie"), is(true));

      LinkedHashMap<String, Object> newObjects = Yaml.forPackage(se.getClass().getPackage().getName())
            .decode(yamlString);
      Assert.assertThat(newObjects.size(), is(2));

      HaveProductCommand tShirtCmd = (HaveProductCommand) newObjects.get("tShirt");
      Assert.assertThat(tShirtCmd.getTime(), is("09:01"));

   }

   @Test
   public void testRemoveCommand()
   {
      StoreEditor se = new StoreEditor();

      new HaveOfferCommand().setId("offer#42").setTime("12:01")
            .setProduct("tShirt").setPrice(24.99).setStartTime("2020.02.01")
            .setEndTime("2020.02.28").run(se);
      Assert.assertThat(se.getOffers().size(), is(1));
      new HaveOfferCommand().setId("offer#43").setTime("13:01")
            .setProduct("tShirt").setPrice(29.99).setStartTime("2020.02.01")
            .setEndTime("2020.02.28").run(se);
      Assert.assertThat(se.getOffers().size(), is(2));
      Offer offer42 = (Offer) se.getOffers().get("offer#42");
      new RemoveCommand().setId("offer#42").setTargetClassName("Offer").run(se);

      // should be ignored
      new HaveOfferCommand().setId("offer#42").setTime("13:01").setPrice(25.00).setStartTime("2020.03.01")
            .setEndTime("2020.02.28").run(se);
      Offer noOffer = se.getOffers().get("offer#42");
      Assert.assertThat(noOffer, equalTo(null));
   }

   @Test
   public void testAssocs()
   {
      StoreEditor se = new StoreEditor();
      Product tShirt = new HaveProductCommand().setId("tShirt").setTime("09:01").setDescription("Cool T-Shirt").run(se);
      new HaveProductCommand().setId("hoodie").setTime("09:02").setDescription("Hoodie XL").run(se);
      Customer alice = new HaveCustomerCommand().setId("alice").setTime("10:01").setName("Alice").setAddress("Wonderland 1").run(se);
      new HaveCustomerCommand().setId("bob").setTime("10:02").setName("Bob").setAddress("Wonderland 1").run(se);

      Offer tShirtSpecial = new HaveOfferCommand().setId("offer#42").setTime("12:00")
            .setProduct("tShirt").setPrice(23.00).setStartTime("12:00").setEndTime("18:00").run(se);

      new HaveOfferCommand().setId("offer#42").setTime("11:59")
            .setProduct("tShirt").setPrice(00.99).setStartTime("12:00").setEndTime("18:00")
            .run(se);

      Assert.assertThat(tShirtSpecial.getProduct(), is(tShirt));
      Assert.assertThat(tShirtSpecial.getPrice(), is(23.00));
      Assert.assertThat(tShirt.getOffers().contains(tShirtSpecial), is(true));
      new RemoveCommand().setId("offer#42").setTargetClassName("Offer")
            .run(se);
      Assert.assertThat(tShirt.getOffers().contains(tShirtSpecial), is(false));

      new HaveCustomerProductsLink().setTime("14:01").setSource("alice").setTarget("tShirt")
      .run(se);
      Assert.assertThat(alice.getProducts().contains(tShirt), is(true));

      new HaveCustomerProductsLink().setTime("14:00").setSource("alice").setTarget("justAnnounced")
            .run(se);
      Assert.assertThat(se.getProducts().size(), is(3));

      new RemoveCustomerProductsLink().setTime("13:00").setSource("alice").setTarget("tShirt")
      .run(se);
      Assert.assertThat(alice.getProducts().contains(tShirt), is(true));

      new RemoveCustomerProductsLink().setTime("16:00").setSource("alice").setTarget("tShirt")
            .run(se);
      Assert.assertThat(alice.getProducts().contains(tShirt), is(false));

      new HaveCustomerProductsLink().setTime("15:00").setSource("alice").setTarget("tShirt")
            .run(se);
      Assert.assertThat(alice.getProducts().contains(tShirt), is(false));

      new HaveCustomerProductsLink().setTime("17:00").setSource("alice").setTarget("tShirt")
            .run(se);
      Assert.assertThat(alice.getProducts().contains(tShirt), is(true));

   }

   @Test
   public void testCommands()
   {
      StoreEditor sme = new StoreEditor();

      HaveProductCommand haveProduct = new HaveProductCommand().setId("tShirt").setTime("09:01")
            .setDescription("Cool T-Shirt");
      Product firstProduct = haveProduct.run(sme);
      Assert.assertThat(firstProduct.getDescription(), is("Cool T-Shirt"));
      Product sameProduct = haveProduct.run(sme);
      Assert.assertThat(sameProduct, is(firstProduct));
      Assert.assertThat(sme.getProducts().size(), is(1));
      Assert.assertThat(sme.getProducts().get("tShirt"), is(firstProduct));

      HaveProductCommand hoodieCommand = new HaveProductCommand().setId("hoodie").setTime("09:20").setDescription("Blue Uni Hoodie");
      hoodieCommand.run(sme);

      haveProduct = new HaveProductCommand().setId("tShirt").setTime("09:05")
            .setDescription("Very Cool T-Shirt");
      Product changedProduct = haveProduct.run(sme);
      Assert.assertThat(firstProduct, is(changedProduct));
      Assert.assertThat(changedProduct.getDescription(), is("Very Cool T-Shirt"));

      Assert.assertThat(sme.getProducts().get("tShirt"), is(sameProduct));

      HaveCustomerCommand haveCustomerCommand = new HaveCustomerCommand().setId("alice").setTime("09:30")
            .setName("Alice").setAddress("Wonderland 1");
      Customer alice = haveCustomerCommand.run(sme);

      Assert.assertThat(alice.getAddress(), is("Wonderland 1"));

      Collection<ModelCommand> activeCommands = sme.getActiveCommands().values();

      Object[] objects = activeCommands.toArray();
      Assert.assertThat(objects.length, is(3));
      String yamlString = Yaml.encode(objects);

      // have another shop
      StoreEditor otherEditor = new StoreEditor();

      // edit the same product and customer
      HaveProductCommand tShirtCmd = new HaveProductCommand().setId("tShirt").setTime("10:05")
            .setDescription("Second Hand T-Shirt");
      tShirtCmd.run(otherEditor);

      HaveCustomerCommand aliceCmd = new HaveCustomerCommand().setId("alice").setTime("08:30")
            .setName("Alice").setAddress("unknown");
      aliceCmd.run(otherEditor);
      new HaveCustomerCommand().setId("bob").setTime("08:31")
            .setName("Bob").setAddress("Wonderland 1").run(otherEditor);

      // load events from first editor
      otherEditor.loadYaml(yamlString);

      Assert.assertThat(otherEditor.getProducts().size(), is(2));
      Assert.assertThat(otherEditor.getCustomers().size(), is(2));

      for (Map.Entry<String, Product> entry : otherEditor.getProducts().entrySet()) {
         System.out.println(entry);
      }
   }


}

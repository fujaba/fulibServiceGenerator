package org.fulib.servicegenerator;

import org.fulib.yaml.Yaml;
import org.junit.Assert;
import org.junit.Test;
import unikassel.shop.model.*;

import java.util.Collection;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class TestStore
{
   @Test
   public void testNewYaml()
   {
      StoreEditor se = new StoreEditor();
      Product tShirt = new HaveProductCommand().setId("tShirt").setTime("09:01")
            .setDescription("Cool T-Shirt")
            .run(se);
      new HaveProductCommand().setId("hoodie").setTime("09:02").setDescription("Hoodie XL").run(se);
      
      String yamlString = Yaml.encode(se.getActiveCommands());

      System.out.println(yamlString);

      Assert.assertThat(yamlString.startsWith("- "), is(true));
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

      Yaml idMap = new Yaml(Product.class.getPackage().getName());
      Object[] objects = activeCommands.toArray();
      Assert.assertThat(objects.length, is(3));
      String yamlString = idMap.encode(objects);

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

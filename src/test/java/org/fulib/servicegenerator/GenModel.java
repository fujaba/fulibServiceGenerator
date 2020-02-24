package org.fulib.servicegenerator;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.builder.ClassModelManager;
import org.fulib.classmodel.Clazz;
import org.fulib.yaml.YamlIdMap;
import org.junit.Assert;
import org.junit.Test;
import unikassel.shop.model.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.fulib.builder.ClassModelBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;
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
      Clazz storeModelEditor = me.haveEditor("StoreModel");

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

      me.associate(offer, "product", ONE, "offers", MANY, product);

      Fulib.generator().generate(mm.getClassModel());
      FulibTools.classDiagrams().dumpSVG(mm.getClassModel(), "tmp/storeClasses.svg");
   }

   @Test
   public void testRemoveCommand()
   {
      StoreModelEditor se = new StoreModelEditor();
      new HaveOfferCommand().setId("offer#42").setTime("12:01").setPrice(24.99).setStartTime("2020.02.01")
            .setEndTime("2020.02.28").run(se);
      Assert.assertThat(se.getOffers().size(), is(1));
      new HaveOfferCommand().setId("offer#43").setTime("13:01").setPrice(29.99).setStartTime("2020.02.01")
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
      StoreModelEditor se = new StoreModelEditor();
      new HaveProductCommand().setId("tShirt").setTime("09:01").setDescription("Cool T-Shirt").run(se);
      new HaveProductCommand().setId("hoodie").setTime("09:02").setDescription("Hoodie XL").run(se);
      new HaveCustomerCommand().setId("alice").setTime("10:01").setName("Alice").setAddress("Wonderland 1").run(se);
      new HaveCustomerCommand().setId("bob").setTime("10:02").setName("Bob").setAddress("Wonderland 1").run(se);

   }

   @Test
   public void testCommands()
   {
      StoreModelEditor sme = new StoreModelEditor();

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

      YamlIdMap idMap = new YamlIdMap(Product.class.getPackage().getName());
      Object[] objects = activeCommands.toArray();
      Assert.assertThat(objects.length, is(3));
      String yamlString = idMap.encode(objects);

      // have another shop
      StoreModelEditor otherEditor = new StoreModelEditor();

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

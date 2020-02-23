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
      ServiceModelEditor sme = new ServiceModelEditor();
      ClassModelManager mm = sme.getClassModelManager();
      mm.haveMainJavaDir("src/test/java");
      mm.havePackageName("unikassel.shop.model");
      Clazz storeModelEditor = sme.haveEditor("StoreModel");

      Clazz product = sme.haveDataClass("Product");
      sme.haveAttribute(product, "description", STRING);

      Clazz customer = sme.haveDataClass("Customer");
      sme.haveAttribute(customer, "name", STRING);
      sme.haveAttribute(customer, "address", STRING);

      Fulib.generator().generate(mm.getClassModel());
      FulibTools.classDiagrams().dumpSVG(mm.getClassModel(), "tmp/storeClasses.svg");
   }

   @Test
   public void testStore()
   {
      StoreModelEditor sme = new StoreModelEditor();

      HaveProductCommand haveProduct = new HaveProductCommand().setId("tShirt").setTime("09:01")
            .setDescription("Cool T-Shirt");
      Product firstProduct = haveProduct.run(sme);
      Assert.assertThat(firstProduct.getDescription(), is("Cool T-Shirt"));
      Product sameProduct = haveProduct.run(sme);
      Assert.assertThat(sameProduct, equalTo(null));
      Assert.assertThat(sme.getModel().size(), is(1));
      Assert.assertThat(sme.getModel().get("Product-tShirt"), is(firstProduct));

      HaveProductCommand hoodieCommand = new HaveProductCommand().setId("hoodie").setTime("09:20").setDescription("Blue Uni Hoodie");
      hoodieCommand.run(sme);

      haveProduct = new HaveProductCommand().setId("tShirt").setTime("09:05")
            .setDescription("Very Cool T-Shirt");
      Product changedProduct = haveProduct.run(sme);
      Assert.assertThat(firstProduct, is(changedProduct));
      Assert.assertThat(changedProduct.getDescription(), is("Very Cool T-Shirt"));

      Assert.assertThat(sme.getModel().get("product-tShirt"), is(sameProduct));

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

      Assert.assertThat(otherEditor.getModel().size(), is(4));
      for (Map.Entry<String, Object> entry : otherEditor.getModel().entrySet()) {
         System.out.println(entry);
      }
   }


}

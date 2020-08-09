package de.hub.mse.ttc2020.solution;

import de.hub.mse.ttc2020.solution.M1.*;
import de.hub.mse.ttc2020.solution.M2.M2Editor;
import org.antlr.v4.runtime.atn.StarLoopEntryState;
import org.fulib.FulibTools;
import org.fulib.servicegenerator.FulibPatternDiagram;
import org.fulib.yaml.Yaml;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestTTCEditors
{
   @Test
   public void testPatternDiagram() throws IOException
   {
      Files.deleteIfExists(Paths.get("tmp/HaveDogPattern.svg"));
      HaveDog haveDog = new HaveDog();
      Pattern pattern = haveDog.havePattern();
      new FulibPatternDiagram().dump("tmp/HaveDogPattern.svg", pattern);

      Path path = Paths.get("tmp/HaveDogPattern.svg");
      assertThat(Files.exists(path), is(true));

   }

   @Test
   public void testParsing()
   {
      M1Editor m1Editor = new M1Editor();

      ModelCommand haveAlice = new HavePerson().setName("Alice").setAge(23).setId("alice1");
      m1Editor.execute(haveAlice);
      Person alice1 = (Person) m1Editor.getModelObject("alice1");
      assertThat(alice1, CoreMatchers.notNullValue());

      ModelCommand haveBob = new HaveDog().setName("Bob").setAge(2).setOwner("alice1").setId("bob2");
      m1Editor.execute(haveBob);
      Dog bob2 = (Dog) m1Editor.getModelObject("bob2");
      assertThat(bob2, CoreMatchers.notNullValue());

      // test parsing
      Person carli = new Person().setId("carli").setName("Carli").setAge(42);
      bob2.setOwner(carli);

      LinkedHashSet allObjects = new Yaml(bob2.getClass().getPackage().getName()).collectObjects(bob2);
      m1Editor.parse(allObjects);

      assertThat(m1Editor.getActiveCommands().size(), is(3));
      ModelCommand alice1Command = m1Editor.getActiveCommands().get("alice1");
      assertThat(m1Editor.getActiveCommands().get("carli"), CoreMatchers.notNullValue());
      assertThat(m1Editor.getActiveCommands().get("bob2"), CoreMatchers.notNullValue());
      assertThat(bob2.getOwner(), is(carli));
   }


   @Test
   public void testAliceAndDog()
   {
      M1Editor m1Editor = new M1Editor();

      ModelCommand haveAlice = new HavePerson().setName("Alice").setAge(23).setId("alice1");
      m1Editor.execute(haveAlice);

      Person alice1 = (Person) m1Editor.getModelObject("alice1");
      assertThat(alice1, CoreMatchers.notNullValue());
      assertThat(alice1.getName(), is("Alice"));
      assertThat(alice1.getAge(), is(23));

      ModelCommand haveBob = new HaveDog().setName("Bob").setAge(2).setOwner("alice1").setId("bob2");
      m1Editor.execute(haveBob);

      Dog bob2 = (Dog) m1Editor.getModelObject("bob2");
      assertThat(bob2, CoreMatchers.notNullValue());
      assertThat(bob2.getName(), is("Bob"));
      assertThat(bob2.getAge(), is(2));
      assertThat(bob2.getOwner(), is(alice1));

      // forward to M2
      M2Editor m2Editor = new M2Editor();
      forwardToM2(m1Editor, m2Editor);

      de.hub.mse.ttc2020.solution.M2.Person m2Alice = (de.hub.mse.ttc2020.solution.M2.Person) m2Editor.getModelObject("alice1");
      assertThat(m2Alice, CoreMatchers.notNullValue());
      assertThat(m2Alice.getName(), is("Alice"));
      assertThat(m2Alice.getYbirth(), is(2020 - 23));

      de.hub.mse.ttc2020.solution.M2.Dog m2Bob = (de.hub.mse.ttc2020.solution.M2.Dog) m2Editor.getModelObject("bob2");
      assertThat(m2Bob, CoreMatchers.notNullValue());
      assertThat(m2Bob.getName(), is("Bob"));
      assertThat(m2Bob.getOwner(), is(m2Alice));

      // collaborative editing
      ModelCommand haveAlice2 = new HavePerson().setName("Alice").setAge(24).setId("alice1");
      m1Editor.execute(haveAlice2);
      assertThat(alice1.getAge(), is(24));

      de.hub.mse.ttc2020.solution.M2.HaveDog haveDog2 = (de.hub.mse.ttc2020.solution.M2.HaveDog)
            new de.hub.mse.ttc2020.solution.M2.HaveDog().setName("Bobby").setAge(2).setOwner("alice1").setId("bob2");
      m2Editor.execute(haveDog2);
      assertThat(m2Bob.getName(), is("Bobby"));

      // forward to M2
      forwardToM2(m1Editor, m2Editor);
      assertThat(m2Alice.getYbirth(), is(2020 - 24));
      assertThat(m2Bob.getName(), is("Bobby"));

      backwardToM1(m2Editor, m1Editor);
      assertThat(bob2.getName(), is("Bobby"));

      // remove alice
      ModelCommand removeAlice1 = new RemoveCommand().setId("alice1");
      m1Editor.execute(removeAlice1);
      Person alice1Zombie = (Person) m1Editor.getModelObject("alice1");
      assertThat(alice1Zombie, nullValue());

      // reuse of alice1 should fail
      ModelCommand haveZombie = new HavePerson().setName("Zombie Alice").setAge(24).setId("alice1");
      m1Editor.execute(haveZombie);
      alice1Zombie = (Person) m1Editor.getModelObject("alice1");
      assertThat(alice1Zombie, nullValue());

      // re-adding Alice as alice2
      ModelCommand haveAlice3 = new HavePerson().setName("Alice").setAge(25).setId("alice2");
      m1Editor.execute(haveAlice3);
      Person alice2 = (Person) m1Editor.getModelObject("alice2");
      assertThat(alice2, notNullValue());

      // move the dog to the new alice2
      ModelCommand haveBob2 = new HaveDog().setName("Bob").setAge(2).setOwner("alice2").setId("bob2");
      m1Editor.execute(haveBob2);

      FulibTools.objectDiagrams().dumpSVG("tmp/M1Model.svg", m1Editor.getMapOfModelObjects().values());


      assertThat(bob2, CoreMatchers.notNullValue());
      assertThat(bob2.getName(), is("Bob"));
      assertThat(bob2.getAge(), is(2));
      assertThat(bob2.getOwner(), is(alice2));
      assertThat(alice2.getDog(), is(bob2));

      forwardToM2(m1Editor, m2Editor);
      m2Alice = (de.hub.mse.ttc2020.solution.M2.Person) m2Editor.getModelObject("alice1");
      assertThat(m2Alice, nullValue());
      m2Alice = (de.hub.mse.ttc2020.solution.M2.Person) m2Editor.getModelObject("alice2");
      assertThat(m2Alice, CoreMatchers.notNullValue());
      assertThat(m2Alice.getName(), is("Alice"));
      assertThat(m2Alice.getYbirth(), is(2020 - 25));

      FulibTools.objectDiagrams().dumpSVG("tmp/M2Model.svg", m2Editor.getMapOfModelObjects().values());

      // remove the dog
      ModelCommand removeDog = new RemoveCommand().setId("bob2");
      m1Editor.execute(removeDog);

      assertThat(m1Editor.getModelObject("bob2"), nullValue());
      assertThat(alice2.getDog(), nullValue());
   }

   private void forwardToM2(M1Editor m1Editor, M2Editor m2Editor)
   {
      Map<String, ModelCommand> activeCommands = m1Editor.getActiveCommands();
      String yaml = Yaml.encode(activeCommands.values());
      m2Editor.loadYaml(yaml);
   }

   private void backwardToM1(M2Editor m2Editor, M1Editor m1Editor)
   {
      Map<String, de.hub.mse.ttc2020.solution.M2.ModelCommand> activeCommands = m2Editor.getActiveCommands();
      String yaml = Yaml.encode(activeCommands.values());
      m1Editor.loadYaml(yaml);
   }
}

package de.hub.mse.ttc2020.solution;

import de.hub.mse.ttc2020.solution.M1.*;
import de.hub.mse.ttc2020.solution.M2.M2Editor;
import org.fulib.yaml.Yaml;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestTTCEditors
{
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

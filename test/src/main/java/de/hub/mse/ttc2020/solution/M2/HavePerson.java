package de.hub.mse.ttc2020.solution.M2;

import java.util.Objects;

public class HavePerson extends ModelCommand
{
   @Override
   public Object run(M2Editor editor)
   {
      Person person = (Person) editor.getOrCreate(Person.class, getId());
      person.setName(name).setYbirth(2020 - age);
      return person;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getName());
      return result.toString();
   }

   public String getName()
   {
      return this.name;
   }

   public HavePerson setName(String value)
   {
      this.name = value;
      return this;
   }

   public int getAge()
   {
      return this.age;
   }

   public HavePerson setAge(int value)
   {
      this.age = value;
      return this;
   }
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_age = "age";
   private int age;

}

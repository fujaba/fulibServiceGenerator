package de.hub.mse.ttc2020.solution.M2;

import java.util.Objects;
import org.fulib.patterns.*;

public class HaveDog extends ModelCommand
{
   @Override
   public Object run(M2Editor editor)
   {
      Dog dog = (Dog) editor.getOrCreate(Dog.class, getId());
      Person ownerObject = (Person) editor.getObjectFrame(Person.class, owner);
      dog.setName(name).setOwner(ownerObject);

      return dog;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getOwner());
      return result.toString();
   }

   public String getName()
   {
      return this.name;
   }

   public HaveDog setName(String value)
   {
      this.name = value;
      return this;
   }

   public int getAge()
   {
      return this.age;
   }

   public HaveDog setAge(int value)
   {
      this.age = value;
      return this;
   }

   public String getOwner()
   {
      return this.owner;
   }

   public HaveDog setOwner(String value)
   {
      this.owner = value;
      return this;
   }
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_age = "age";
   private int age;
   public static final String PROPERTY_owner = "owner";
   private String owner;

}

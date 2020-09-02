package de.hub.mse.ttc2020.solution.M2;

import java.util.Objects;

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
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_name, oldValue, value);
      return this;
   }

   public int getAge()
   {
      return this.age;
   }

   public HaveDog setAge(int value)
   {
      if (value == this.age)
      {
         return this;
      }

      final int oldValue = this.age;
      this.age = value;
      this.firePropertyChange(PROPERTY_age, oldValue, value);
      return this;
   }

   public String getOwner()
   {
      return this.owner;
   }

   public HaveDog setOwner(String value)
   {
      if (Objects.equals(value, this.owner))
      {
         return this;
      }

      final String oldValue = this.owner;
      this.owner = value;
      this.firePropertyChange(PROPERTY_owner, oldValue, value);
      return this;
   }
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_age = "age";
   private int age;
   public static final String PROPERTY_owner = "owner";
   private String owner;

}

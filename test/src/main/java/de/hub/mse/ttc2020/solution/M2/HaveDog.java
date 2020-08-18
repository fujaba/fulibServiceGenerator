package de.hub.mse.ttc2020.solution.M2;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;import java.util.Objects;

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

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
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

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_age = "age";
   private int age;
   public static final String PROPERTY_owner = "owner";
   private String owner;

}
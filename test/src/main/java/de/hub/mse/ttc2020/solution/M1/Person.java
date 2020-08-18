package de.hub.mse.ttc2020.solution.M1;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class Person
{

   protected PropertyChangeSupport listeners;

   public static final String PROPERTY_dog = "dog";

   private Dog dog;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_age = "age";
   private int age;

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
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public Dog getDog()
   {
      return this.dog;
   }

   public Person setDog(Dog value)
   {
      if (this.dog == value)
      {
         return this;
      }

      final Dog oldValue = this.dog;
      if (this.dog != null)
      {
         this.dog = null;
         oldValue.setOwner(null);
      }
      this.dog = value;
      if (value != null)
      {
         value.setOwner(this);
      }
      this.firePropertyChange(PROPERTY_dog, oldValue, value);
      return this;
   }

   public void removeYou()
   {
      this.setDog(null);
   }

   public String getId()
   {
      return this.id;
   }

   public Person setId(String value)
   {
      if (Objects.equals(value, this.id))
      {
         return this;
      }

      final String oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_id, oldValue, value);
      return this;
   }

   public String getName()
   {
      return this.name;
   }

   public Person setName(String value)
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

   public Person setAge(int value)
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

}
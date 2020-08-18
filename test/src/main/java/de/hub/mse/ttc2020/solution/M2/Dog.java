package de.hub.mse.ttc2020.solution.M2;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class Dog
{

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_owner = "owner";
   private Person owner;

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

   public String getId()
   {
      return this.id;
   }

   public Dog setId(String value)
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

   public Dog setName(String value)
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

   public Person getOwner()
   {
      return this.owner;
   }

   public Dog setOwner(Person value)
   {
      if (Objects.equals(value, this.owner))
      {
         return this;
      }

      final Person oldValue = this.owner;
      this.owner = value;
      this.firePropertyChange(PROPERTY_owner, oldValue, value);
      return this;
   }

}
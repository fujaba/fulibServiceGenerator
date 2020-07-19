package de.hub.mse.ttc2020.solution.M2;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Person  
{

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public Person setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_ybirth = "ybirth";

   private int ybirth;

   public int getYbirth()
   {
      return ybirth;
   }

   public Person setYbirth(int value)
   {
      if (value != this.ybirth)
      {
         int oldValue = this.ybirth;
         this.ybirth = value;
         firePropertyChange("ybirth", oldValue, value);
      }
      return this;
   }

   protected PropertyChangeSupport listeners = null;

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (listeners != null)
      {
         listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getId());
      result.append(" ").append(this.getName());


      return result.substring(1);
   }

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Person setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

}
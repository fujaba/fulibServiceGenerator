package de.hub.mse.ttc2020.solution.M1;

import org.fulib.servicegenerator.FulibPatternDiagram;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.Table;
import org.fulib.yaml.Reflector;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import java.util.*;

public class HaveDog extends ModelCommand  
{

   protected PropertyChangeSupport listeners = null;

   public static final String PROPERTY_name = "name";

   private String name;

   public static final String PROPERTY_age = "age";

   private int age;

   public static final String PROPERTY_owner = "owner";

   private String owner;

   private static Pattern pattern = null;

   @Override
   public Pattern havePattern() {
      if (pattern == null) {
         pattern = new Pattern();
         PatternObject dog = new PatternObject().setPattern(pattern).setPoId("dog").setHandleObjectClass(Dog.class).setKind("core");
         new PatternAttribute().setObject(dog).setHandleAttrName(Dog.PROPERTY_id).setCommandParamName(HaveDog.PROPERTY_id);
         new PatternAttribute().setObject(dog).setHandleAttrName(Dog.PROPERTY_name).setCommandParamName(HaveDog.PROPERTY_name);
         new PatternAttribute().setObject(dog).setHandleAttrName(Dog.PROPERTY_age).setCommandParamName(HaveDog.PROPERTY_age);

         PatternObject person = new PatternObject().setPattern(pattern).setPoId("person").setHandleObjectClass(Person.class).setKind("context");
         new PatternAttribute().setObject(person).setHandleAttrName(Person.PROPERTY_id).setCommandParamName(HaveDog.PROPERTY_owner);

         new PatternLink().setSource(dog).setHandleLinkName(Dog.PROPERTY_owner).setTarget(person);

         new FulibPatternDiagram().dump("tmp/HaveDogPattern.svg", pattern);
      }
      return pattern;
   }

   public String getName()
   {
      return name;
   }

   public HaveDog setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
      }
      return this;
   }

   public int getAge()
   {
      return age;
   }

   public HaveDog setAge(int value)
   {
      if (value != this.age)
      {
         int oldValue = this.age;
         this.age = value;
         firePropertyChange("age", oldValue, value);
      }
      return this;
   }

   public String getOwner()
   {
      return owner;
   }

   public HaveDog setOwner(String value)
   {
      if (value == null ? this.owner != null : ! value.equals(this.owner))
      {
         String oldValue = this.owner;
         this.owner = value;
         firePropertyChange("owner", oldValue, value);
      }
      return this;
   }

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

      result.append(" ").append(this.getName());
      result.append(" ").append(this.getOwner());


      return result.substring(1);
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
   }

}

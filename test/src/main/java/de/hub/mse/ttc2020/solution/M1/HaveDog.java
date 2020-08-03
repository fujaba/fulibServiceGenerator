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
   private static Pattern pattern = null;

   protected PropertyChangeSupport listeners = null;

   public static final String PROPERTY_name = "name";

   private String name;

   public static final String PROPERTY_age = "age";

   private int age;

   public static final String PROPERTY_owner = "owner";

   private String owner;

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

   @Override
   public ModelCommand parse(Object currentObject)
   {
      Pattern pattern = havePattern();

      if (pattern == null) {
         return null;
      }

      PatternObject firstPatternObject = pattern.getObjects().get(0);
      if ( ! firstPatternObject.getHandleObjectClass().equals(currentObject.getClass())) {
         // not my business
         return null;
      }

      ObjectTable objectTable = new ObjectTable(firstPatternObject.getPoId(), currentObject);
      LinkedHashMap<PatternObject, ObjectTable> mapPatternObject2Table = new LinkedHashMap<>();
      mapPatternObject2Table.put(firstPatternObject, objectTable);

      matchAttributesAndLinks(pattern, mapPatternObject2Table, firstPatternObject, objectTable);

      // retrieve command
      ArrayList rows = new ArrayList();
      objectTable.filterRows( m -> { rows.add(m); return true; });
      Map<String, Object> firstRow = (Map<String, Object>) rows.get(0);
      HaveDog newCommand = new HaveDog();
      Reflector commandReflector = new Reflector().setClazz(newCommand.getClass());
      for (PatternObject patternObject : pattern.getObjects()) {
         String poId = patternObject.getPoId();
         for (PatternAttribute attribute : patternObject.getAttributes()) {
            String commandParamName = attribute.getCommandParamName();
            Object value = firstRow.get(poId + "." + attribute.getHandleAttrName());
            commandReflector.setValue(newCommand, commandParamName, "" + value);
         }
      }

      return newCommand;


//      if (currentObject instanceof Dog) {
//         // yes, its me
//         Dog currentDog = (Dog) currentObject;
//         HaveDog modelCommand = new HaveDog();
//         modelCommand.setId(currentDog.getId());
//         modelCommand.setName(currentDog.getName())
//               .setAge(currentDog.getAge());
//         modelCommand.setOwner(currentDog.getOwner().getId());
//
//         return modelCommand;
//      }
//
//      return null;
   }

   private void matchAttributesAndLinks(Pattern pattern, LinkedHashMap<PatternObject, ObjectTable> mapPatternObject2Table, PatternObject currentPatternObject, ObjectTable objectTable)
   {
      // match attributes
      String poId = currentPatternObject.getPoId();
      for (PatternAttribute attribute : currentPatternObject.getAttributes()) {
         String attrName = attribute.getHandleAttrName();
         objectTable.expandAttribute(poId + "." +attrName, attrName);
      }

      // match links
      for (PatternLink link : currentPatternObject.getLinks()) {
         PatternObject target = link.getTarget();
         ObjectTable targetTable = mapPatternObject2Table.get(target);

         if (targetTable != null) {
            objectTable.hasLink(link.getHandleLinkName(), targetTable);
         }
         else {
            targetTable = objectTable.expandLink(target.getPoId(), link.getHandleLinkName());
            mapPatternObject2Table.put(target, targetTable);
            matchAttributesAndLinks(pattern, mapPatternObject2Table, target, targetTable);
         }
      }
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

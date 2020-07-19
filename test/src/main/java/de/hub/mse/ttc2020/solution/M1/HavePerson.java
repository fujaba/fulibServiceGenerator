package de.hub.mse.ttc2020.solution.M1;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HavePerson extends ModelCommand  
{
   @Override
   public Object run(M1Editor editor)
   {
      Person person = (Person) editor.getOrCreate(Person.class, getId());
      person.setName(name).setAge(age);
      return person;
   }

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public HavePerson setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_age = "age";

   private int age;

   public int getAge()
   {
      return age;
   }

   public HavePerson setAge(int value)
   {
      if (value != this.age)
      {
         int oldValue = this.age;
         this.age = value;
         firePropertyChange("age", oldValue, value);
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

      result.append(" ").append(this.getName());


      return result.substring(1);
   }

   public boolean preCheck(M1Editor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("HavePerson-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("HavePerson-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("HavePerson-" + this.getId(), this);
      return true;
   }

}

package de.hub.mse.ttc2020.solution.M1;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class PatternAttribute  
{

   public static final String PROPERTY_handleAttrName = "handleAttrName";

   private String handleAttrName;

   public String getHandleAttrName()
   {
      return handleAttrName;
   }

   public PatternAttribute setHandleAttrName(String value)
   {
      if (value == null ? this.handleAttrName != null : ! value.equals(this.handleAttrName))
      {
         String oldValue = this.handleAttrName;
         this.handleAttrName = value;
         firePropertyChange("handleAttrName", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_commandParamName = "commandParamName";

   private String commandParamName;

   public String getCommandParamName()
   {
      return commandParamName;
   }

   public PatternAttribute setCommandParamName(String value)
   {
      if (value == null ? this.commandParamName != null : ! value.equals(this.commandParamName))
      {
         String oldValue = this.commandParamName;
         this.commandParamName = value;
         firePropertyChange("commandParamName", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_object = "object";

   private PatternObject object = null;

   public PatternObject getObject()
   {
      return this.object;
   }

   public PatternAttribute setObject(PatternObject value)
   {
      if (this.object != value)
      {
         PatternObject oldValue = this.object;
         if (this.object != null)
         {
            this.object = null;
            oldValue.withoutAttributes(this);
         }
         this.object = value;
         if (value != null)
         {
            value.withAttributes(this);
         }
         firePropertyChange("object", oldValue, value);
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

      result.append(" ").append(this.getHandleAttrName());
      result.append(" ").append(this.getCommandParamName());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setObject(null);

   }

}
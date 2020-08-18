package javaPackagesToJavaDoc.JavaPackages;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class PatternAttribute
{

   public static final String PROPERTY_object = "object";

   private PatternObject object;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_handleAttrName = "handleAttrName";
   private String handleAttrName;
   public static final String PROPERTY_commandParamName = "commandParamName";
   private String commandParamName;

   public PatternObject getObject()
   {
      return this.object;
   }

   public PatternAttribute setObject(PatternObject value)
   {
      if (this.object == value)
      {
         return this;
      }

      final PatternObject oldValue = this.object;
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
      this.firePropertyChange(PROPERTY_object, oldValue, value);
      return this;
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
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getHandleAttrName());
      result.append(' ').append(this.getCommandParamName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setObject(null);
   }

   public String getHandleAttrName()
   {
      return this.handleAttrName;
   }

   public PatternAttribute setHandleAttrName(String value)
   {
      if (Objects.equals(value, this.handleAttrName))
      {
         return this;
      }

      final String oldValue = this.handleAttrName;
      this.handleAttrName = value;
      this.firePropertyChange(PROPERTY_handleAttrName, oldValue, value);
      return this;
   }

   public String getCommandParamName()
   {
      return this.commandParamName;
   }

   public PatternAttribute setCommandParamName(String value)
   {
      if (Objects.equals(value, this.commandParamName))
      {
         return this;
      }

      final String oldValue = this.commandParamName;
      this.commandParamName = value;
      this.firePropertyChange(PROPERTY_commandParamName, oldValue, value);
      return this;
   }

}
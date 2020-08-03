package de.hub.mse.ttc2020.solution.M1;
import org.fulib.yaml.Reflector;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;import java.util.Objects;

public class ModelCommand  
{
   protected PropertyChangeSupport listeners = null;

   public static final String PROPERTY_id = "id";

   private String id;

   public static final String PROPERTY_time = "time";

   private String time;

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
      result.append(" ").append(this.getTime());


      return result.substring(1);
   }

   public void removeYou()
   {
   }

   public String getId()
   {
      return id;
   }

   public ModelCommand setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public String getTime()
   {
      return time;
   }

   public ModelCommand setTime(String value)
   {
      if (value == null ? this.time != null : ! value.equals(this.time))
      {
         String oldValue = this.time;
         this.time = value;
         firePropertyChange("time", oldValue, value);
      }
      return this;
   }

   public ModelCommand parse(Object currentObject)
   {
      return null;
   }

   public boolean equalsButTime(ModelCommand newCommand)
   {
      Reflector reflector = new Reflector().setClazz(this.getClass());

      for (String property : reflector.getProperties()) {
         if ("time".equals(property)) {
            continue;
         }
         Object oldValue = reflector.getValue(this, property);
         Object newValue = reflector.getValue(newCommand, property);

         if ( ! Objects.equals(oldValue, newValue)) {
            return false;
         }
      }

      return true;
   }
   public Object run(M1Editor editor) { 
      Pattern pattern = havePattern();

      if (pattern == null) {
         return null;
      }

      // have handle objects
      for (PatternObject patternObject : pattern.getObjects()) {
         String handleObjectId = (String) getHandleObjectAttributeValue(patternObject, "id");
         Class handleObjectClass = patternObject.getHandleObjectClass();
         Object handleObject = null;
         if (patternObject.getKind() == "core") {
            handleObject = editor.getOrCreate(handleObjectClass, handleObjectId);
         }
         else {
            handleObject = editor.getObjectFrame(handleObjectClass, handleObjectId);
         }
         patternObject.setHandleObject(handleObject);
      }


      for (PatternObject patternObject : pattern.getObjects()) {
         Reflector commandReflector = new Reflector().setClassName(this.getClass().getName());
         Reflector handleObjectReflector = new Reflector().setClassName(patternObject.getHandleObject().getClass().getName());

         for (PatternAttribute patternAttribute : patternObject.getAttributes()) {
            if ("id".equals(patternAttribute.getHandleAttrName())) {
               continue;
            }

            Object paramValue = commandReflector.getValue(this, patternAttribute.getCommandParamName());
            paramValue = paramValue.toString();
            handleObjectReflector.setValue(patternObject.getHandleObject(), patternAttribute.getHandleAttrName(), paramValue, null);
         }

         Object sourceHandleObject = patternObject.getHandleObject();
         for (PatternLink patternLink : patternObject.getLinks()) {
            String linkName = patternLink.getHandleLinkName();
            Object targetHandleObject = patternLink.getTarget().getHandleObject();
            handleObjectReflector.setValue(sourceHandleObject, linkName, targetHandleObject, null);
         }
      }
      return null;
   }

   public void undo(M1Editor editor) { 
      Pattern pattern = havePattern();

      if (pattern == null) {
         return;
      }

      for (PatternObject patternObject : pattern.getObjects()) {
         if (patternObject.getKind() == "core") {
            String handleObjectId = (String) getHandleObjectAttributeValue(patternObject, "id");
            Object oldObject = editor.removeModelObject(handleObjectId);
            Reflector handleObjectReflector = new Reflector().setClassName(oldObject.getClass().getName());
            handleObjectReflector.removeObject(oldObject);
         }
      }
   }

   public Pattern havePattern() { 
      return null;
   }

   public Object getHandleObjectAttributeValue(PatternObject patternObject, String handleAttributeName) { 
      Reflector reflector = new Reflector().setClassName(this.getClass().getName());
      for (PatternAttribute patternAttribute : patternObject.getAttributes()) {
         if (patternAttribute.getHandleAttrName().equals(handleAttributeName)) {
            String commandParamName = patternAttribute.getCommandParamName();
            Object value = reflector.getValue(this, commandParamName);
            return value;
         }
      }
      return null;
   }

}

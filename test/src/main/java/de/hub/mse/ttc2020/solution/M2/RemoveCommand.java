package de.hub.mse.ttc2020.solution.M2;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import java.lang.reflect.Method;

public class RemoveCommand extends ModelCommand  
{

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

   public Object run(M2Editor editor) { 
      java.util.Map<String,Object> mapOfModelObjects = editor.getMapOfModelObjects();
      java.util.Map<String,Object> mapOfFrames = editor.getMapOfFrames();

      Object oldObject = mapOfModelObjects.remove(getId());

      if (oldObject != null) {
         mapOfFrames.put(getId(), oldObject);
      }

      // call undo on old command
      ModelCommand oldCommand = editor.getActiveCommands().get(getId());
      oldCommand.undo(editor);

      return null;
   }

}

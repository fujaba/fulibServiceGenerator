package unikassel.bpmn2wf.WorkFlows;
import org.fulib.yaml.Reflector;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class ModelCommand  
{

   public static final String PROPERTY_id = "id";

   private String id;

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

   public static final String PROPERTY_time = "time";

   private String time;

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
      result.append(" ").append(this.getTime());


      return result.substring(1);
   }

   public Object run(WorkFlowsEditor editor) { 
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

   public void removeYou(WorkFlowsEditor editor)
   {
      RemoveCommand removeCommand = new RemoveCommand();
      removeCommand.setTargetClassName(this.getClass().getSimpleName())
            .setId(this.getId());
      String key = String.format("%s-%s", removeCommand.getTargetClassName(), this.getId());
      editor.getActiveCommands().remove(key);
      editor.getRemoveCommands().put(key, removeCommand);
      this.undo(editor);
   }

   public void undo(WorkFlowsEditor editor)
   {
      String key = String.format("%s-%s", this.getClass().getSimpleName(), this.getId());
      System.out.println("" + key +  "needs to overwrite undo");
   }
}

package unikassel.bpmn2wf.WorkFlows;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveAddStepCommand extends ModelCommand
{

   public static final String PROPERTY_taskText = "taskText";

   private String taskText;

   public String getTaskText()
   {
      return taskText;
   }

   public HaveAddStepCommand setTaskText(String value)
   {
      if (value == null ? this.taskText != null : ! value.equals(this.taskText))
      {
         String oldValue = this.taskText;
         this.taskText = value;
         firePropertyChange("taskText", oldValue, value);
      }
      return this;
   }

   public boolean preCheck(WorkFlowsEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("WorkFlowsAddStep-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("WorkFlowsAddStep-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("WorkFlowsAddStep-" + this.getId(), this);
      return true;
   }

   @Override
   public WorkFlowsAddStep run(WorkFlowsEditor editor) { 
      if ( ! preCheck(editor)) {
         return editor.getWorkFlowsAddSteps().get(this.getId());
      }
      WorkFlowsAddStep dataObject = editor.getOrCreateWorkFlowsAddStep(this.getId());
      dataObject.setTaskText(this.getTaskText());

      editor.fireCommandExecuted(this);
      return dataObject;
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

      result.append(" ").append(this.getTaskText());


      return result.substring(1);
   }

}
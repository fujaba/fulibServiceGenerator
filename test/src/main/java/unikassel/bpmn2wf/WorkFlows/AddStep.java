package unikassel.bpmn2wf.WorkFlows;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class AddStep extends ModelCommand  
{
   @Override
   public Object run(WorkFlowsEditor editor)
   {
      this.setId(taskId);
      if ( ! preCheck(editor)) {
         return editor.stepMap.get(taskId);
      }
      editor.init();
      System.out.println("Workflow add step " + taskId);
      Step step = editor.getOrCreateStep(taskId);
      step.setParent(editor.root);
      step.setText(taskText);
      step.setKind(taskKind);

      editor.fireCommandExecuted(this);

      return null;
   }

   public boolean preCheck(WorkFlowsEditor editor) {
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("AddStep-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("AddStep-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("AddStep-" + this.getId(), this);
      return true;
   }

   public static final String PROPERTY_taskText = "taskText";

   private String taskText;

   public String getTaskText()
   {
      return taskText;
   }

   public AddStep setTaskText(String value)
   {
      if (value == null ? this.taskText != null : ! value.equals(this.taskText))
      {
         String oldValue = this.taskText;
         this.taskText = value;
         firePropertyChange("taskText", oldValue, value);
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

      result.append(" ").append(this.getTaskId());
      result.append(" ").append(this.getTaskKind());
      result.append(" ").append(this.getTaskText());


      return result.substring(1);
   }

   public static final String PROPERTY_taskId = "taskId";

   private String taskId;

   public String getTaskId()
   {
      return taskId;
   }

   public AddStep setTaskId(String value)
   {
      if (value == null ? this.taskId != null : ! value.equals(this.taskId))
      {
         String oldValue = this.taskId;
         this.taskId = value;
         firePropertyChange("taskId", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_taskKind = "taskKind";

   private String taskKind;

   public String getTaskKind()
   {
      return taskKind;
   }

   public AddStep setTaskKind(String value)
   {
      if (value == null ? this.taskKind != null : ! value.equals(this.taskKind))
      {
         String oldValue = this.taskKind;
         this.taskKind = value;
         firePropertyChange("taskKind", oldValue, value);
      }
      return this;
   }

}

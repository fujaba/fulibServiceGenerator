package unikassel.bpmn2wf.WorkFlows;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class AddParallel extends ModelCommand  
{

   @Override
   public Object run(WorkFlowsEditor editor)
   {
      this.setId(gateId);
      if ( ! preCheck(editor)) {
         return null;
      }

      System.out.println("Workflow add parallel " + getId());
      Step pStep = editor.getOrCreateStep(gateId);
      pStep.setKind("parallelStep");

      editor.fireCommandExecuted(this);
      return null;
   }

   public static final String PROPERTY_gateId = "gateId";

   private String gateId;

   public String getGateId()
   {
      return gateId;
   }

   public AddParallel setGateId(String value)
   {
      if (value == null ? this.gateId != null : ! value.equals(this.gateId))
      {
         String oldValue = this.gateId;
         this.gateId = value;
         firePropertyChange("gateId", oldValue, value);
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

      result.append(" ").append(this.getGateId());
      result.append(" ").append(this.getGateKind());


      return result.substring(1);
   }

   public static final String PROPERTY_gateKind = "gateKind";

   private String gateKind;

   public String getGateKind()
   {
      return gateKind;
   }

   public AddParallel setGateKind(String value)
   {
      if (value == null ? this.gateKind != null : ! value.equals(this.gateKind))
      {
         String oldValue = this.gateKind;
         this.gateKind = value;
         firePropertyChange("gateKind", oldValue, value);
      }
      return this;
   }

   public boolean preCheck(WorkFlowsEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("AddParallel-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("AddParallel-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("AddParallel-" + this.getId(), this);
      return true;
   }

}

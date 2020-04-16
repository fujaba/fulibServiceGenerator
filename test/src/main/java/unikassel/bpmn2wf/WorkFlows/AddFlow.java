package unikassel.bpmn2wf.WorkFlows;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Collection;

public class AddFlow extends ModelCommand  
{
   @Override
   public Object run(WorkFlowsEditor editor)
   {
      editor.init();

      String flowId = source + "_" + target;
      this.setId(flowId);
      if ( ! preCheck(editor)) {
         return null;
      }

      // just add the flow
      System.out.println("Workflow add flow " + source + " -> " + target);

      if ("start".equals(source)) {
         Step targetStep = editor.getOrCreateStep(target);
         targetStep.setParent(editor.root);
      }
      else if ("end".equals(target)) {
         Step sourceStep = editor.getOrCreateStep(source);
         sourceStep.setFinalFlow(sourceStep.getParent());
      }
      else {
         String pSource = null;
         String pTarget = null;
         if (source.endsWith("_dot_start")) {
            pSource = source;
            source = source.substring(0, source.length() - "_dot_start".length());
         }
         if (source.endsWith("_dot_end")) {
            source = source.substring(0, source.length() - "_dot_end".length());
         }
         if (target.endsWith("_dot_start")) {
            target = target.substring(0, target.length() - "_dot_start".length());
         }
         if (target.endsWith("_dot_end")) {
            pTarget = target;
            target = target.substring(0, target.length() - "_dot_end".length());
         }
         Step sourceStep = editor.getOrCreateStep(source);
         Step targetStep = editor.getOrCreateStep(target);
         if ( pSource == null && pTarget == null) {
            sourceStep.withNext(targetStep);
            targetStep.setParent(sourceStep.getParent());
         }
         else if ("parallelStep".equals(sourceStep.getKind())) {
            Flow subFlow = new Flow();
            sourceStep.withInvokedFlows(subFlow);
            subFlow.withSteps(targetStep);
         }
         else if ("parallelStep".equals(targetStep.getKind())) {
            sourceStep.setFinalFlow(sourceStep.getParent());
         }
      }

      editor.fireCommandExecuted(this);
      return null;
   }
   public static final String PROPERTY_source = "source";

   private String source;

   public String getSource()
   {
      return source;
   }

   public AddFlow setSource(String value)
   {
      if (value == null ? this.source != null : ! value.equals(this.source))
      {
         String oldValue = this.source;
         this.source = value;
         firePropertyChange("source", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_target = "target";

   private String target;

   public String getTarget()
   {
      return target;
   }

   public AddFlow setTarget(String value)
   {
      if (value == null ? this.target != null : ! value.equals(this.target))
      {
         String oldValue = this.target;
         this.target = value;
         firePropertyChange("target", oldValue, value);
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

      result.append(" ").append(this.getSource());
      result.append(" ").append(this.getTarget());


      return result.substring(1);
   }

   public boolean preCheck(WorkFlowsEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("AddFlow-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("AddFlow-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("AddFlow-" + this.getId(), this);
      return true;
   }

}

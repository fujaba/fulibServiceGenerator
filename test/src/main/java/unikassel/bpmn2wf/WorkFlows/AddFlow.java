package unikassel.bpmn2wf.WorkFlows;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

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
         propagateFlow(targetStep);
      }
      else if ("end".equals(target)) {
         Step sourceStep = editor.getOrCreateStep(source);
         sourceStep.setFinalFlag(true);
         propagateFlow(sourceStep);
      }
      else {
         Step sourceStep = editor.getOrCreateStep(truncate(source));
         Step targetStep = editor.getOrCreateStep(truncate(target));
         if ( ! source.endsWith("_dot_start") && ! target.endsWith("_dot_end")) {
            sourceStep.withNext(targetStep);
            propagateFlow(sourceStep);
         }
         else if ("parallelStep".equals(sourceStep.getKind())) {
            Flow subFlow = new Flow().setKind("parallel");
            sourceStep.withInvokedFlows(subFlow);
            subFlow.withSteps(targetStep);
            propagateFlow(targetStep);
         }
         else if ("parallelStep".equals(targetStep.getKind())) {
            sourceStep.setFinalFlag(true);
            propagateFlow(sourceStep);
         }
      }

      editor.fireCommandExecuted(this);
      return null;
   }

   public String truncate(String old) {
      int pos = old.indexOf("_dot_");
      if (pos < 0) {
         return old;
      }

      return old.substring(0, pos);
   }

   public void propagateFlow(Step currentStep)
   {
      if (currentStep.getFinalFlag()) {
         currentStep.setFinalFlow(currentStep.getParent());
         return;
      }

      for (Step nextStep : currentStep.getNext()) {
         nextStep.setParent(currentStep.getParent());
         propagateFlow(nextStep);
      }
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

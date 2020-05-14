package unikassel.bpmn2wf.WorkFlows;
import javax.swing.*;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class WorkFlowsApp  
{

   public static final String PROPERTY_modelEditor = "modelEditor";

   private WorkFlowsEditor modelEditor;
   public StringBuilder buf;

   public WorkFlowsEditor getModelEditor()
   {
      return modelEditor;
   }

   public WorkFlowsApp setModelEditor(WorkFlowsEditor value)
   {
      if (value != this.modelEditor)
      {
         WorkFlowsEditor oldValue = this.modelEditor;
         this.modelEditor = value;
         firePropertyChange("modelEditor", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public WorkFlowsApp setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_description = "description";

   private String description;

   public String getDescription()
   {
      return description;
   }

   public WorkFlowsApp setDescription(String value)
   {
      if (value == null ? this.description != null : ! value.equals(this.description))
      {
         String oldValue = this.description;
         this.description = value;
         firePropertyChange("description", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_content = "content";

   private Page content = null;

   public Page getContent()
   {
      return this.content;
   }

   public WorkFlowsApp setContent(Page value)
   {
      if (this.content != value)
      {
         Page oldValue = this.content;
         if (this.content != null)
         {
            this.content = null;
            oldValue.setApp(null);
         }
         this.content = value;
         if (value != null)
         {
            value.setApp(this);
         }
         firePropertyChange("content", oldValue, value);
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
      result.append(" ").append(this.getDescription());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setContent(null);

   }

   public WorkFlowsApp init(WorkFlowsEditor editor) // no fulib
   {
      editor.init();
      this.modelEditor = editor;
      this.setId("root");
      this.setDescription("WorkFlows App");
      diagram();
      return this;
   }

   private String toolBar = "button addStep | button addGatePair| button addFlow | button edit";

   public Page diagram()
   {
      Page page = new Page().setId("workflowDiagram").setApp(this);

      ArrayList<Flow> allFlows = new ArrayList<>();
      allFlows.add(modelEditor.root);

      buf = new StringBuilder();

      while (allFlows.size() > 0) {
         Flow currentFlow = allFlows.remove(0);
         String flowName = "";
         if (currentFlow.getInvoker() != null) {
            flowName = " " + currentFlow.getInvoker().getId();
            Step firstStep = getFirstStep(currentFlow);
            flowName += " " + firstStep.getId();
         }
         String text = "" + currentFlow.getKind() + " flow" + flowName;
         buf.append(text).append("\n");

         for (Step step : currentFlow.getSteps()) {
            String nextId = "";
            if (step.getNext().size() > 0) {
               nextId = "next " + step.getNext().iterator().next().getId();
            }
            String description = String.format("&nbsp;&nbsp;&nbsp;&nbsp; step %s \"%s\" %s", step.getId(), step.getText(), nextId);
            text = String.format("    step %s \"%s\" %s", step.getId(), step.getText(), nextId);

            if ("parallelStep".equals(step.getKind())) {
               allFlows.addAll(step.getInvokedFlows());
               String subFlowIds = "";
               ArrayList<String> subIdList = new ArrayList<>();
               for (Flow subFlow : step.getInvokedFlows()) {
                  Step firstStep = getFirstStep(subFlow);
                  subIdList.add(firstStep.getId());
               }
               subFlowIds = String.join(", ", subIdList.toArray(new String[0]));
               description = String.format("&nbsp;&nbsp;&nbsp;&nbsp; parallel step %s subflows %s %s", step.getId(), subFlowIds, nextId);
               text = String.format("    parallel step %s subflows %s %s", step.getId(), subFlowIds, nextId);
            }

            buf.append(text).append("\n");
         }
         buf.append("end flow\n");
      }

      new Line().setId("text").setPage(page).setDescription("<pre>\n" + buf.toString() + "</pre>\n");

      new Line().setId("toolBar").setPage(page)
            .setDescription(toolBar);

      return page;
   }

   private Step getFirstStep(Flow root)
   {
      Step firstStep = null;
      for (Step step : root.getSteps()) {
         if (step.getPrev().size() == 0) {
            firstStep = step;
            break;
         }
      }
      return firstStep;
   }

}

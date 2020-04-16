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

   private String toolBar = "button addStep | button addFlow";

   public Page diagram()
   {
      Page page = new Page().setId("workflowDiagram").setApp(this)
            .setDescription(toolBar);

      Step firstStep = getFirstStep(modelEditor.root);

      if (firstStep == null) {
         new Line().setId("l1").setPage(page).setDescription("empty diagram");
         return page;
      }

      LinkedHashSet<Step> prevSteps = new LinkedHashSet<>();
      prevSteps.add(firstStep);
      while (prevSteps.size() > 0) {
         ArrayList<String> stepNames = new ArrayList<>();
         for (Step prevStep : prevSteps) {
            stepNames.add(prevStep.getId());
         }

         String join = String.join("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", stepNames.toArray(new String[0]));
         new Line().setId(modelEditor.getTime()).setPage(page).setDescription(join);

         LinkedHashSet<Step> nextSteps = new LinkedHashSet<>();
         for (Step prevStep : prevSteps) {
            ArrayList<Step> nextOfThis = prevStep.getNext();
            for (Step nextStep : nextOfThis) {
               if ("parallelStep".equals(nextStep.getKind())) {
                  for (Flow invokedFlow : nextStep.getInvokedFlows()) {
                     Step subStep = getFirstStep(invokedFlow);
                     nextSteps.add(subStep);
                  }
               }
               else {
                  nextSteps.add(nextStep);
               }
            }
         }

         if (nextSteps.size() > 0) {
            // add arrows
            if (prevSteps.size() == 1 && nextSteps.size() == 1) {
               new Line().setId(modelEditor.getTime()).setPage(page).setDescription("<i class=\"fa fa-long-arrow-down\"></i>");
            }
            else if (prevSteps.size() == 1 && nextSteps.size() > 1) {
               new Line().setId(modelEditor.getTime()).setPage(page)
                     .setDescription("<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(45deg);\"></i>" +
                           "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                           "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(-45deg);\"></i>");
            }
            else if (prevSteps.size() > 1 && nextSteps.size() == 1) {
               new Line().setId(modelEditor.getTime()).setPage(page)
                     .setDescription("<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(-45deg);\"></i>" +
                           "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                           "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(45deg);\"></i>");
            }
            else if (prevSteps.size() > 1 && nextSteps.size() > 1) {
               new Line().setId(modelEditor.getTime()).setPage(page)
                     .setDescription("<i class=\"fa fa-long-arrow-down\"></i>" +
                           "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                           "<i class=\"fa fa-long-arrow-down\"></i>");
            }
         }

         prevSteps = nextSteps;

      }

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

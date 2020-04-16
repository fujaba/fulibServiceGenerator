package unikassel.bpmn2wf.BPMN;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class BPMNApp  
{

   public static final String PROPERTY_modelEditor = "modelEditor";

   private BPMNEditor modelEditor;

   public BPMNEditor getModelEditor()
   {
      return modelEditor;
   }

   public BPMNApp setModelEditor(BPMNEditor value)
   {
      if (value != this.modelEditor)
      {
         BPMNEditor oldValue = this.modelEditor;
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

   public BPMNApp setId(String value)
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

   public BPMNApp setDescription(String value)
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

   public BPMNApp setContent(Page value)
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

   public BPMNApp init(BPMNEditor editor) // no fulib
   {
      editor.init();
      this.modelEditor = editor;
      this.setId("root");
      this.setDescription("BPMN App");
      diagram();
      return this;
   }

   private String toolBar = "button addStep | button addGatePair| button addFlow";

   public Page diagram()
   {
      Page page = new Page().setId("bpmnDiagram").setApp(this)
            .setDescription(toolBar);
      new Line().setId("startLine").setPage(page).setDescription("<i class=\"fa fa-circle-o\"></i>");

      Task start = modelEditor.taskMap.get("start");
      LinkedHashSet<Task> preTasks = new LinkedHashSet<>();
      LinkedHashSet<Flow> flows = new LinkedHashSet<>();
      LinkedHashSet<Task> nextTasks = new LinkedHashSet<>();
      preTasks.add(start);

      while ( ! preTasks.isEmpty()) {
         for (Task preTask : preTasks) {
            flows.addAll(preTask.getOutgoing());
         }
         for (Flow flow : flows) {
            nextTasks.add(flow.getTarget());
         }
         if (preTasks.size() == 1 && nextTasks.size() == 1) {
            new Line().setId(modelEditor.getTime()).setPage(page).setDescription("<i class=\"fa fa-long-arrow-down\"></i>");
         }
         else if (preTasks.size() == 1 && nextTasks.size() > 1) {
            new Line().setId(modelEditor.getTime()).setPage(page)
                  .setDescription("<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(45deg);\"></i>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(-45deg);\"></i>");
         }
         else if (preTasks.size() > 1 && nextTasks.size() > 1) {
            new Line().setId(modelEditor.getTime()).setPage(page)
                  .setDescription("<i class=\"fa fa-long-arrow-down\"></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</i><i class=\"fa fa-long-arrow-down\"></i>");
         }
         else if (preTasks.size() > 1 && nextTasks.size() == 1) {
            new Line().setId(modelEditor.getTime()).setPage(page)
                  .setDescription("<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(-45deg);\"></i>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(45deg);\"></i>");

         }
         else if (nextTasks.size() == 0) {
            new Line().setId(modelEditor.getTime()).setPage(page).setDescription("&nbsp;&nbsp;&nbsp;&nbsp;");
         }

         ArrayList<String> taskNames = new ArrayList<>();
         for (Task nextTask : nextTasks) {
            if ("gate".equals(nextTask.getKind())) {
               taskNames.add("<i class=\"fa fa-square-o\" style=\"transform: rotate(45deg);\"></i>");
            }
            else if ("end".equals(nextTask.getKind())) {
               taskNames.add("<i class=\"fa fa-dot-circle-o\"></i>");
            }
            else {
               taskNames.add(nextTask.getId());
            }
         }

         String join = String.join("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", taskNames.toArray(new String[0]));
         new Line().setId(modelEditor.getTime()).setPage(page).setDescription(join);

         preTasks = nextTasks;
         nextTasks = new LinkedHashSet<>();
         flows.clear();

      }

      return page;
   }

   public void addStep()
   {
      Page page = diagram();

      new Line().setId("taskIdIn").setPage(page).setDescription("input step id?");
      new Line().setId("taskTextIn").setPage(page).setDescription("input step text?");
      new Line().setId("addButton").setPage(page).setDescription("button add")
         .setAction("AddStep taskIdIn taskTextIn diagram");

   }

   public void addGatePair() {
      Page page = diagram();
      new Line().setId("gateIdIn").setPage(page).setDescription("input gate id?");
      new Line().setId("addButton").setPage(page).setDescription("button add")
            .setAction("AddParallel gateIdIn diagram");

   }

   public void addFlow() {
      Page page = diagram();
      new Line().setId("sourceIn").setPage(page).setDescription("input source step id?");
      new Line().setId("targetIn").setPage(page).setDescription("input target step id?");
      new Line().setId("addButton").setPage(page).setDescription("button add")
            .setAction("AddFlow sourceIn targetIn diagram");

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

}

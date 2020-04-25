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
      Page page = new Page().setId("bpmnDiagram").setApp(this);

      ArrayList<Task> allTasks = new ArrayList<>();
      Task start = modelEditor.taskMap.get("start");
      allTasks.add(start);

      for (Task step : modelEditor.taskMap.values()) {
         if ( ! allTasks.contains(step)) {
            allTasks.add(step);
         }
      }

      // move end task to the end of the list
      Task end = modelEditor.taskMap.get("end");
      allTasks.remove(end);
      allTasks.add(end);

      LinkedHashSet<Task> preTasks = new LinkedHashSet<>();
      LinkedHashSet<Flow> flows = new LinkedHashSet<>();
      LinkedHashSet<Task> nextTasks = new LinkedHashSet<>();

      while (allTasks.size() > 0 || preTasks.size() > 0) {
         if (preTasks.isEmpty()) {
            Task nextPreTask = allTasks.remove(0);
            preTasks.add(nextPreTask);
         }

         // draw preTasks
         ArrayList<String> taskNames = new ArrayList<>();
         for (Task preTask : preTasks) {
            if ("gate_parallel".equals(preTask.getKind())) {
               taskNames.add("<i class=\"fa fa-times-rectangle-o\" style=\"transform: rotate(45deg);\"></i>");
            }
            else if ("gate_alternative".equals(preTask.getKind())) {
               taskNames.add("<i class=\"fa fa-plus-square-o\" style=\"transform: rotate(45deg);\"></i>");
            }
            else if ("start".equals(preTask.getKind())) {
               taskNames.add("<i class=\"fa fa-circle-o\"></i>");
            }
            else if ("end".equals(preTask.getKind())) {
               taskNames.add("<i class=\"fa fa-dot-circle-o\"></i>");
            }
            else {
               taskNames.add(preTask.getId());
            }
         }

         String join = String.join("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", taskNames.toArray(new String[0]));
         new Line().setId(modelEditor.getTime()).setPage(page).setDescription(join);

         // compute next tasks
         for (Task preTask : preTasks) {
            flows.addAll(preTask.getOutgoing());
         }
         for (Flow flow : flows) {
            Task flowTarget = flow.getTarget();
            nextTasks.add(flowTarget);
            allTasks.remove(flowTarget);
         }

         // compute arrows
         if (nextTasks.size() == 0) {
            new Line().setId(modelEditor.getTime()).setPage(page).setDescription("&nbsp;&nbsp;&nbsp;&nbsp;");
         }
         else {
            ArrayList<String> arrowList = new ArrayList<>();
            int numberOfArrows = Math.max(preTasks.size(), nextTasks.size());
            for (int i = 0; i < numberOfArrows; i++) {
               String arrow = "<i class=\"fa fa-long-arrow-down\"></i>";
               if (preTasks.size() > nextTasks.size() && i == 0) {
                  arrow = "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(-45deg);\"></i>";
               }
               else if (preTasks.size() > nextTasks.size() && i == numberOfArrows -1) {
                  arrow = "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(45deg);\"></i>";
               }
               else if (preTasks.size() < nextTasks.size() && i == 0) {
                  arrow = "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(45deg);\"></i>";
               }
               else if (preTasks.size() < nextTasks.size() && i == numberOfArrows -1) {
                  arrow = "<i class=\"fa fa-long-arrow-down\" style=\"transform: rotate(-45deg);\"></i>";
               }
               arrowList.add(arrow);
            }
            String arrowJoin = String.join("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", arrowList.toArray(new String[0]));
            new Line().setId(modelEditor.getTime()).setPage(page).setDescription(arrowJoin);
         }

         // forward nextTasks
         preTasks = nextTasks;
         nextTasks = new LinkedHashSet<>();
         flows.clear();
      }

      new Line().setId("toolbar").setPage(page).setDescription(toolBar);

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
      new Line().setId("gateKindIn").setPage(page).setDescription("input gate kind?");
      new Line().setId("addButton").setPage(page).setDescription("button add")
            .setAction("AddParallel gateIdIn gateKindIn diagram");

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

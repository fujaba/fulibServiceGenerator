package unikassel.bpmn2wf.WorkFlows;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fulib.yaml.Yaml;

public class WorkFlowsEditor  
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public WorkFlowsEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
   {
      if (value != this.activeCommands)
      {
         java.util.Map<String, ModelCommand> oldValue = this.activeCommands;
         this.activeCommands = value;
         firePropertyChange("activeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_removeCommands = "removeCommands";

   private java.util.Map<String, RemoveCommand> removeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, RemoveCommand> getRemoveCommands()
   {
      return removeCommands;
   }

   public WorkFlowsEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
   {
      if (value != this.removeCommands)
      {
         java.util.Map<String, RemoveCommand> oldValue = this.removeCommands;
         this.removeCommands = value;
         firePropertyChange("removeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_commandListeners = "commandListeners";

   private java.util.Map<String, ArrayList<CommandStream>> commandListeners = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ArrayList<CommandStream>> getCommandListeners()
   {
      return commandListeners;
   }

   public WorkFlowsEditor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
   {
      if (value != this.commandListeners)
      {
         java.util.Map<String, ArrayList<CommandStream>> oldValue = this.commandListeners;
         this.commandListeners = value;
         firePropertyChange("commandListeners", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_isoDateFormat = "isoDateFormat";

   private DateFormat isoDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

   public DateFormat getIsoDateFormat()
   {
      return isoDateFormat;
   }

   public WorkFlowsEditor setIsoDateFormat(DateFormat value)
   {
      if (value != this.isoDateFormat)
      {
         DateFormat oldValue = this.isoDateFormat;
         this.isoDateFormat = value;
         firePropertyChange("isoDateFormat", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_lastTime = "lastTime";

   private String lastTime = isoDateFormat.format(new Date());

   public String getLastTime()
   {
      return lastTime;
   }

   public WorkFlowsEditor setLastTime(String value)
   {
      if (value == null ? this.lastTime != null : ! value.equals(this.lastTime))
      {
         String oldValue = this.lastTime;
         this.lastTime = value;
         firePropertyChange("lastTime", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_timeDelta = "timeDelta";

   private long timeDelta = 1;

   public long getTimeDelta()
   {
      return timeDelta;
   }

   public WorkFlowsEditor setTimeDelta(long value)
   {
      if (value != this.timeDelta)
      {
         long oldValue = this.timeDelta;
         this.timeDelta = value;
         firePropertyChange("timeDelta", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_service = "service";

   private WorkFlowsService service = null;

   public WorkFlowsService getService()
   {
      return this.service;
   }

   public WorkFlowsEditor setService(WorkFlowsService value)
   {
      if (this.service != value)
      {
         WorkFlowsService oldValue = this.service;
         if (this.service != null)
         {
            this.service = null;
            oldValue.setModelEditor(null);
         }
         this.service = value;
         if (value != null)
         {
            value.setModelEditor(this);
         }
         firePropertyChange("service", oldValue, value);
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

   public void removeYou()
   {
      this.setService(null);

   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getLastTime());


      return result.substring(1);
   }

   public Flow root = null;
   public LinkedHashMap<String, Step> stepMap = new LinkedHashMap<>();
   public int stepCounter = 0;

   public WorkFlowsEditor init()
   {
      if (root == null) {
         root = new Flow().setKind("basic");
      }

      return this;
   }

   public Step getOrCreateStep(String id) {
      Step step = stepMap.computeIfAbsent(id, k -> new Step().setId(k));
      return step;
   }

   public String getTime() { 
      String newTime = isoDateFormat.format(new Date());
      if (newTime.compareTo(lastTime) <= 0) {
         try {
            Date lastDate = isoDateFormat.parse(lastTime);
            long millis = lastDate.getTime();
            millis += timeDelta;
            Date newDate = new Date(millis);
            newTime = isoDateFormat.format(newDate);
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      lastTime = newTime;
      return newTime;
   }

   public void fireCommandExecuted(ModelCommand command) { 
      String commandName = command.getClass().getSimpleName();
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      for (CommandStream stream : listeners) {
         stream.publish(command);
      }
   }

   public WorkFlowsEditor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void storeYamlCommands(String fileName) {
      String yaml = Yaml.encode(this.activeCommands.values());
      try {
         Files.write(Paths.get(fileName), yaml.getBytes(StandardCharsets.UTF_8));
      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.bpmn2wf.WorkFlows").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public Map<String, ModelCommand> parseModelToCommandSet(Flow basicFlow)
   {
      Map<String, ModelCommand> newCommands = new LinkedHashMap<>();

      addStepsOfFlow(newCommands, basicFlow, "start", "end");

      return newCommands;
   }

   private void addStepsOfFlow(Map<String, ModelCommand> newCommands, Flow flow, String previousStep, String successorStep)
   {
      for (Step step : flow.getSteps()) {
         if ("parallelStep".equals(step.getKind())) {
            AddParallel addParallel = new AddParallel().setGateId(step.getId()).setGateKind("parallel");
            addParallel.setId(addParallel.getGateId());
            String key = "AddParallel-" + addParallel.getId();
            newCommands.put(key, addParallel);
            addFlowCommand(newCommands, previousStep, step.getId() + "_dot_start");
            previousStep = step.getId() + "_dot_end";

            if (step.getFinalFlag()) {
               addFlowCommand(newCommands, step.getId() + "_dot_end", successorStep);
            }

            for (Flow invokedFlow : step.getInvokedFlows()) {
               addStepsOfFlow(newCommands, invokedFlow, step.getId() + "_dot_start", step.getId() + "_dot_end");
            }
         }
         else { // standard step
            AddStep addStep = new AddStep().setTaskId(step.getId()).setTaskText(step.getText());
            addStep.setId(step.getId());
            String key = String.format("AddStep-%s", step.getId());
            newCommands.put(key, addStep);
            addFlowCommand(newCommands, previousStep, step.getId());
            previousStep = step.getId();

            if (step.getFinalFlag()) {
               addFlowCommand(newCommands, step.getId(), successorStep);
            }
         }
      }
   }

   private void addFlowCommand(Map<String, ModelCommand> newCommands, String previousStep, String id)
   {
      String key;
      AddFlow inFlow = new AddFlow().setSource(previousStep).setTarget(id);
      inFlow.setId(inFlow.getSource() + "_" + inFlow.getTarget());
      key = "AddFlow-" + inFlow.getId();
      newCommands.put(key, inFlow);
   }

   public void mergeIntoHistory(Map<String, ModelCommand> newHistory) {
      // remove old commands that are no longer there
      LinkedHashMap<String, ModelCommand> oldHistory = new LinkedHashMap<>();
      oldHistory.putAll(activeCommands);

      for (Map.Entry<String, ModelCommand> entry : oldHistory.entrySet()) {
         String key = entry.getKey();
         ModelCommand oldCommand = entry.getValue();
         ModelCommand newCommand = newHistory.get(key);
         if (newCommand == null) {
            System.out.println("Remove " + key);
            oldCommand.removeYou(this);
         }
      }

      // compare new commands and apply if actually new
      for (Map.Entry<String, ModelCommand> entry : newHistory.entrySet()) {
         String key = entry.getKey();
         ModelCommand newCommand = entry.getValue();
         ModelCommand oldCommand = activeCommands.get(key);

         if (oldCommand == null) {
            newCommand.run(this);
            continue;
         }

         if ( ! oldCommand.equalsButTime(newCommand)) {
            newCommand.run(this);
         }
      }
   }
}

package unikassel.bpmn2wf.BPMN;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.ArrayList;
import org.fulib.yaml.Yaml;

public class BPMNEditor 
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public BPMNEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public BPMNEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
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

   public BPMNEditor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
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

   public BPMNEditor setIsoDateFormat(DateFormat value)
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

   public BPMNEditor setLastTime(String value)
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

   public BPMNEditor setTimeDelta(long value)
   {
      if (value != this.timeDelta)
      {
         long oldValue = this.timeDelta;
         this.timeDelta = value;
         firePropertyChange("timeDelta", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_bPMNAddSteps = "bPMNAddSteps";

   private java.util.Map<String, BPMNAddStep> bPMNAddSteps = new java.util.LinkedHashMap<>();

   public java.util.Map<String, BPMNAddStep> getBPMNAddSteps()
   {
      return bPMNAddSteps;
   }

   public BPMNEditor setBPMNAddSteps(java.util.Map<String, BPMNAddStep> value)
   {
      if (value != this.bPMNAddSteps)
      {
         java.util.Map<String, BPMNAddStep> oldValue = this.bPMNAddSteps;
         this.bPMNAddSteps = value;
         firePropertyChange("bPMNAddSteps", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_bPMNAddFlows = "bPMNAddFlows";

   private java.util.Map<String, BPMNAddFlow> bPMNAddFlows = new java.util.LinkedHashMap<>();

   public java.util.Map<String, BPMNAddFlow> getBPMNAddFlows()
   {
      return bPMNAddFlows;
   }

   public BPMNEditor setBPMNAddFlows(java.util.Map<String, BPMNAddFlow> value)
   {
      if (value != this.bPMNAddFlows)
      {
         java.util.Map<String, BPMNAddFlow> oldValue = this.bPMNAddFlows;
         this.bPMNAddFlows = value;
         firePropertyChange("bPMNAddFlows", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_service = "service";

   private BPMNService service = null;

   public BPMNService getService()
   {
      return this.service;
   }

   public BPMNEditor setService(BPMNService value)
   {
      if (this.service != value)
      {
         BPMNService oldValue = this.service;
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

   public BPMNEditor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.bpmn2wf.BPMN").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public BPMNAddStep getOrCreateBPMNAddStep(String id) { 
      if (id == null) {
         return null;
      }
      BPMNAddStep oldObject = this.getBPMNAddSteps().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      BPMNAddStep newObject = new BPMNAddStep();
      newObject.setId(id);
      this.getBPMNAddSteps().put(id, newObject);
      return newObject;
   }

   public BPMNAddFlow getOrCreateBPMNAddFlow(String id) { 
      if (id == null) {
         return null;
      }
      BPMNAddFlow oldObject = this.getBPMNAddFlows().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      BPMNAddFlow newObject = new BPMNAddFlow();
      newObject.setId(id);
      this.getBPMNAddFlows().put(id, newObject);
      return newObject;
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

      result.append(" ").append(this.getLastTime());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setService(null);

   }

}
package de.hub.mse.ttc2020.solution.M2;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.ArrayList;
import org.fulib.yaml.Yaml;

public class M2Editor  
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public M2Editor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public M2Editor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
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

   public M2Editor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
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

   public M2Editor setIsoDateFormat(DateFormat value)
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

   public M2Editor setLastTime(String value)
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

   public M2Editor setTimeDelta(long value)
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

   private M2Service service = null;

   public M2Service getService()
   {
      return this.service;
   }

   public M2Editor setService(M2Service value)
   {
      if (this.service != value)
      {
         M2Service oldValue = this.service;
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

   public M2Editor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("de.hub.mse.ttc2020.solution.M2").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

}
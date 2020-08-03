package de.hub.mse.ttc2020.solution.M1;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.Yaml;
import java.util.Date;
import java.util.ArrayList;

public class M1Editor  
{
   public static final String PROPERTY_service = "service";

   private M1Service service = null;

   protected PropertyChangeSupport listeners = null;

   public static final String PROPERTY_isoDateFormat = "isoDateFormat";

   private DateFormat isoDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

   public static final String PROPERTY_lastTime = "lastTime";

   private String lastTime = isoDateFormat.format(new Date());

   public static final String PROPERTY_timeDelta = "timeDelta";

   private long timeDelta = 1;

   public DateFormat getIsoDateFormat()
   {
      return isoDateFormat;
   }

   public M1Editor setIsoDateFormat(DateFormat value)
   {
      if (value != this.isoDateFormat)
      {
         DateFormat oldValue = this.isoDateFormat;
         this.isoDateFormat = value;
         firePropertyChange("isoDateFormat", oldValue, value);
      }
      return this;
   }

public String getLastTime()
   {
      return lastTime;
   }

public M1Editor setLastTime(String value)
   {
      if (value == null ? this.lastTime != null : ! value.equals(this.lastTime))
      {
         String oldValue = this.lastTime;
         this.lastTime = value;
         firePropertyChange("lastTime", oldValue, value);
      }
      return this;
   }

public long getTimeDelta()
   {
      return timeDelta;
   }

public M1Editor setTimeDelta(long value)
   {
      if (value != this.timeDelta)
      {
         long oldValue = this.timeDelta;
         this.timeDelta = value;
         firePropertyChange("timeDelta", oldValue, value);
      }
      return this;
   }

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
   }public M1Service getService()
   {
      return this.service;
   }

public M1Editor setService(M1Service value)
   {
      if (this.service != value)
      {
         M1Service oldValue = this.service;
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

public void removeYou()
   {
      this.setService(null);

   }

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public M1Editor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public M1Editor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
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

   public M1Editor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
   {
      if (value != this.commandListeners)
      {
         java.util.Map<String, ArrayList<CommandStream>> oldValue = this.commandListeners;
         this.commandListeners = value;
         firePropertyChange("commandListeners", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_mapOfFrames = "mapOfFrames";

   private java.util.Map<String, Object> mapOfFrames = new java.util.LinkedHashMap<>();

   public java.util.Map<String, Object> getMapOfFrames()
   {
      return mapOfFrames;
   }

   public M1Editor setMapOfFrames(java.util.Map<String, Object> value)
   {
      if (value != this.mapOfFrames)
      {
         java.util.Map<String, Object> oldValue = this.mapOfFrames;
         this.mapOfFrames = value;
         firePropertyChange("mapOfFrames", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_mapOfModelObjects = "mapOfModelObjects";

   private java.util.Map<String, Object> mapOfModelObjects = new java.util.LinkedHashMap<>();

   public java.util.Map<String, Object> getMapOfModelObjects()
   {
      return mapOfModelObjects;
   }

   public M1Editor setMapOfModelObjects(java.util.Map<String, Object> value)
   {
      if (value != this.mapOfModelObjects)
      {
         java.util.Map<String, Object> oldValue = this.mapOfModelObjects;
         this.mapOfModelObjects = value;
         firePropertyChange("mapOfModelObjects", oldValue, value);
      }
      return this;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getLastTime());


      return result.substring(1);
   }

   public void parse(Object... startObjects)
   {
      LinkedHashSet allObjects = new Yaml(startObjects[0].getClass().getPackage().getName()).collectObjects(startObjects);

      // add parsed objects to model
      for (Object parsedObject : allObjects) {
         Reflector reflector = new Reflector().setClazz(parsedObject.getClass());
         String id = (String) reflector.getValue(parsedObject, "id");
         if (id != null) {
            mapOfModelObjects.put(id, parsedObject);
            mapOfFrames.remove(id);
         }
      }

      ArrayList<ModelCommand> allCommands = new ArrayList<>();
      for (Object currentObject : allObjects) {
         findCommands(allCommands, currentObject);
      }

      // remove obsolete commands from history
      for (Map.Entry<String, ModelCommand> entry : activeCommands.entrySet()) {
         String id = entry.getKey();
         ModelCommand oldCommand = entry.getValue();
         if (oldCommand instanceof RemoveCommand) {
            continue;
         }

         ModelCommand parsedCommand = getFromAllCommands(allCommands, id);
         if (parsedCommand == null) {
            ModelCommand removeCommand = new RemoveCommand().setId(id);
            execute(removeCommand);
         }
      }

      // add parsed commands, if new
      for (ModelCommand parsedCommand : allCommands) {
         String id = parsedCommand.getId();
         ModelCommand oldCommand = activeCommands.get(id);
         if (oldCommand == null || ! oldCommand.equalsButTime(parsedCommand)) {
            execute(parsedCommand);
         }
      }
   }

   private ModelCommand getFromAllCommands(ArrayList<ModelCommand> allCommands, String id)
   {
      for (ModelCommand command : allCommands) {
         if (command.getId().equals(id)) {
            return command;
         }
      }
      return null;
   }

   private ModelCommand findCommands(ArrayList<ModelCommand> allCommands, Object currentObject)
   {
      ArrayList<ModelCommand> prototypes = haveCommandPrototypes();
      for (ModelCommand prototype : prototypes) {
         ModelCommand currentCommand = prototype.parse(currentObject);
         if (currentCommand != null) {
            allCommands.add(currentCommand);
         }
      }

      return null;
   }

   private ArrayList<ModelCommand> commandPrototypes = null;

   private ArrayList<ModelCommand> haveCommandPrototypes()
   {
      if (commandPrototypes == null) {
         commandPrototypes = new ArrayList<>();
         commandPrototypes.add(new HavePerson());
         commandPrototypes.add(new HaveDog());
      }

      return commandPrototypes;
   }
   public Object getOrCreate(Class clazz, String id) { 
      Object modelObject = mapOfModelObjects.get(id);
      if (modelObject != null) {
         return modelObject;
      }

      modelObject = getObjectFrame(clazz, id);

      mapOfFrames.remove(id);
      mapOfModelObjects.put(id, modelObject);

      return modelObject;
   }

   public Object getObjectFrame(Class clazz, String id) { 
      try {
         Object modelObject = mapOfModelObjects.get(id);
         if (modelObject != null) {
            return modelObject;
         }

         modelObject = mapOfFrames.get(id);
         if (modelObject != null) {
            return modelObject;
         }

         modelObject = (Object) clazz.getConstructor().newInstance();
         Method setIdMethod = clazz.getMethod("setId", String.class);
         setIdMethod.invoke(modelObject, id);
         mapOfFrames.put(id, modelObject);

         return modelObject;
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public Object getModelObject(String id) { 
   return mapOfModelObjects.get(id);
   }

   public Object removeModelObject(String id) { 
      Object oldObject = mapOfModelObjects.remove(id);

      if (oldObject != null) {
         mapOfFrames.put(id, oldObject);
      }

      return mapOfFrames.get(id);
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

   public M1Editor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("de.hub.mse.ttc2020.solution.M1").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         execute(cmd);
      }
   }

   public void execute(ModelCommand command) { 
      String id = command.getId();
      if (id == null) {
         id = "obj" + activeCommands.size();
         command.setId(id);
      }

      String time = command.getTime();
      if (time == null) {
         time = getTime();
         command.setTime(time);
      }

      ModelCommand oldCommand = activeCommands.get(id);

      if (oldCommand != null && oldCommand.getTime().compareTo(time) >= 0) {
         // already updated
         return;
      }

      if (oldCommand != null && oldCommand instanceof RemoveCommand) {
         // object is dead, do not recreate
         return;
      }

      command.run(this);

      activeCommands.put(id, command);
   }

}

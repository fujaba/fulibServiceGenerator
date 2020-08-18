package javaPackagesToJavaDoc.JavaPackages;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.ArrayList;
import org.fulib.yaml.Yaml;
import java.util.*;
import org.fulib.yaml.Reflector;
import java.util.Objects;

public class JavaPackagesEditor
{

   public static final String PROPERTY_service = "service";

   private JavaPackagesService service;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_activeCommands = "activeCommands";
   private java.util.Map<String,ModelCommand> activeCommands = new java.util.LinkedHashMap<>();
   public static final String PROPERTY_commandListeners = "commandListeners";
   private java.util.Map<String,ArrayList<CommandStream>> commandListeners = new java.util.LinkedHashMap<>();
   public static final String PROPERTY_mapOfFrames = "mapOfFrames";
   private java.util.Map<String,Object> mapOfFrames = new java.util.LinkedHashMap<>();
   public static final String PROPERTY_mapOfModelObjects = "mapOfModelObjects";
   private java.util.Map<String,Object> mapOfModelObjects = new java.util.LinkedHashMap<>();
   public static final String PROPERTY_isoDateFormat = "isoDateFormat";
   private DateFormat isoDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
   public static final String PROPERTY_lastTime = "lastTime";
   private String lastTime = isoDateFormat.format(new Date());
   public static final String PROPERTY_timeDelta = "timeDelta";
   private long timeDelta = 1;
   public static final String PROPERTY_commandPrototypes = "commandPrototypes";
   private ArrayList<ModelCommand> commandPrototypes;

   public JavaPackagesService getService()
   {
      return this.service;
   }

   public JavaPackagesEditor setService(JavaPackagesService value)
   {
      if (this.service == value)
      {
         return this;
      }

      final JavaPackagesService oldValue = this.service;
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
      this.firePropertyChange(PROPERTY_service, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(propertyName, listener);
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
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getLastTime());
      return result.substring(1);
   }

   public Object getOrCreate(Class clazz, String id)
   {
      Object modelObject = mapOfModelObjects.get(id);
      if (modelObject != null) {
         return modelObject;
      }

      modelObject = getObjectFrame(clazz, id);

      mapOfFrames.remove(id);
      mapOfModelObjects.put(id, modelObject);

      return modelObject;
   }

   public Object getObjectFrame(Class clazz, String id)
   {
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

   public Object getModelObject(String id)
   {
         return mapOfModelObjects.get(id);
   }

   public Object removeModelObject(String id)
   {
      Object oldObject = mapOfModelObjects.remove(id);

      if (oldObject != null) {
         mapOfFrames.put(id, oldObject);
      }

      return mapOfFrames.get(id);
   }

   public String getTime()
   {
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

   public void fireCommandExecuted(ModelCommand command)
   {
      String commandName = command.getClass().getSimpleName();
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      for (CommandStream stream : listeners) {
         stream.publish(command);
      }
   }

   public JavaPackagesEditor addCommandListener(String commandName, CommandStream stream)
   {
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString)
   {
      java.util.Map map = Yaml.forPackage("javaPackagesToJavaDoc.JavaPackages").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         execute(cmd);
      }
   }

   public void execute(ModelCommand command)
   {
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

   public void parse(Collection allObjects)
   {
      // add parsed objects to model
      for (Object parsedObject : allObjects) {
         Reflector reflector = new Reflector().setClazz(parsedObject.getClass());
         String id = (String) reflector.getValue(parsedObject, "id");
         if (id != null) {
            mapOfModelObjects.put(id, parsedObject);
            mapOfFrames.remove(id);
         }
      }

      ArrayList<ModelCommand> allCommandsFromParsing = new ArrayList<>();
      for (Object currentObject : allObjects) {
         findCommands(allCommandsFromParsing, currentObject);
      }

      // add parsed commands, if new
      for (ModelCommand commandFromParsing : allCommandsFromParsing) {
         String id = commandFromParsing.getId();
         ModelCommand oldCommand = activeCommands.get(id);
         if (oldCommand == null || ! equalsButTime(oldCommand, commandFromParsing)) {
            execute(commandFromParsing);
         }
      }
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

   private ModelCommand getFromAllCommands(ArrayList<ModelCommand> allCommands, String id)
   {
      for (ModelCommand command : allCommands) {
         if (command.getId().equals(id)) {
            return command;
         }
      }
      return null;
   }

   public boolean equalsButTime(ModelCommand oldCommand, ModelCommand newCommand)
   {
      if (oldCommand.getClass() != newCommand.getClass()) {
         return false;
      }

      Reflector reflector = new Reflector().setClazz(oldCommand.getClass());

      for (String property : reflector.getProperties()) {
         if ("time".equals(property)) {
            continue;
         }
         Object oldValue = reflector.getValue(oldCommand, property);
         Object newValue = reflector.getValue(newCommand, property);

         if ( ! Objects.equals(oldValue, newValue)) {
            return false;
         }
      }

      return true;
   }

private ArrayList<ModelCommand> haveCommandPrototypes()
   {
      if (commandPrototypes == null) {
         commandPrototypes = new ArrayList<>();
         commandPrototypes.add(new HaveRoot());
         commandPrototypes.add(new HaveSubUnit());
         commandPrototypes.add(new HaveLeaf());
      }

      return commandPrototypes;
   }

   public java.util.Map<String,ModelCommand> getActiveCommands()
   {
      return this.activeCommands;
   }

   public JavaPackagesEditor setActiveCommands(java.util.Map<String,ModelCommand> value)
   {
      if (Objects.equals(value, this.activeCommands))
      {
         return this;
      }

      final java.util.Map<String,ModelCommand> oldValue = this.activeCommands;
      this.activeCommands = value;
      this.firePropertyChange(PROPERTY_activeCommands, oldValue, value);
      return this;
   }

   public java.util.Map<String,ArrayList<CommandStream>> getCommandListeners()
   {
      return this.commandListeners;
   }

   public JavaPackagesEditor setCommandListeners(java.util.Map<String,ArrayList<CommandStream>> value)
   {
      if (Objects.equals(value, this.commandListeners))
      {
         return this;
      }

      final java.util.Map<String,ArrayList<CommandStream>> oldValue = this.commandListeners;
      this.commandListeners = value;
      this.firePropertyChange(PROPERTY_commandListeners, oldValue, value);
      return this;
   }

   public java.util.Map<String,Object> getMapOfFrames()
   {
      return this.mapOfFrames;
   }

   public JavaPackagesEditor setMapOfFrames(java.util.Map<String,Object> value)
   {
      if (Objects.equals(value, this.mapOfFrames))
      {
         return this;
      }

      final java.util.Map<String,Object> oldValue = this.mapOfFrames;
      this.mapOfFrames = value;
      this.firePropertyChange(PROPERTY_mapOfFrames, oldValue, value);
      return this;
   }

   public java.util.Map<String,Object> getMapOfModelObjects()
   {
      return this.mapOfModelObjects;
   }

   public JavaPackagesEditor setMapOfModelObjects(java.util.Map<String,Object> value)
   {
      if (Objects.equals(value, this.mapOfModelObjects))
      {
         return this;
      }

      final java.util.Map<String,Object> oldValue = this.mapOfModelObjects;
      this.mapOfModelObjects = value;
      this.firePropertyChange(PROPERTY_mapOfModelObjects, oldValue, value);
      return this;
   }

   public DateFormat getIsoDateFormat()
   {
      return this.isoDateFormat;
   }

   public JavaPackagesEditor setIsoDateFormat(DateFormat value)
   {
      if (Objects.equals(value, this.isoDateFormat))
      {
         return this;
      }

      final DateFormat oldValue = this.isoDateFormat;
      this.isoDateFormat = value;
      this.firePropertyChange(PROPERTY_isoDateFormat, oldValue, value);
      return this;
   }

   public String getLastTime()
   {
      return this.lastTime;
   }

   public JavaPackagesEditor setLastTime(String value)
   {
      if (Objects.equals(value, this.lastTime))
      {
         return this;
      }

      final String oldValue = this.lastTime;
      this.lastTime = value;
      this.firePropertyChange(PROPERTY_lastTime, oldValue, value);
      return this;
   }

   public long getTimeDelta()
   {
      return this.timeDelta;
   }

   public JavaPackagesEditor setTimeDelta(long value)
   {
      if (value == this.timeDelta)
      {
         return this;
      }

      final long oldValue = this.timeDelta;
      this.timeDelta = value;
      this.firePropertyChange(PROPERTY_timeDelta, oldValue, value);
      return this;
   }

   public ArrayList<ModelCommand> getCommandPrototypes()
   {
      return this.commandPrototypes;
   }

   public JavaPackagesEditor setCommandPrototypes(ArrayList<ModelCommand> value)
   {
      if (Objects.equals(value, this.commandPrototypes))
      {
         return this;
      }

      final ArrayList<ModelCommand> oldValue = this.commandPrototypes;
      this.commandPrototypes = value;
      this.firePropertyChange(PROPERTY_commandPrototypes, oldValue, value);
      return this;
   }

}
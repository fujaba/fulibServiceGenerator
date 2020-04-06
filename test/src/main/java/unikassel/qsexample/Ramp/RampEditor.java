package unikassel.qsexample.Ramp;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import org.fulib.yaml.Yaml;
import java.util.ArrayList;

public class RampEditor  
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public RampEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public RampEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
   {
      if (value != this.removeCommands)
      {
         java.util.Map<String, RemoveCommand> oldValue = this.removeCommands;
         this.removeCommands = value;
         firePropertyChange("removeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_isoDateFormat = "isoDateFormat";

   private DateFormat isoDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

   public DateFormat getIsoDateFormat()
   {
      return isoDateFormat;
   }

   public RampEditor setIsoDateFormat(DateFormat value)
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

   public RampEditor setLastTime(String value)
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

   public RampEditor setTimeDelta(long value)
   {
      if (value != this.timeDelta)
      {
         long oldValue = this.timeDelta;
         this.timeDelta = value;
         firePropertyChange("timeDelta", oldValue, value);
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

   public static final String PROPERTY_commandListeners = "commandListeners";

   private java.util.Map<String, ArrayList<CommandStream>> commandListeners = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ArrayList<CommandStream>> getCommandListeners()
   {
      return commandListeners;
   }

   public RampEditor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
   {
      if (value != this.commandListeners)
      {
         java.util.Map<String, ArrayList<CommandStream>> oldValue = this.commandListeners;
         this.commandListeners = value;
         firePropertyChange("commandListeners", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_rampProducts = "rampProducts";

   private java.util.Map<String, RampProduct> rampProducts = new java.util.LinkedHashMap<>();

   public java.util.Map<String, RampProduct> getRampProducts()
   {
      return rampProducts;
   }

   public RampEditor setRampProducts(java.util.Map<String, RampProduct> value)
   {
      if (value != this.rampProducts)
      {
         java.util.Map<String, RampProduct> oldValue = this.rampProducts;
         this.rampProducts = value;
         firePropertyChange("rampProducts", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_rampSuppliers = "rampSuppliers";

   private java.util.Map<String, RampSupplier> rampSuppliers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, RampSupplier> getRampSuppliers()
   {
      return rampSuppliers;
   }

   public RampEditor setRampSuppliers(java.util.Map<String, RampSupplier> value)
   {
      if (value != this.rampSuppliers)
      {
         java.util.Map<String, RampSupplier> oldValue = this.rampSuppliers;
         this.rampSuppliers = value;
         firePropertyChange("rampSuppliers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_rampSupplys = "rampSupplys";

   private java.util.Map<String, RampSupply> rampSupplys = new java.util.LinkedHashMap<>();

   public java.util.Map<String, RampSupply> getRampSupplys()
   {
      return rampSupplys;
   }

   public RampEditor setRampSupplys(java.util.Map<String, RampSupply> value)
   {
      if (value != this.rampSupplys)
      {
         java.util.Map<String, RampSupply> oldValue = this.rampSupplys;
         this.rampSupplys = value;
         firePropertyChange("rampSupplys", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_rampCustomers = "rampCustomers";

   private java.util.Map<String, RampCustomer> rampCustomers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, RampCustomer> getRampCustomers()
   {
      return rampCustomers;
   }

   public RampEditor setRampCustomers(java.util.Map<String, RampCustomer> value)
   {
      if (value != this.rampCustomers)
      {
         java.util.Map<String, RampCustomer> oldValue = this.rampCustomers;
         this.rampCustomers = value;
         firePropertyChange("rampCustomers", oldValue, value);
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

   public RampEditor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.qsexample.Ramp").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public RampProduct getOrCreateRampProduct(String id) { 
      if (id == null) {
         return null;
      }
      RampProduct oldObject = this.getRampProducts().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      RampProduct newObject = new RampProduct();
      newObject.setId(id);
      this.getRampProducts().put(id, newObject);
      return newObject;
   }

   public RampSupplier getOrCreateRampSupplier(String id) { 
      if (id == null) {
         return null;
      }
      RampSupplier oldObject = this.getRampSuppliers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      RampSupplier newObject = new RampSupplier();
      newObject.setId(id);
      this.getRampSuppliers().put(id, newObject);
      return newObject;
   }

   public RampSupply getOrCreateRampSupply(String id) { 
      if (id == null) {
         return null;
      }
      RampSupply oldObject = this.getRampSupplys().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      RampSupply newObject = new RampSupply();
      newObject.setId(id);
      this.getRampSupplys().put(id, newObject);
      return newObject;
   }

   public RampCustomer getOrCreateRampCustomer(String id) { 
      if (id == null) {
         return null;
      }
      RampCustomer oldObject = this.getRampCustomers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      RampCustomer newObject = new RampCustomer();
      newObject.setId(id);
      this.getRampCustomers().put(id, newObject);
      return newObject;
   }

}

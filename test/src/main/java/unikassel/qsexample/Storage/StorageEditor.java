package unikassel.qsexample.Storage;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import org.fulib.yaml.Yaml;
import java.util.ArrayList;

public class StorageEditor  
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public StorageEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public StorageEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
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

   public StorageEditor setIsoDateFormat(DateFormat value)
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

   public StorageEditor setLastTime(String value)
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

   public StorageEditor setTimeDelta(long value)
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

   public StorageEditor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
   {
      if (value != this.commandListeners)
      {
         java.util.Map<String, ArrayList<CommandStream>> oldValue = this.commandListeners;
         this.commandListeners = value;
         firePropertyChange("commandListeners", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storageProducts = "storageProducts";

   private java.util.Map<String, StorageProduct> storageProducts = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StorageProduct> getStorageProducts()
   {
      return storageProducts;
   }

   public StorageEditor setStorageProducts(java.util.Map<String, StorageProduct> value)
   {
      if (value != this.storageProducts)
      {
         java.util.Map<String, StorageProduct> oldValue = this.storageProducts;
         this.storageProducts = value;
         firePropertyChange("storageProducts", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storageSuppliers = "storageSuppliers";

   private java.util.Map<String, StorageSupplier> storageSuppliers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StorageSupplier> getStorageSuppliers()
   {
      return storageSuppliers;
   }

   public StorageEditor setStorageSuppliers(java.util.Map<String, StorageSupplier> value)
   {
      if (value != this.storageSuppliers)
      {
         java.util.Map<String, StorageSupplier> oldValue = this.storageSuppliers;
         this.storageSuppliers = value;
         firePropertyChange("storageSuppliers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storageSupplys = "storageSupplys";

   private java.util.Map<String, StorageSupply> storageSupplys = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StorageSupply> getStorageSupplys()
   {
      return storageSupplys;
   }

   public StorageEditor setStorageSupplys(java.util.Map<String, StorageSupply> value)
   {
      if (value != this.storageSupplys)
      {
         java.util.Map<String, StorageSupply> oldValue = this.storageSupplys;
         this.storageSupplys = value;
         firePropertyChange("storageSupplys", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storageCustomers = "storageCustomers";

   private java.util.Map<String, StorageCustomer> storageCustomers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StorageCustomer> getStorageCustomers()
   {
      return storageCustomers;
   }

   public StorageEditor setStorageCustomers(java.util.Map<String, StorageCustomer> value)
   {
      if (value != this.storageCustomers)
      {
         java.util.Map<String, StorageCustomer> oldValue = this.storageCustomers;
         this.storageCustomers = value;
         firePropertyChange("storageCustomers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_service = "service";

   private StorageService service = null;

   public StorageService getService()
   {
      return this.service;
   }

   public StorageEditor setService(StorageService value)
   {
      if (this.service != value)
      {
         StorageService oldValue = this.service;
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

   public StorageEditor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.qsexample.Storage").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public StorageProduct getOrCreateStorageProduct(String id) { 
      if (id == null) {
         return null;
      }
      StorageProduct oldObject = this.getStorageProducts().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StorageProduct newObject = new StorageProduct();
      newObject.setId(id);
      this.getStorageProducts().put(id, newObject);
      return newObject;
   }

   public StorageSupplier getOrCreateStorageSupplier(String id) { 
      if (id == null) {
         return null;
      }
      StorageSupplier oldObject = this.getStorageSuppliers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StorageSupplier newObject = new StorageSupplier();
      newObject.setId(id);
      this.getStorageSuppliers().put(id, newObject);
      return newObject;
   }

   public StorageSupply getOrCreateStorageSupply(String id) { 
      if (id == null) {
         return null;
      }
      StorageSupply oldObject = this.getStorageSupplys().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StorageSupply newObject = new StorageSupply();
      newObject.setId(id);
      this.getStorageSupplys().put(id, newObject);
      return newObject;
   }

   public StorageCustomer getOrCreateStorageCustomer(String id) { 
      if (id == null) {
         return null;
      }
      StorageCustomer oldObject = this.getStorageCustomers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StorageCustomer newObject = new StorageCustomer();
      newObject.setId(id);
      this.getStorageCustomers().put(id, newObject);
      return newObject;
   }

}

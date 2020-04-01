package unikassel.websystem.Store;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.fulib.yaml.Yaml;
import java.text.DateFormat;
import java.util.Date;

public class StoreEditor  
{
   private LinkedHashMap<String, ArrayList<CommandStream>> commandListeners = new LinkedHashMap<>();

   public StoreEditor addCommandListener(String commandName, CommandStream stream) {
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void fireCommandExecuted(ModelCommand command)
   {
      String commandName = command.getClass().getSimpleName();
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      for (CommandStream stream : listeners) {
         stream.publish(command);
      }
   }

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public StoreEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public StoreEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
   {
      if (value != this.removeCommands)
      {
         java.util.Map<String, RemoveCommand> oldValue = this.removeCommands;
         this.removeCommands = value;
         firePropertyChange("removeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storeProducts = "storeProducts";

   private java.util.Map<String, StoreProduct> storeProducts = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StoreProduct> getStoreProducts()
   {
      return storeProducts;
   }

   public StoreEditor setStoreProducts(java.util.Map<String, StoreProduct> value)
   {
      if (value != this.storeProducts)
      {
         java.util.Map<String, StoreProduct> oldValue = this.storeProducts;
         this.storeProducts = value;
         firePropertyChange("storeProducts", oldValue, value);
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

   public static final String PROPERTY_storeCustomers = "storeCustomers";

   private java.util.Map<String, StoreCustomer> storeCustomers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StoreCustomer> getStoreCustomers()
   {
      return storeCustomers;
   }

   public StoreEditor setStoreCustomers(java.util.Map<String, StoreCustomer> value)
   {
      if (value != this.storeCustomers)
      {
         java.util.Map<String, StoreCustomer> oldValue = this.storeCustomers;
         this.storeCustomers = value;
         firePropertyChange("storeCustomers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storeOffers = "storeOffers";

   private java.util.Map<String, StoreOffer> storeOffers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StoreOffer> getStoreOffers()
   {
      return storeOffers;
   }

   public StoreEditor setStoreOffers(java.util.Map<String, StoreOffer> value)
   {
      if (value != this.storeOffers)
      {
         java.util.Map<String, StoreOffer> oldValue = this.storeOffers;
         this.storeOffers = value;
         firePropertyChange("storeOffers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storeOrders = "storeOrders";

   private java.util.Map<String, StoreOrder> storeOrders = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StoreOrder> getStoreOrders()
   {
      return storeOrders;
   }

   public StoreEditor setStoreOrders(java.util.Map<String, StoreOrder> value)
   {
      if (value != this.storeOrders)
      {
         java.util.Map<String, StoreOrder> oldValue = this.storeOrders;
         this.storeOrders = value;
         firePropertyChange("storeOrders", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_storeOrderPositions = "storeOrderPositions";

   private java.util.Map<String, StoreOrderPosition> storeOrderPositions = new java.util.LinkedHashMap<>();

   public java.util.Map<String, StoreOrderPosition> getStoreOrderPositions()
   {
      return storeOrderPositions;
   }

   public StoreEditor setStoreOrderPositions(java.util.Map<String, StoreOrderPosition> value)
   {
      if (value != this.storeOrderPositions)
      {
         java.util.Map<String, StoreOrderPosition> oldValue = this.storeOrderPositions;
         this.storeOrderPositions = value;
         firePropertyChange("storeOrderPositions", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_isoDateFormat = "isoDateFormat";

   private DateFormat isoDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

   public DateFormat getIsoDateFormat()
   {
      return isoDateFormat;
   }

   public StoreEditor setIsoDateFormat(DateFormat value)
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

   public StoreEditor setLastTime(String value)
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

   public StoreEditor setTimeDelta(long value)
   {
      if (value != this.timeDelta)
      {
         long oldValue = this.timeDelta;
         this.timeDelta = value;
         firePropertyChange("timeDelta", oldValue, value);
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

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.websystem.Store").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public StoreProduct getOrCreateStoreProduct(String id) { 
      if (id == null) {
         return null;
      }
      StoreProduct oldObject = this.getStoreProducts().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StoreProduct newObject = new StoreProduct();
      newObject.setId(id);
      this.getStoreProducts().put(id, newObject);
      return newObject;
   }

   public StoreCustomer getOrCreateStoreCustomer(String id) { 
      if (id == null) {
         return null;
      }
      StoreCustomer oldObject = this.getStoreCustomers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StoreCustomer newObject = new StoreCustomer();
      newObject.setId(id);
      this.getStoreCustomers().put(id, newObject);
      return newObject;
   }

   public StoreOffer getOrCreateStoreOffer(String id) { 
      if (id == null) {
         return null;
      }
      StoreOffer oldObject = this.getStoreOffers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StoreOffer newObject = new StoreOffer();
      newObject.setId(id);
      this.getStoreOffers().put(id, newObject);
      return newObject;
   }

   public StoreOrder getOrCreateStoreOrder(String id) { 
      if (id == null) {
         return null;
      }
      StoreOrder oldObject = this.getStoreOrders().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StoreOrder newObject = new StoreOrder();
      newObject.setId(id);
      this.getStoreOrders().put(id, newObject);
      return newObject;
   }

   public StoreOrderPosition getOrCreateStoreOrderPosition(String id) { 
      if (id == null) {
         return null;
      }
      StoreOrderPosition oldObject = this.getStoreOrderPositions().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      StoreOrderPosition newObject = new StoreOrderPosition();
      newObject.setId(id);
      this.getStoreOrderPositions().put(id, newObject);
      return newObject;
   }

}

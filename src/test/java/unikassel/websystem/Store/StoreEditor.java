package unikassel.websystem.Store;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.fulib.yaml.Yaml;

public class StoreEditor  
{

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

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.websystem.Store").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public StoreProduct getOrCreateStoreProduct(String id) { 
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
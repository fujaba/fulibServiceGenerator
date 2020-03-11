package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.fulib.yaml.Yaml;

public class ShopEditor  
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public ShopEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public ShopEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
   {
      if (value != this.removeCommands)
      {
         java.util.Map<String, RemoveCommand> oldValue = this.removeCommands;
         this.removeCommands = value;
         firePropertyChange("removeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_shopProducts = "shopProducts";

   private java.util.Map<String, ShopProduct> shopProducts = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ShopProduct> getShopProducts()
   {
      return shopProducts;
   }

   public ShopEditor setShopProducts(java.util.Map<String, ShopProduct> value)
   {
      if (value != this.shopProducts)
      {
         java.util.Map<String, ShopProduct> oldValue = this.shopProducts;
         this.shopProducts = value;
         firePropertyChange("shopProducts", oldValue, value);
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

   public static final String PROPERTY_shopCustomers = "shopCustomers";

   private java.util.Map<String, ShopCustomer> shopCustomers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ShopCustomer> getShopCustomers()
   {
      return shopCustomers;
   }

   public ShopEditor setShopCustomers(java.util.Map<String, ShopCustomer> value)
   {
      if (value != this.shopCustomers)
      {
         java.util.Map<String, ShopCustomer> oldValue = this.shopCustomers;
         this.shopCustomers = value;
         firePropertyChange("shopCustomers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_shopOffers = "shopOffers";

   private java.util.Map<String, ShopOffer> shopOffers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ShopOffer> getShopOffers()
   {
      return shopOffers;
   }

   public ShopEditor setShopOffers(java.util.Map<String, ShopOffer> value)
   {
      if (value != this.shopOffers)
      {
         java.util.Map<String, ShopOffer> oldValue = this.shopOffers;
         this.shopOffers = value;
         firePropertyChange("shopOffers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_shopOrders = "shopOrders";

   private java.util.Map<String, ShopOrder> shopOrders = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ShopOrder> getShopOrders()
   {
      return shopOrders;
   }

   public ShopEditor setShopOrders(java.util.Map<String, ShopOrder> value)
   {
      if (value != this.shopOrders)
      {
         java.util.Map<String, ShopOrder> oldValue = this.shopOrders;
         this.shopOrders = value;
         firePropertyChange("shopOrders", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_shopOrderPositions = "shopOrderPositions";

   private java.util.Map<String, ShopOrderPosition> shopOrderPositions = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ShopOrderPosition> getShopOrderPositions()
   {
      return shopOrderPositions;
   }

   public ShopEditor setShopOrderPositions(java.util.Map<String, ShopOrderPosition> value)
   {
      if (value != this.shopOrderPositions)
      {
         java.util.Map<String, ShopOrderPosition> oldValue = this.shopOrderPositions;
         this.shopOrderPositions = value;
         firePropertyChange("shopOrderPositions", oldValue, value);
      }
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.websystem.Shop").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public ShopProduct getOrCreateShopProduct(String id) { 
      ShopProduct oldObject = this.getShopProducts().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      ShopProduct newObject = new ShopProduct();
      newObject.setId(id);
      this.getShopProducts().put(id, newObject);
      return newObject;
   }

   public ShopCustomer getOrCreateShopCustomer(String id) { 
      ShopCustomer oldObject = this.getShopCustomers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      ShopCustomer newObject = new ShopCustomer();
      newObject.setId(id);
      this.getShopCustomers().put(id, newObject);
      return newObject;
   }

   public ShopOffer getOrCreateShopOffer(String id) { 
      ShopOffer oldObject = this.getShopOffers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      ShopOffer newObject = new ShopOffer();
      newObject.setId(id);
      this.getShopOffers().put(id, newObject);
      return newObject;
   }

   public ShopOrder getOrCreateShopOrder(String id) { 
      ShopOrder oldObject = this.getShopOrders().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      ShopOrder newObject = new ShopOrder();
      newObject.setId(id);
      this.getShopOrders().put(id, newObject);
      return newObject;
   }

   public ShopOrderPosition getOrCreateShopOrderPosition(String id) { 
      ShopOrderPosition oldObject = this.getShopOrderPositions().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      ShopOrderPosition newObject = new ShopOrderPosition();
      newObject.setId(id);
      this.getShopOrderPositions().put(id, newObject);
      return newObject;
   }

}
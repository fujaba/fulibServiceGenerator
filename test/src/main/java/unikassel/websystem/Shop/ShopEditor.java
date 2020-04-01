package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import org.fulib.yaml.Yaml;

public class ShopEditor  
{
   private DateFormat isoDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
   private String lastTime = isoDateFormat.format(new Date());
   private long timeDelta = 1;

   public ShopEditor setTimeDelta(long value)
   {
      if (value != this.timeDelta)
      {
         long oldValue = this.timeDelta;
         this.timeDelta = value;
         firePropertyChange("timeDelta", oldValue, value);
      }
      return this;
   }

   public ShopEditor() {
      isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      lastTime =  isoDateFormat.format(new Date());
   }

   private LinkedHashMap<String, ArrayList<CommandStream>> commandListeners = new LinkedHashMap<>();

   public ShopEditor addCommandListener(String commandName, CommandStream stream) {
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void fireCommandExecuted(ModelCommand command) { 
// st.render();
   }

   public ShopEditor setLastTime(String value)
   {
      if (value == null ? this.lastTime != null : ! value.equals(this.lastTime))
      {
         String oldValue = this.lastTime;
         this.lastTime = value;
         firePropertyChange("lastTime", oldValue, value);
      }
      return this;
   }

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

   public static final String PROPERTY_isoDateFormat = "isoDateFormat";

   public DateFormat getIsoDateFormat()
   {
      return isoDateFormat;
   }

   public ShopEditor setIsoDateFormat(DateFormat value)
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

   public String getLastTime()
   {
      return lastTime;
   }

   public static final String PROPERTY_timeDelta = "timeDelta";

   public long getTimeDelta()
   {
      return timeDelta;
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
      java.util.Map map = Yaml.forPackage("unikassel.websystem.Shop").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public ShopProduct getOrCreateShopProduct(String id) { 
      if (id == null) {
         return null;
      }
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
      if (id == null) {
         return null;
      }
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
      if (id == null) {
         return null;
      }
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
      if (id == null) {
         return null;
      }
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
      if (id == null) {
         return null;
      }
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

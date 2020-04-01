package unikassel.shop.model;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.fulib.yaml.Yaml;
import org.fulib.yaml.YamlIdMap;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class StoreEditor  
{

   public <T> T getOrCreate(Class<T> dataClass, String id ) {
      // does it already exist?
      ReflectorMap reflectorMap = new ReflectorMap(this.getClass().getPackage().getName());
      Reflector reflector = reflectorMap.getReflector(this);
      Object value = reflector.getValue(this, dataClass.getSimpleName() + "s");
      Map<String, T> objects = (Map<String, T>) value;
      T oldObject = objects.get(id);
      if (oldObject != null) {
         return oldObject;
      }

      // make a new one
      try {
         Constructor<T> constructor = dataClass.getConstructor(new Class[0]);
         T newObject = constructor.newInstance(new Object[0]);
         objects.put(id, newObject);
         reflector = reflectorMap.getReflector(newObject);
         reflector.setValue(newObject, "id", id, ClassModelBuilder.STRING);
         return newObject;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public StoreEditor init()
   {
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

   public static final String PROPERTY_products = "products";

   private java.util.Map<String, Product> products = new java.util.LinkedHashMap<>();

   public java.util.Map<String, Product> getProducts()
   {
      return products;
   }

   public StoreEditor setProducts(java.util.Map<String, Product> value)
   {
      if (value != this.products)
      {
         java.util.Map<String, Product> oldValue = this.products;
         this.products = value;
         firePropertyChange("products", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_customers = "customers";

   private java.util.Map<String, Customer> customers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, Customer> getCustomers()
   {
      return customers;
   }

   public StoreEditor setCustomers(java.util.Map<String, Customer> value)
   {
      if (value != this.customers)
      {
         java.util.Map<String, Customer> oldValue = this.customers;
         this.customers = value;
         firePropertyChange("customers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_offers = "offers";

   private java.util.Map<String, Offer> offers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, Offer> getOffers()
   {
      return offers;
   }

   public StoreEditor setOffers(java.util.Map<String, Offer> value)
   {
      if (value != this.offers)
      {
         java.util.Map<String, Offer> oldValue = this.offers;
         this.offers = value;
         firePropertyChange("offers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_orders = "orders";

   private java.util.Map<String, Order> orders = new java.util.LinkedHashMap<>();

   public java.util.Map<String, Order> getOrders()
   {
      return orders;
   }

   public StoreEditor setOrders(java.util.Map<String, Order> value)
   {
      if (value != this.orders)
      {
         java.util.Map<String, Order> oldValue = this.orders;
         this.orders = value;
         firePropertyChange("orders", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_orderPositions = "orderPositions";

   private java.util.Map<String, OrderPosition> orderPositions = new java.util.LinkedHashMap<>();

   public java.util.Map<String, OrderPosition> getOrderPositions()
   {
      return orderPositions;
   }

   public StoreEditor setOrderPositions(java.util.Map<String, OrderPosition> value)
   {
      if (value != this.orderPositions)
      {
         java.util.Map<String, OrderPosition> oldValue = this.orderPositions;
         this.orderPositions = value;
         firePropertyChange("orderPositions", oldValue, value);
      }
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.shop.model").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public Product getOrCreateProduct(String id) { 
      Product oldObject = this.getProducts().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      Product newObject = new Product();
      newObject.setId(id);
      this.getProducts().put(id, newObject);
      return newObject;
   }

   public Customer getOrCreateCustomer(String id) { 
      Customer oldObject = this.getCustomers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      Customer newObject = new Customer();
      newObject.setId(id);
      this.getCustomers().put(id, newObject);
      return newObject;
   }

   public Offer getOrCreateOffer(String id) { 
      Offer oldObject = this.getOffers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      Offer newObject = new Offer();
      newObject.setId(id);
      this.getOffers().put(id, newObject);
      return newObject;
   }

   public Order getOrCreateOrder(String id) { 
      Order oldObject = this.getOrders().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      Order newObject = new Order();
      newObject.setId(id);
      this.getOrders().put(id, newObject);
      return newObject;
   }

   public OrderPosition getOrCreateOrderPosition(String id) { 
      OrderPosition oldObject = this.getOrderPositions().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      OrderPosition newObject = new OrderPosition();
      newObject.setId(id);
      this.getOrderPositions().put(id, newObject);
      return newObject;
   }

}

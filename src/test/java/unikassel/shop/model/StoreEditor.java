package unikassel.shop.model;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.fulib.yaml.Yaml;

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

   public static final String PROPERTY_removeCommands = "removeCommands";

   private Map<String, RemoveCommand> removeCommands = new LinkedHashMap<>();

   public Map<String, RemoveCommand> getRemoveCommands()
   {
      return removeCommands;
   }

   public StoreEditor setRemoveCommands(Map<String, RemoveCommand> value)
   {
      if (value != this.removeCommands)
      {
         Map<String, RemoveCommand> oldValue = this.removeCommands;
         this.removeCommands = value;
         firePropertyChange("removeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_products = "products";

   private Map<String, Product> products = new LinkedHashMap<>();

   public Map<String, Product> getProducts()
   {
      return products;
   }

   public StoreEditor setProducts(Map<String, Product> value)
   {
      if (value != this.products)
      {
         Map<String, Product> oldValue = this.products;
         this.products = value;
         firePropertyChange("products", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_customers = "customers";

   private Map<String, Customer> customers = new LinkedHashMap<>();

   public Map<String, Customer> getCustomers()
   {
      return customers;
   }

   public StoreEditor setCustomers(Map<String, Customer> value)
   {
      if (value != this.customers)
      {
         Map<String, Customer> oldValue = this.customers;
         this.customers = value;
         firePropertyChange("customers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_offers = "offers";

   private Map<String, Offer> offers = new LinkedHashMap<>();

   public Map<String, Offer> getOffers()
   {
      return offers;
   }

   public StoreEditor setOffers(Map<String, Offer> value)
   {
      if (value != this.offers)
      {
         Map<String, Offer> oldValue = this.offers;
         this.offers = value;
         firePropertyChange("offers", oldValue, value);
      }
      return this;
   }

   private Map<String, ModelCommand> activeCommands = new LinkedHashMap<>();

   public Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
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

   public StoreEditor setActiveCommands(Map<String, ModelCommand> value)
   {
      if (value != this.activeCommands)
      {
         Map<String, ModelCommand> oldValue = this.activeCommands;
         this.activeCommands = value;
         firePropertyChange("activeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_orders = "orders";

   private Map<String, Order> orders = new LinkedHashMap<>();

   public Map<String, Order> getOrders()
   {
      return orders;
   }

   public StoreEditor setOrders(Map<String, Order> value)
   {
      if (value != this.orders)
      {
         Map<String, Order> oldValue = this.orders;
         this.orders = value;
         firePropertyChange("orders", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_orderPositions = "orderPositions";

   private Map<String, OrderPosition> orderPositions = new LinkedHashMap<>();

   public Map<String, OrderPosition> getOrderPositions()
   {
      return orderPositions;
   }

   public StoreEditor setOrderPositions(Map<String, OrderPosition> value)
   {
      if (value != this.orderPositions)
      {
         Map<String, OrderPosition> oldValue = this.orderPositions;
         this.orderPositions = value;
         firePropertyChange("orderPositions", oldValue, value);
      }
      return this;
   }

   public void loadYaml(String yamlString) { 
      Yaml idMap;
      idMap = new Yaml("unikassel.shop.model");
      Object decode = idMap.decode(yamlString);
      Collection values = idMap.getObjIdMap().values();
      for (Object value : values) {
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

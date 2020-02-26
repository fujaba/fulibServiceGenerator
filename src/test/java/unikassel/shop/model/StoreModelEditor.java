package unikassel.shop.model;

import org.fulib.yaml.YamlIdMap;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class StoreModelEditor  
{
   private Map<String, Customer> customers = new LinkedHashMap<>();

   public Map<String, Customer> getCustomers()
   {
      return customers;
   }

   private LinkedHashMap<String, ModelCommand> activeCommands = new LinkedHashMap<>();

   public LinkedHashMap<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public StoreModelEditor init()
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

   public static final String PROPERTY_products = "products";

   private Map<String, Product> products = new LinkedHashMap<>();

   public Map<String, Product> getProducts()
   {
      return products;
   }

   public StoreModelEditor setProducts(Map<String, Product> value)
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

   public StoreModelEditor setCustomers(Map<String, Customer> value)
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

   public StoreModelEditor setOffers(Map<String, Offer> value)
   {
      if (value != this.offers)
      {
         Map<String, Offer> oldValue = this.offers;
         this.offers = value;
         firePropertyChange("offers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_removeCommands = "removeCommands";

   private Map<String, RemoveCommand> removeCommands = new LinkedHashMap<>();

   public Map<String, RemoveCommand> getRemoveCommands()
   {
      return removeCommands;
   }

   public StoreModelEditor setRemoveCommands(Map<String, RemoveCommand> value)
   {
      if (value != this.removeCommands)
      {
         Map<String, RemoveCommand> oldValue = this.removeCommands;
         this.removeCommands = value;
         firePropertyChange("removeCommands", oldValue, value);
      }
      return this;
   }

   public void loadYaml(String yamlString) { 
      YamlIdMap idMap;
      idMap = new YamlIdMap("unikassel.shop.model");
      Object decode = idMap.decode(yamlString);
      Collection values = idMap.getObjIdMap().values();
      for (Object value : values) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

}

package unikassel.websystem.Store;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class StoreCustomer  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public StoreCustomer setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public StoreCustomer setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_address = "address";

   private String address;

   public String getAddress()
   {
      return address;
   }

   public StoreCustomer setAddress(String value)
   {
      if (value == null ? this.address != null : ! value.equals(this.address))
      {
         String oldValue = this.address;
         this.address = value;
         firePropertyChange("address", oldValue, value);
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

      result.append(" ").append(this.getId());
      result.append(" ").append(this.getName());
      result.append(" ").append(this.getAddress());


      return result.substring(1);
   }

   public static final java.util.ArrayList<StoreOrder> EMPTY_orders = new java.util.ArrayList<StoreOrder>()
   { @Override public boolean add(StoreOrder value){ throw new UnsupportedOperationException("No direct add! Use xy.withOrders(obj)"); }};

   public static final String PROPERTY_orders = "orders";

   private java.util.ArrayList<StoreOrder> orders = null;

   public java.util.ArrayList<StoreOrder> getOrders()
   {
      if (this.orders == null)
      {
         return EMPTY_orders;
      }

      return this.orders;
   }

   public StoreCustomer withOrders(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withOrders(i);
            }
         }
         else if (item instanceof StoreOrder)
         {
            if (this.orders == null)
            {
               this.orders = new java.util.ArrayList<StoreOrder>();
            }
            if ( ! this.orders.contains(item))
            {
               this.orders.add((StoreOrder)item);
               ((StoreOrder)item).setCustomer(this);
               firePropertyChange("orders", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public StoreCustomer withoutOrders(Object... value)
   {
      if (this.orders == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutOrders(i);
            }
         }
         else if (item instanceof StoreOrder)
         {
            if (this.orders.contains(item))
            {
               this.orders.remove((StoreOrder)item);
               ((StoreOrder)item).setCustomer(null);
               firePropertyChange("orders", item, null);
            }
         }
      }
      return this;
   }

   public void removeYou()
   {
      this.withoutOrders(this.getOrders().clone());


      this.withoutProducts(this.getProducts().clone());


   }

   public static final java.util.ArrayList<StoreProduct> EMPTY_products = new java.util.ArrayList<StoreProduct>()
   { @Override public boolean add(StoreProduct value){ throw new UnsupportedOperationException("No direct add! Use xy.withProducts(obj)"); }};

   public static final String PROPERTY_products = "products";

   private java.util.ArrayList<StoreProduct> products = null;

   public java.util.ArrayList<StoreProduct> getProducts()
   {
      if (this.products == null)
      {
         return EMPTY_products;
      }

      return this.products;
   }

   public StoreCustomer withProducts(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withProducts(i);
            }
         }
         else if (item instanceof StoreProduct)
         {
            if (this.products == null)
            {
               this.products = new java.util.ArrayList<StoreProduct>();
            }
            if ( ! this.products.contains(item))
            {
               this.products.add((StoreProduct)item);
               ((StoreProduct)item).withCustomers(this);
               firePropertyChange("products", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public StoreCustomer withoutProducts(Object... value)
   {
      if (this.products == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutProducts(i);
            }
         }
         else if (item instanceof StoreProduct)
         {
            if (this.products.contains(item))
            {
               this.products.remove((StoreProduct)item);
               ((StoreProduct)item).withoutCustomers(this);
               firePropertyChange("products", item, null);
            }
         }
      }
      return this;
   }

}

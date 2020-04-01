package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ShopProduct  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public ShopProduct setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
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
      result.append(" ").append(this.getDescription());


      return result.substring(1);
   }

   public static final String PROPERTY_description = "description";

   private String description;

   public String getDescription()
   {
      return description;
   }

   public ShopProduct setDescription(String value)
   {
      if (value == null ? this.description != null : ! value.equals(this.description))
      {
         String oldValue = this.description;
         this.description = value;
         firePropertyChange("description", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_items = "items";

   private double items;

   public double getItems()
   {
      return items;
   }

   public ShopProduct setItems(double value)
   {
      if (value != this.items)
      {
         double oldValue = this.items;
         this.items = value;
         firePropertyChange("items", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<ShopOffer> EMPTY_offers = new java.util.ArrayList<ShopOffer>()
   { @Override public boolean add(ShopOffer value){ throw new UnsupportedOperationException("No direct add! Use xy.withOffers(obj)"); }};

   public static final String PROPERTY_offers = "offers";

   private java.util.ArrayList<ShopOffer> offers = null;

   public java.util.ArrayList<ShopOffer> getOffers()
   {
      if (this.offers == null)
      {
         return EMPTY_offers;
      }

      return this.offers;
   }

   public ShopProduct withOffers(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withOffers(i);
            }
         }
         else if (item instanceof ShopOffer)
         {
            if (this.offers == null)
            {
               this.offers = new java.util.ArrayList<ShopOffer>();
            }
            if ( ! this.offers.contains(item))
            {
               this.offers.add((ShopOffer)item);
               ((ShopOffer)item).setProduct(this);
               firePropertyChange("offers", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public ShopProduct withoutOffers(Object... value)
   {
      if (this.offers == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutOffers(i);
            }
         }
         else if (item instanceof ShopOffer)
         {
            if (this.offers.contains(item))
            {
               this.offers.remove((ShopOffer)item);
               ((ShopOffer)item).setProduct(null);
               firePropertyChange("offers", item, null);
            }
         }
      }
      return this;
   }

   public void removeYou()
   {
      this.withoutOffers(this.getOffers().clone());


      this.withoutCustomers(this.getCustomers().clone());


   }

   public static final java.util.ArrayList<ShopCustomer> EMPTY_customers = new java.util.ArrayList<ShopCustomer>()
   { @Override public boolean add(ShopCustomer value){ throw new UnsupportedOperationException("No direct add! Use xy.withCustomers(obj)"); }};

   public static final String PROPERTY_customers = "customers";

   private java.util.ArrayList<ShopCustomer> customers = null;

   public java.util.ArrayList<ShopCustomer> getCustomers()
   {
      if (this.customers == null)
      {
         return EMPTY_customers;
      }

      return this.customers;
   }

   public ShopProduct withCustomers(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withCustomers(i);
            }
         }
         else if (item instanceof ShopCustomer)
         {
            if (this.customers == null)
            {
               this.customers = new java.util.ArrayList<ShopCustomer>();
            }
            if ( ! this.customers.contains(item))
            {
               this.customers.add((ShopCustomer)item);
               ((ShopCustomer)item).withProducts(this);
               firePropertyChange("customers", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public ShopProduct withoutCustomers(Object... value)
   {
      if (this.customers == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutCustomers(i);
            }
         }
         else if (item instanceof ShopCustomer)
         {
            if (this.customers.contains(item))
            {
               this.customers.remove((ShopCustomer)item);
               ((ShopCustomer)item).withoutProducts(this);
               firePropertyChange("customers", item, null);
            }
         }
      }
      return this;
   }

}
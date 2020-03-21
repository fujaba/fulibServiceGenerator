package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ShopOrder  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public ShopOrder setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_date = "date";

   private String date;

   public String getDate()
   {
      return date;
   }

   public ShopOrder setDate(String value)
   {
      if (value == null ? this.date != null : ! value.equals(this.date))
      {
         String oldValue = this.date;
         this.date = value;
         firePropertyChange("date", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_state = "state";

   private String state;

   public String getState()
   {
      return state;
   }

   public ShopOrder setState(String value)
   {
      if (value == null ? this.state != null : ! value.equals(this.state))
      {
         String oldValue = this.state;
         this.state = value;
         firePropertyChange("state", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_customer = "customer";

   private ShopCustomer customer = null;

   public ShopCustomer getCustomer()
   {
      return this.customer;
   }

   public ShopOrder setCustomer(ShopCustomer value)
   {
      if (this.customer != value)
      {
         ShopCustomer oldValue = this.customer;
         if (this.customer != null)
         {
            this.customer = null;
            oldValue.withoutOrders(this);
         }
         this.customer = value;
         if (value != null)
         {
            value.withOrders(this);
         }
         firePropertyChange("customer", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<ShopOrderPosition> EMPTY_positions = new java.util.ArrayList<ShopOrderPosition>()
   { @Override public boolean add(ShopOrderPosition value){ throw new UnsupportedOperationException("No direct add! Use xy.withPositions(obj)"); }};

   public static final String PROPERTY_positions = "positions";

   private java.util.ArrayList<ShopOrderPosition> positions = null;

   public java.util.ArrayList<ShopOrderPosition> getPositions()
   {
      if (this.positions == null)
      {
         return EMPTY_positions;
      }

      return this.positions;
   }

   public ShopOrder withPositions(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withPositions(i);
            }
         }
         else if (item instanceof ShopOrderPosition)
         {
            if (this.positions == null)
            {
               this.positions = new java.util.ArrayList<ShopOrderPosition>();
            }
            if ( ! this.positions.contains(item))
            {
               this.positions.add((ShopOrderPosition)item);
               ((ShopOrderPosition)item).setOrder(this);
               firePropertyChange("positions", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public ShopOrder withoutPositions(Object... value)
   {
      if (this.positions == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutPositions(i);
            }
         }
         else if (item instanceof ShopOrderPosition)
         {
            if (this.positions.contains(item))
            {
               this.positions.remove((ShopOrderPosition)item);
               ((ShopOrderPosition)item).setOrder(null);
               firePropertyChange("positions", item, null);
            }
         }
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
      result.append(" ").append(this.getDate());
      result.append(" ").append(this.getState());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setCustomer(null);
      this.setShopApp(null);

      this.withoutPositions(this.getPositions().clone());


   }

   public static final String PROPERTY_shopApp = "shopApp";

   private ShopApp shopApp = null;

   public ShopApp getShopApp()
   {
      return this.shopApp;
   }

   public ShopOrder setShopApp(ShopApp value)
   {
      if (this.shopApp != value)
      {
         ShopApp oldValue = this.shopApp;
         if (this.shopApp != null)
         {
            this.shopApp = null;
            oldValue.setShoppingCard(null);
         }
         this.shopApp = value;
         if (value != null)
         {
            value.setShoppingCard(this);
         }
         firePropertyChange("shopApp", oldValue, value);
      }
      return this;
   }


}
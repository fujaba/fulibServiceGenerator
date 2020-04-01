package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ShopOrderPosition  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public ShopOrderPosition setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_amount = "amount";

   private double amount;

   public double getAmount()
   {
      return amount;
   }

   public ShopOrderPosition setAmount(double value)
   {
      if (value != this.amount)
      {
         double oldValue = this.amount;
         this.amount = value;
         firePropertyChange("amount", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_state = "state";

   private String state;

   public String getState()
   {
      return state;
   }

   public ShopOrderPosition setState(String value)
   {
      if (value == null ? this.state != null : ! value.equals(this.state))
      {
         String oldValue = this.state;
         this.state = value;
         firePropertyChange("state", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_order = "order";

   private ShopOrder order = null;

   public ShopOrder getOrder()
   {
      return this.order;
   }

   public ShopOrderPosition setOrder(ShopOrder value)
   {
      if (this.order != value)
      {
         ShopOrder oldValue = this.order;
         if (this.order != null)
         {
            this.order = null;
            oldValue.withoutPositions(this);
         }
         this.order = value;
         if (value != null)
         {
            value.withPositions(this);
         }
         firePropertyChange("order", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_offer = "offer";

   private ShopOffer offer = null;

   public ShopOffer getOffer()
   {
      return this.offer;
   }

   public ShopOrderPosition setOffer(ShopOffer value)
   {
      if (this.offer != value)
      {
         ShopOffer oldValue = this.offer;
         if (this.offer != null)
         {
            this.offer = null;
            oldValue.withoutOrders(this);
         }
         this.offer = value;
         if (value != null)
         {
            value.withOrders(this);
         }
         firePropertyChange("offer", oldValue, value);
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
      result.append(" ").append(this.getState());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setOrder(null);
      this.setOffer(null);

   }

}
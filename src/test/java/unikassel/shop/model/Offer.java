package unikassel.shop.model;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Offer  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Offer setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_price = "price";

   private double price;

   public double getPrice()
   {
      return price;
   }

   public Offer setPrice(double value)
   {
      if (value != this.price)
      {
         double oldValue = this.price;
         this.price = value;
         firePropertyChange("price", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_startTime = "startTime";

   private String startTime;

   public String getStartTime()
   {
      return startTime;
   }

   public Offer setStartTime(String value)
   {
      if (value == null ? this.startTime != null : ! value.equals(this.startTime))
      {
         String oldValue = this.startTime;
         this.startTime = value;
         firePropertyChange("startTime", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_endTime = "endTime";

   private String endTime;

   public String getEndTime()
   {
      return endTime;
   }

   public Offer setEndTime(String value)
   {
      if (value == null ? this.endTime != null : ! value.equals(this.endTime))
      {
         String oldValue = this.endTime;
         this.endTime = value;
         firePropertyChange("endTime", oldValue, value);
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
      result.append(" ").append(this.getStartTime());
      result.append(" ").append(this.getEndTime());


      return result.substring(1);
   }

   public static final String PROPERTY_product = "product";

   private Product product = null;

   public Product getProduct()
   {
      return this.product;
   }

   public Offer setProduct(Product value)
   {
      if (this.product != value)
      {
         Product oldValue = this.product;
         if (this.product != null)
         {
            this.product = null;
            oldValue.withoutOffers(this);
         }
         this.product = value;
         if (value != null)
         {
            value.withOffers(this);
         }
         firePropertyChange("product", oldValue, value);
      }
      return this;
   }

   public void removeYou()
   {
      this.setProduct(null);

   }

}
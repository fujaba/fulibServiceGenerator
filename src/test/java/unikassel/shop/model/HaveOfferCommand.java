package unikassel.shop.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveOfferCommand extends ModelCommand<HaveOfferCommand, Offer> // no fulib
{

   public static final String PROPERTY_price = "price";

   private double price;

   public double getPrice()
   {
      return price;
   }

   public HaveOfferCommand setPrice(double value)
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

   public HaveOfferCommand setStartTime(String value)
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

   public HaveOfferCommand setEndTime(String value)
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

      result.append(" ").append(this.getStartTime());
      result.append(" ").append(this.getEndTime());
      result.append(" ").append(this.getProduct());


      return result.substring(1);
   }

   @Override
   public Offer run(StoreEditor sme) { 
      if ( ! preCheck(sme)) {
         return sme.getOffers().get(this.getId());
      }
      Offer dataObject = this.getOrCreate(sme);
      dataObject.setPrice(this.getPrice());
      dataObject.setStartTime(this.getStartTime());
      dataObject.setEndTime(this.getEndTime());
      Product product = new HaveProductCommand().setId(this.getProduct()).getOrCreate(sme);
      dataObject.setProduct(product);

      return dataObject;
   }

   public static final String PROPERTY_product = "product";

   private String product;

   public String getProduct()
   {
      return product;
   }

   public HaveOfferCommand setProduct(String value)
   {
      if (value == null ? this.product != null : ! value.equals(this.product))
      {
         String oldValue = this.product;
         this.product = value;
         firePropertyChange("product", oldValue, value);
      }
      return this;
   }

   public Offer getOrCreate(StoreEditor sme) { 
      Object obj = sme.getOffers().get(this.getId());
      if (obj != null) {
         return (Offer) obj;
      }
      Offer newObj = new Offer().setId(this.getId());
      sme.getOffers().put(this.getId(), newObj);
      return newObj;
   }

   public boolean preCheck(StoreEditor editor) { 
      RemoveCommand oldRemove = editor.getRemoveCommands().get("Offer-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("Offer-" + this.getId());
      if (oldCommand != null && oldCommand.getTime().compareTo(this.getTime()) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("Offer-" + this.getId(), this);
      return true;
   }

}

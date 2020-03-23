package unikassel.websystem.Store;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveOfferCommand extends ModelCommand  
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

@Override
   public StoreOffer run(StoreEditor editor) { 
      if ( ! preCheck(editor)) {
         return editor.getStoreOffers().get(this.getId());
      }
      StoreOffer dataObject = editor.getOrCreateStoreOffer(this.getId());
      dataObject.setPrice(this.getPrice());
      dataObject.setStartTime(this.getStartTime());
      dataObject.setEndTime(this.getEndTime());
      StoreProduct product = editor.getOrCreateStoreProduct(this.getProduct());
      dataObject.setProduct(product);

      editor.fireCommandExecuted(this);
      return dataObject;
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

      result.append(" ").append(this.getProduct());
      result.append(" ").append(this.getStartTime());
      result.append(" ").append(this.getEndTime());


      return result.substring(1);
   }

   public boolean preCheck(StoreEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("StoreOffer-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("StoreOffer-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("StoreOffer-" + this.getId(), this);
      return true;
   }

}

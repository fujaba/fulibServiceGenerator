package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveOrderPositionCommand extends ModelCommand  
{

   public static final String PROPERTY_order = "order";

   private String order;

   public String getOrder()
   {
      return order;
   }

   public HaveOrderPositionCommand setOrder(String value)
   {
      if (value == null ? this.order != null : ! value.equals(this.order))
      {
         String oldValue = this.order;
         this.order = value;
         firePropertyChange("order", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_offer = "offer";

   private String offer;

   public String getOffer()
   {
      return offer;
   }

   public HaveOrderPositionCommand setOffer(String value)
   {
      if (value == null ? this.offer != null : ! value.equals(this.offer))
      {
         String oldValue = this.offer;
         this.offer = value;
         firePropertyChange("offer", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_amount = "amount";

   private double amount;

   public double getAmount()
   {
      return amount;
   }

   public HaveOrderPositionCommand setAmount(double value)
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

   public HaveOrderPositionCommand setState(String value)
   {
      if (value == null ? this.state != null : ! value.equals(this.state))
      {
         String oldValue = this.state;
         this.state = value;
         firePropertyChange("state", oldValue, value);
      }
      return this;
   }

@Override
   public ShopOrderPosition run(ShopEditor editor) { 
      if ( ! preCheck(editor)) {
         return editor.getShopOrderPositions().get(this.getId());
      }
      ShopOrderPosition dataObject = editor.getOrCreateShopOrderPosition(this.getId());
      dataObject.setAmount(this.getAmount());
      dataObject.setState(this.getState());
      ShopOrder order = editor.getOrCreateShopOrder(this.getOrder());
      dataObject.setOrder(order);
      ShopOffer offer = editor.getOrCreateShopOffer(this.getOffer());
      dataObject.setOffer(offer);

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

      result.append(" ").append(this.getOrder());
      result.append(" ").append(this.getOffer());
      result.append(" ").append(this.getState());


      return result.substring(1);
   }

   public boolean preCheck(ShopEditor editor) { 
      RemoveCommand oldRemove = editor.getRemoveCommands().get("ShopOrderPosition-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("ShopOrderPosition-" + this.getId());
      if (oldCommand != null && oldCommand.getTime().compareTo(this.getTime()) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("ShopOrderPosition-" + this.getId(), this);
      return true;
   }

}

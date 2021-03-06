package unikassel.websystem.Shop;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class OrderAction extends ModelCommand  
{
   @Override
   public Object run(ShopEditor modelEditor)
   {
      // have a customer
      ShopCustomer customer = new HaveCustomerCommand()
            .setId(this.name)
            .setName(this.name)
            .setAddress(this.address)
            .run(modelEditor);
      _app.setCustomer(customer);

      // have an order
      ShopOrder shoppingCard = _app.getShoppingCart();
      if (shoppingCard == null) {
         return null;
      }

      shoppingCard = new HaveOrderCommand()
            .setId(shoppingCard.getId())
            .setCustomer(customer.getId())
            .setState("order-submitted")
            .run(modelEditor);
      _app.setShoppingCart(null);

      return null;
   }

   public static final String PROPERTY_order = "order";

   private String order;

   public String getOrder()
   {
      return order;
   }

   public OrderAction setOrder(String value)
   {
      if (value == null ? this.order != null : ! value.equals(this.order))
      {
         String oldValue = this.order;
         this.order = value;
         firePropertyChange("order", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public OrderAction setName(String value)
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

   public OrderAction setAddress(String value)
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

      result.append(" ").append(this.getOrder());
      result.append(" ").append(this.getName());
      result.append(" ").append(this.getAddress());


      return result.substring(1);
   }

   public static final String PROPERTY__app = "_app";

   private ShopApp _app;

   public ShopApp get_app()
   {
      return _app;
   }

   public OrderAction set_app(ShopApp value)
   {
      if (value != this._app)
      {
         ShopApp oldValue = this._app;
         this._app = value;
         firePropertyChange("_app", oldValue, value);
      }
      return this;
   }

   public boolean preCheck(ShopEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("OrderAction-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("OrderAction-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("OrderAction-" + this.getId(), this);
      return true;
   }

}

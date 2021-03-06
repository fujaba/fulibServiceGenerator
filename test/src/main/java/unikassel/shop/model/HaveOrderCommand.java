package unikassel.shop.model;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveOrderCommand extends ModelCommand  
{

   @Override
   public Order run(StoreEditor editor) { 
      if ( ! preCheck(editor)) {
         return editor.getOrders().get(this.getId());
      }
      Order dataObject = this.getOrCreate(editor);
      dataObject.setDate(this.getDate());
      dataObject.setState(this.getState());
      Customer customer = editor.getOrCreateCustomer(this.getCustomer());
      dataObject.setCustomer(customer);

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

   public static final String PROPERTY_date = "date";

   private String date;

   public String getDate()
   {
      return date;
   }

   public HaveOrderCommand setDate(String value)
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

   public HaveOrderCommand setState(String value)
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
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getCustomer());
      result.append(" ").append(this.getDate());
      result.append(" ").append(this.getState());


      return result.substring(1);
   }

   public static final String PROPERTY_customer = "customer";

   private String customer;

   public String getCustomer()
   {
      return customer;
   }

   public HaveOrderCommand setCustomer(String value)
   {
      if (value == null ? this.customer != null : ! value.equals(this.customer))
      {
         String oldValue = this.customer;
         this.customer = value;
         firePropertyChange("customer", oldValue, value);
      }
      return this;
   }

   public Order getOrCreate(StoreEditor sme) { 
      java.util.Objects.requireNonNull(this.getId());
      Object obj = sme.getOrders().get(this.getId());
      if (obj != null) {
         return (Order) obj;
      }
      Order newObj = new Order().setId(this.getId());
      sme.getOrders().put(this.getId(), newObj);
      return newObj;
   }

   public boolean preCheck(StoreEditor editor) { 
      RemoveCommand oldRemove = editor.getRemoveCommands().get("Order-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("Order-" + this.getId());
      if (oldCommand != null && oldCommand.getTime().compareTo(this.getTime()) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("Order-" + this.getId(), this);
      return true;
   }

}

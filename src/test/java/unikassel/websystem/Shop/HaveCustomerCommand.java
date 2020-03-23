package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveCustomerCommand extends ModelCommand<HaveCustomerCommand, ShopCustomer> // no fulib
{

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public HaveCustomerCommand setName(String value)
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

   public HaveCustomerCommand setAddress(String value)
   {
      if (value == null ? this.address != null : ! value.equals(this.address))
      {
         String oldValue = this.address;
         this.address = value;
         firePropertyChange("address", oldValue, value);
      }
      return this;
   }

@Override
   public ShopCustomer run(ShopEditor editor) { 
      if ( ! preCheck(editor)) {
         return editor.getShopCustomers().get(this.getId());
      }
      ShopCustomer dataObject = editor.getOrCreateShopCustomer(this.getId());
      dataObject.setName(this.getName());
      dataObject.setAddress(this.getAddress());

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

      result.append(" ").append(this.getName());
      result.append(" ").append(this.getAddress());


      return result.substring(1);
   }

   public boolean preCheck(ShopEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("ShopCustomer-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("ShopCustomer-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("ShopCustomer-" + this.getId(), this);
      return true;
   }

}

package unikassel.qsexample.Accounting;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveSupplyCommand extends ModelCommand  
{

   public static final String PROPERTY_supplier = "supplier";

   private String supplier;

   public String getSupplier()
   {
      return supplier;
   }

   public HaveSupplyCommand setSupplier(String value)
   {
      if (value == null ? this.supplier != null : ! value.equals(this.supplier))
      {
         String oldValue = this.supplier;
         this.supplier = value;
         firePropertyChange("supplier", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_product = "product";

   private String product;

   public String getProduct()
   {
      return product;
   }

   public HaveSupplyCommand setProduct(String value)
   {
      if (value == null ? this.product != null : ! value.equals(this.product))
      {
         String oldValue = this.product;
         this.product = value;
         firePropertyChange("product", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_items = "items";

   private double items;

   public double getItems()
   {
      return items;
   }

   public HaveSupplyCommand setItems(double value)
   {
      if (value != this.items)
      {
         double oldValue = this.items;
         this.items = value;
         firePropertyChange("items", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_state = "state";

   private String state;

   public String getState()
   {
      return state;
   }

   public HaveSupplyCommand setState(String value)
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
   public AccountingSupply run(AccountingEditor editor) { 
      if ( ! preCheck(editor)) {
         return editor.getAccountingSupplys().get(this.getId());
      }
      AccountingSupply dataObject = editor.getOrCreateAccountingSupply(this.getId());
      dataObject.setItems(this.getItems());
      dataObject.setState(this.getState());
      AccountingSupplier supplier = editor.getOrCreateAccountingSupplier(this.getSupplier());
      dataObject.setSupplier(supplier);
      AccountingProduct product = editor.getOrCreateAccountingProduct(this.getProduct());
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

      result.append(" ").append(this.getSupplier());
      result.append(" ").append(this.getProduct());
      result.append(" ").append(this.getState());


      return result.substring(1);
   }

   public boolean preCheck(AccountingEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("AccountingSupply-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("AccountingSupply-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("AccountingSupply-" + this.getId(), this);
      return true;
   }

}
package unikassel.qsexample.Accounting;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class AddSupply extends ModelCommand  
{
   @Override
   public Object run(AccountingEditor editor)
   {
      System.out.println("Going to create supply record");
      String[] split = supplier.split(" ");
      String sid = split[0];
      String name = split[0];
      if (split.length > 1) {
         name = split[1];
      }
      AccountingSupplier supplier = (AccountingSupplier) new HaveSupplierCommand().setName(name).setId(sid)
            .run(editor);
      split = product.split(" ");
      String pid = split[0];
      name = split[0];
      if (split.length > 1) {
         name = split[1];
      }
      AccountingProduct product = (AccountingProduct) new HaveProductCommand().setDescription(name).setId(pid)
            .run(editor);

      Object supply = new HaveSupplyCommand().setSupplier(sid).setProduct(pid).setState("ordered").setItems(Double.parseDouble(items))
            .setId(pid + sid)
            .run(editor);
      return supply;
   }

   public static final String PROPERTY_supplier = "supplier";

   private String supplier;

   public String getSupplier()
   {
      return supplier;
   }

   public AddSupply setSupplier(String value)
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

   public AddSupply setProduct(String value)
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

   private String items;

   public String getItems()
   {
      return items;
   }

   public AddSupply setItems(String value)
   {
      if (value == null ? this.items != null : ! value.equals(this.items))
      {
         String oldValue = this.items;
         this.items = value;
         firePropertyChange("items", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY__app = "_app";

   private AccountingApp _app;

   public AccountingApp get_app()
   {
      return _app;
   }

   public AddSupply set_app(AccountingApp value)
   {
      if (value != this._app)
      {
         AccountingApp oldValue = this._app;
         this._app = value;
         firePropertyChange("_app", oldValue, value);
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

      result.append(" ").append(this.getSupplier());
      result.append(" ").append(this.getProduct());
      result.append(" ").append(this.getItems());


      return result.substring(1);
   }

}

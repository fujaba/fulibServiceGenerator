package unikassel.qsexample.Laboratory;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class LaboratorySupply  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public LaboratorySupply setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_items = "items";

   private double items;

   public double getItems()
   {
      return items;
   }

   public LaboratorySupply setItems(double value)
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

   public LaboratorySupply setState(String value)
   {
      if (value == null ? this.state != null : ! value.equals(this.state))
      {
         String oldValue = this.state;
         this.state = value;
         firePropertyChange("state", oldValue, value);
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
      this.setSupplier(null);
      this.setProduct(null);

   }

   public static final String PROPERTY_supplier = "supplier";

   private LaboratorySupplier supplier = null;

   public LaboratorySupplier getSupplier()
   {
      return this.supplier;
   }

   public LaboratorySupply setSupplier(LaboratorySupplier value)
   {
      if (this.supplier != value)
      {
         LaboratorySupplier oldValue = this.supplier;
         if (this.supplier != null)
         {
            this.supplier = null;
            oldValue.withoutDeliveries(this);
         }
         this.supplier = value;
         if (value != null)
         {
            value.withDeliveries(this);
         }
         firePropertyChange("supplier", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_product = "product";

   private LaboratoryProduct product = null;

   public LaboratoryProduct getProduct()
   {
      return this.product;
   }

   public LaboratorySupply setProduct(LaboratoryProduct value)
   {
      if (this.product != value)
      {
         LaboratoryProduct oldValue = this.product;
         if (this.product != null)
         {
            this.product = null;
            oldValue.withoutSupplies(this);
         }
         this.product = value;
         if (value != null)
         {
            value.withSupplies(this);
         }
         firePropertyChange("product", oldValue, value);
      }
      return this;
   }

}
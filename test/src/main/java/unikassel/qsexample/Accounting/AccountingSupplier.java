package unikassel.qsexample.Accounting;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class AccountingSupplier  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public AccountingSupplier setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public AccountingSupplier setName(String value)
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

   public AccountingSupplier setAddress(String value)
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

      result.append(" ").append(this.getId());
      result.append(" ").append(this.getName());
      result.append(" ").append(this.getAddress());


      return result.substring(1);
   }

   public static final java.util.ArrayList<AccountingSupply> EMPTY_deliveries = new java.util.ArrayList<AccountingSupply>()
   { @Override public boolean add(AccountingSupply value){ throw new UnsupportedOperationException("No direct add! Use xy.withDeliveries(obj)"); }};

   public static final String PROPERTY_deliveries = "deliveries";

   private java.util.ArrayList<AccountingSupply> deliveries = null;

   public java.util.ArrayList<AccountingSupply> getDeliveries()
   {
      if (this.deliveries == null)
      {
         return EMPTY_deliveries;
      }

      return this.deliveries;
   }

   public AccountingSupplier withDeliveries(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withDeliveries(i);
            }
         }
         else if (item instanceof AccountingSupply)
         {
            if (this.deliveries == null)
            {
               this.deliveries = new java.util.ArrayList<AccountingSupply>();
            }
            if ( ! this.deliveries.contains(item))
            {
               this.deliveries.add((AccountingSupply)item);
               ((AccountingSupply)item).setSupplier(this);
               firePropertyChange("deliveries", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public AccountingSupplier withoutDeliveries(Object... value)
   {
      if (this.deliveries == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutDeliveries(i);
            }
         }
         else if (item instanceof AccountingSupply)
         {
            if (this.deliveries.contains(item))
            {
               this.deliveries.remove((AccountingSupply)item);
               ((AccountingSupply)item).setSupplier(null);
               firePropertyChange("deliveries", item, null);
            }
         }
      }
      return this;
   }

   public void removeYou()
   {
      this.withoutDeliveries(this.getDeliveries().clone());


   }

}
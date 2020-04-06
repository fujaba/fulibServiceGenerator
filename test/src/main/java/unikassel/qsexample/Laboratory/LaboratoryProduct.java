package unikassel.qsexample.Laboratory;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class LaboratoryProduct  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public LaboratoryProduct setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_description = "description";

   private String description;

   public String getDescription()
   {
      return description;
   }

   public LaboratoryProduct setDescription(String value)
   {
      if (value == null ? this.description != null : ! value.equals(this.description))
      {
         String oldValue = this.description;
         this.description = value;
         firePropertyChange("description", oldValue, value);
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
      result.append(" ").append(this.getDescription());


      return result.substring(1);
   }

   public static final java.util.ArrayList<LaboratorySupply> EMPTY_supplies = new java.util.ArrayList<LaboratorySupply>()
   { @Override public boolean add(LaboratorySupply value){ throw new UnsupportedOperationException("No direct add! Use xy.withSupplies(obj)"); }};

   public static final String PROPERTY_supplies = "supplies";

   private java.util.ArrayList<LaboratorySupply> supplies = null;

   public java.util.ArrayList<LaboratorySupply> getSupplies()
   {
      if (this.supplies == null)
      {
         return EMPTY_supplies;
      }

      return this.supplies;
   }

   public LaboratoryProduct withSupplies(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withSupplies(i);
            }
         }
         else if (item instanceof LaboratorySupply)
         {
            if (this.supplies == null)
            {
               this.supplies = new java.util.ArrayList<LaboratorySupply>();
            }
            if ( ! this.supplies.contains(item))
            {
               this.supplies.add((LaboratorySupply)item);
               ((LaboratorySupply)item).setProduct(this);
               firePropertyChange("supplies", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public LaboratoryProduct withoutSupplies(Object... value)
   {
      if (this.supplies == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutSupplies(i);
            }
         }
         else if (item instanceof LaboratorySupply)
         {
            if (this.supplies.contains(item))
            {
               this.supplies.remove((LaboratorySupply)item);
               ((LaboratorySupply)item).setProduct(null);
               firePropertyChange("supplies", item, null);
            }
         }
      }
      return this;
   }

   public void removeYou()
   {
      this.withoutSupplies(this.getSupplies().clone());


   }

}
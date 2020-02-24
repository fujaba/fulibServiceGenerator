package unikassel.shop.model;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveProductCommand extends ModelCommand<HaveProductCommand, Product> // no fulib
{

   public static final String PROPERTY_description = "description";

   private String description;

   public String getDescription()
   {
      return description;
   }

   public HaveProductCommand setDescription(String value)
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

      result.append(" ").append(this.getDescription());


      return result.substring(1);
   }

   @Override
   public Product run(StoreModelEditor sme) { 
      if ( ! preCheck(sme)) {
         return sme.getProducts().get(this.getId());
      }
      Product dataObject = this.getOrCreate(sme);
      dataObject.setDescription(this.getDescription());

      return dataObject;
   }

   public boolean preCheck(StoreModelEditor editor) { // no fulib
      ModelCommand oldCommand = editor.getActiveCommands().get("Product-" + this.getId());
      if (oldCommand == null || oldCommand.getTime().compareTo(this.getTime()) < 0) {
         editor.getActiveCommands().put("Product-" + this.getId(), this);
         return true;
      }
      return false;
   }

   public Product getOrCreate(StoreModelEditor sme) { 
      Object obj = sme.getProducts().get(this.getId());
      if (obj != null) {
         return (Product) obj;
      }
      Product newObj = new Product().setId(this.getId());
      sme.getProducts().put(this.getId(), newObj);
      return newObj;
   }

}

package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveCustomerProductsLink extends ModelCommand  
{

   public static final String PROPERTY_source = "source";

   private String source;

   public String getSource()
   {
      return source;
   }

   public HaveCustomerProductsLink setSource(String value)
   {
      if (value == null ? this.source != null : ! value.equals(this.source))
      {
         String oldValue = this.source;
         this.source = value;
         firePropertyChange("source", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_target = "target";

   private String target;

   public String getTarget()
   {
      return target;
   }

   public HaveCustomerProductsLink setTarget(String value)
   {
      if (value == null ? this.target != null : ! value.equals(this.target))
      {
         String oldValue = this.target;
         this.target = value;
         firePropertyChange("target", oldValue, value);
      }
      return this;
   }

   @Override
   public Object run(ShopEditor editor) { 
      java.util.Objects.requireNonNull(this.getTime());
      this.setId("" + this.getSource() + "-Products-" + this.getTarget());
      ModelCommand oldCommand = editor.getActiveCommands().get(this.getId());
      if (oldCommand != null && oldCommand.getTime().compareTo(this.getTime()) >= 0) {
         // ignore new command
         return null;
      }

      ShopCustomer sourceObject = editor.getOrCreateShopCustomer(this.getSource());
      ShopProduct targetObject = editor.getOrCreateShopProduct(this.getTarget());
      sourceObject.withProducts(targetObject);

      editor.getActiveCommands().put(this.getId(), this);

      return null;
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

      result.append(" ").append(this.getSource());
      result.append(" ").append(this.getTarget());


      return result.substring(1);
   }

}
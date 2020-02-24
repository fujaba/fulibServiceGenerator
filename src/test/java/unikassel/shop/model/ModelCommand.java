package unikassel.shop.model;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ModelCommand<CMD extends ModelCommand,PROD> // no fulib
{
   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public CMD setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return (CMD) this;
   }

   public static final String PROPERTY_time = "time";

   private String time;

   public String getTime()
   {
      return time;
   }

   public CMD setTime(String value)
   {
      if (value == null ? this.time != null : ! value.equals(this.time))
      {
         String oldValue = this.time;
         this.time = value;
         firePropertyChange("time", oldValue, value);
      }
      return (CMD) this;
   }

   public PROD run(StoreModelEditor sme) {
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

}

package unikassel.qsexample.Storage;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class CommandStream  
{

   public static final String PROPERTY_service = "service";

   private StorageService service = null;

   public StorageService getService()
   {
      return this.service;
   }

   public CommandStream setService(StorageService value)
   {
      if (this.service != value)
      {
         StorageService oldValue = this.service;
         if (this.service != null)
         {
            this.service = null;
            oldValue.withoutStreams(this);
         }
         this.service = value;
         if (value != null)
         {
            value.withStreams(this);
         }
         firePropertyChange("service", oldValue, value);
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

   public void removeYou()
   {
      this.setService(null);

   }

}
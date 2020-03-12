package unikassel.websystem.Store;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class StoreService  
{

   public static final String PROPERTY_myPort = "myPort";

   private int myPort;

   public int getMyPort()
   {
      return myPort;
   }

   public StoreService setMyPort(int value)
   {
      if (value != this.myPort)
      {
         int oldValue = this.myPort;
         this.myPort = value;
         firePropertyChange("myPort", oldValue, value);
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

   public static final String PROPERTY_modelEditor = "modelEditor";

   private StoreEditor modelEditor;

   public StoreEditor getModelEditor()
   {
      return modelEditor;
   }

   public StoreService setModelEditor(StoreEditor value)
   {
      if (value != this.modelEditor)
      {
         StoreEditor oldValue = this.modelEditor;
         this.modelEditor = value;
         firePropertyChange("modelEditor", oldValue, value);
      }
      return this;
   }

   public void start() { 
      myPort = 4571;
      String envPort = System.getenv("PORT");
      if (envPort != null) {
         myPort = Integer.parseInt(envPort);
      }
      java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
      modelEditor = new StoreEditor();
   }

}

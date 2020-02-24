package unikassel.shop.model;
import org.fulib.yaml.YamlIdMap;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.LinkedHashMap;

public class StoreModelEditor  
{
   public static final String HAVE_PRODUCT = "haveProduct";

   private LinkedHashMap<String, Object> model = new LinkedHashMap<>();

   // what we really want:
   // private Map<String, Customer> customers = new LinkedHashMap<>();
   // public Map<String, Customer> getCustomers()
   //   {
   //      return Collections.unmodifiableMap(this.customers);
   //   }
   // for every data class, and also for every command

   public LinkedHashMap<String, Object> getModel()
   {
      return model;
   }

   private LinkedHashMap<String, ModelCommand> activeCommands = new LinkedHashMap<>();

   public LinkedHashMap<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public StoreModelEditor init() {
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

   public void loadYaml(String yamlString) { 
      YamlIdMap idMap;
      idMap = new YamlIdMap("unikassel.shop.model");
      Object decode = idMap.decode(yamlString);
      Collection values = idMap.getObjIdMap().values();
      for (Object value : values) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

}

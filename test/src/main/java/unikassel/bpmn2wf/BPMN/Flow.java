package unikassel.bpmn2wf.BPMN;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Flow  
{

   public static final String PROPERTY_source = "source";

   private Task source = null;

   public Task getSource()
   {
      return this.source;
   }

   public Flow setSource(Task value)
   {
      if (this.source != value)
      {
         Task oldValue = this.source;
         if (this.source != null)
         {
            this.source = null;
            oldValue.withoutOutgoing(this);
         }
         this.source = value;
         if (value != null)
         {
            value.withOutgoing(this);
         }
         firePropertyChange("source", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_target = "target";

   private Task target = null;

   public Task getTarget()
   {
      return this.target;
   }

   public Flow setTarget(Task value)
   {
      if (this.target != value)
      {
         Task oldValue = this.target;
         if (this.target != null)
         {
            this.target = null;
            oldValue.withoutIncomming(this);
         }
         this.target = value;
         if (value != null)
         {
            value.withIncomming(this);
         }
         firePropertyChange("target", oldValue, value);
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
      this.setSource(null);
      this.setTarget(null);

   }

}
package unikassel.bpmn2wf.WorkFlows;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Flow  
{

   public static final java.util.ArrayList<Step> EMPTY_steps = new java.util.ArrayList<Step>()
   { @Override public boolean add(Step value){ throw new UnsupportedOperationException("No direct add! Use xy.withSteps(obj)"); }};

   public static final String PROPERTY_steps = "steps";

   private java.util.ArrayList<Step> steps = null;

   public java.util.ArrayList<Step> getSteps()
   {
      if (this.steps == null)
      {
         return EMPTY_steps;
      }

      return this.steps;
   }

   public Flow withSteps(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withSteps(i);
            }
         }
         else if (item instanceof Step)
         {
            if (this.steps == null)
            {
               this.steps = new java.util.ArrayList<Step>();
            }
            if ( ! this.steps.contains(item))
            {
               this.steps.add((Step)item);
               ((Step)item).setParent(this);
               firePropertyChange("steps", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Flow withoutSteps(Object... value)
   {
      if (this.steps == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutSteps(i);
            }
         }
         else if (item instanceof Step)
         {
            if (this.steps.contains(item))
            {
               this.steps.remove((Step)item);
               ((Step)item).setParent(null);
               firePropertyChange("steps", item, null);
            }
         }
      }
      return this;
   }

   public static final String PROPERTY_invoker = "invoker";

   private Step invoker = null;

   public Step getInvoker()
   {
      return this.invoker;
   }

   public Flow setInvoker(Step value)
   {
      if (this.invoker != value)
      {
         Step oldValue = this.invoker;
         if (this.invoker != null)
         {
            this.invoker = null;
            oldValue.withoutInvokedFlows(this);
         }
         this.invoker = value;
         if (value != null)
         {
            value.withInvokedFlows(this);
         }
         firePropertyChange("invoker", oldValue, value);
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
      this.setFinalFlow(null);
      this.setInvoker(null);

      this.withoutSteps(this.getSteps().clone());


   }

   public static final String PROPERTY_finalFlow = "finalFlow";

   private Step finalFlow = null;

   public Step getFinalFlow()
   {
      return this.finalFlow;
   }

   public Flow setFinalFlow(Step value)
   {
      if (this.finalFlow != value)
      {
         Step oldValue = this.finalFlow;
         if (this.finalFlow != null)
         {
            this.finalFlow = null;
            oldValue.setFinalFlow(null);
         }
         this.finalFlow = value;
         if (value != null)
         {
            value.setFinalFlow(this);
         }
         firePropertyChange("finalFlow", oldValue, value);
      }
      return this;
   }

}
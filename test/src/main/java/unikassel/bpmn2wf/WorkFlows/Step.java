package unikassel.bpmn2wf.WorkFlows;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Step  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Step setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_text = "text";

   private String text;

   public String getText()
   {
      return text;
   }

   public Step setText(String value)
   {
      if (value == null ? this.text != null : ! value.equals(this.text))
      {
         String oldValue = this.text;
         this.text = value;
         firePropertyChange("text", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_kind = "kind";

   private String kind;

   public String getKind()
   {
      return kind;
   }

   public Step setKind(String value)
   {
      if (value == null ? this.kind != null : ! value.equals(this.kind))
      {
         String oldValue = this.kind;
         this.kind = value;
         firePropertyChange("kind", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<Step> EMPTY_next = new java.util.ArrayList<Step>()
   { @Override public boolean add(Step value){ throw new UnsupportedOperationException("No direct add! Use xy.withNext(obj)"); }};

   public static final String PROPERTY_next = "next";

   private java.util.ArrayList<Step> next = null;

   public java.util.ArrayList<Step> getNext()
   {
      if (this.next == null)
      {
         return EMPTY_next;
      }

      return this.next;
   }

   public Step withNext(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withNext(i);
            }
         }
         else if (item instanceof Step)
         {
            if (this.next == null)
            {
               this.next = new java.util.ArrayList<Step>();
            }
            if ( ! this.next.contains(item))
            {
               this.next.add((Step)item);
               ((Step)item).withPrev(this);
               firePropertyChange("next", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Step withoutNext(Object... value)
   {
      if (this.next == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutNext(i);
            }
         }
         else if (item instanceof Step)
         {
            if (this.next.contains(item))
            {
               this.next.remove((Step)item);
               ((Step)item).withoutPrev(this);
               firePropertyChange("next", item, null);
            }
         }
      }
      return this;
   }

   public static final java.util.ArrayList<Step> EMPTY_prev = new java.util.ArrayList<Step>()
   { @Override public boolean add(Step value){ throw new UnsupportedOperationException("No direct add! Use xy.withPrev(obj)"); }};

   public static final String PROPERTY_prev = "prev";

   private java.util.ArrayList<Step> prev = null;

   public java.util.ArrayList<Step> getPrev()
   {
      if (this.prev == null)
      {
         return EMPTY_prev;
      }

      return this.prev;
   }

   public Step withPrev(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withPrev(i);
            }
         }
         else if (item instanceof Step)
         {
            if (this.prev == null)
            {
               this.prev = new java.util.ArrayList<Step>();
            }
            if ( ! this.prev.contains(item))
            {
               this.prev.add((Step)item);
               ((Step)item).withNext(this);
               firePropertyChange("prev", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Step withoutPrev(Object... value)
   {
      if (this.prev == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutPrev(i);
            }
         }
         else if (item instanceof Step)
         {
            if (this.prev.contains(item))
            {
               this.prev.remove((Step)item);
               ((Step)item).withoutNext(this);
               firePropertyChange("prev", item, null);
            }
         }
      }
      return this;
   }

   public static final String PROPERTY_parent = "parent";

   private Flow parent = null;

   public Flow getParent()
   {
      return this.parent;
   }

   public Step setParent(Flow value)
   {
      if (this.parent != value)
      {
         Flow oldValue = this.parent;
         if (this.parent != null)
         {
            this.parent = null;
            oldValue.withoutSteps(this);
         }
         this.parent = value;
         if (value != null)
         {
            value.withSteps(this);
         }
         firePropertyChange("parent", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<Flow> EMPTY_invokedFlows = new java.util.ArrayList<Flow>()
   { @Override public boolean add(Flow value){ throw new UnsupportedOperationException("No direct add! Use xy.withInvokedFlows(obj)"); }};

   public static final String PROPERTY_invokedFlows = "invokedFlows";

   private java.util.ArrayList<Flow> invokedFlows = null;

   public java.util.ArrayList<Flow> getInvokedFlows()
   {
      if (this.invokedFlows == null)
      {
         return EMPTY_invokedFlows;
      }

      return this.invokedFlows;
   }

   public Step withInvokedFlows(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withInvokedFlows(i);
            }
         }
         else if (item instanceof Flow)
         {
            if (this.invokedFlows == null)
            {
               this.invokedFlows = new java.util.ArrayList<Flow>();
            }
            if ( ! this.invokedFlows.contains(item))
            {
               this.invokedFlows.add((Flow)item);
               ((Flow)item).setInvoker(this);
               firePropertyChange("invokedFlows", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Step withoutInvokedFlows(Object... value)
   {
      if (this.invokedFlows == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutInvokedFlows(i);
            }
         }
         else if (item instanceof Flow)
         {
            if (this.invokedFlows.contains(item))
            {
               this.invokedFlows.remove((Flow)item);
               ((Flow)item).setInvoker(null);
               firePropertyChange("invokedFlows", item, null);
            }
         }
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
      result.append(" ").append(this.getText());
      result.append(" ").append(this.getKind());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setParent(null);
      this.setFinalFlow(null);

      this.withoutNext(this.getNext().clone());


      this.withoutPrev(this.getPrev().clone());


      this.withoutInvokedFlows(this.getInvokedFlows().clone());


   }

   public static final String PROPERTY_finalFlow = "finalFlow";

   private Flow finalFlow = null;

   public Flow getFinalFlow()
   {
      return this.finalFlow;
   }

   public Step setFinalFlow(Flow value)
   {
      if (this.finalFlow != value)
      {
         Flow oldValue = this.finalFlow;
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
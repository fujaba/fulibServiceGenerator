package unikassel.bpmn2wf.BPMN;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Task  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Task setId(String value)
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

   public Task setText(String value)
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

   public Task setKind(String value)
   {
      if (value == null ? this.kind != null : ! value.equals(this.kind))
      {
         String oldValue = this.kind;
         this.kind = value;
         firePropertyChange("kind", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<Flow> EMPTY_outgoing = new java.util.ArrayList<Flow>()
   { @Override public boolean add(Flow value){ throw new UnsupportedOperationException("No direct add! Use xy.withOutgoing(obj)"); }};

   public static final String PROPERTY_outgoing = "outgoing";

   private java.util.ArrayList<Flow> outgoing = null;

   public java.util.ArrayList<Flow> getOutgoing()
   {
      if (this.outgoing == null)
      {
         return EMPTY_outgoing;
      }

      return this.outgoing;
   }

   public Task withOutgoing(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withOutgoing(i);
            }
         }
         else if (item instanceof Flow)
         {
            if (this.outgoing == null)
            {
               this.outgoing = new java.util.ArrayList<Flow>();
            }
            if ( ! this.outgoing.contains(item))
            {
               this.outgoing.add((Flow)item);
               ((Flow)item).setSource(this);
               firePropertyChange("outgoing", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Task withoutOutgoing(Object... value)
   {
      if (this.outgoing == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutOutgoing(i);
            }
         }
         else if (item instanceof Flow)
         {
            if (this.outgoing.contains(item))
            {
               this.outgoing.remove((Flow)item);
               ((Flow)item).setSource(null);
               firePropertyChange("outgoing", item, null);
            }
         }
      }
      return this;
   }

   public static final java.util.ArrayList<Flow> EMPTY_incomming = new java.util.ArrayList<Flow>()
   { @Override public boolean add(Flow value){ throw new UnsupportedOperationException("No direct add! Use xy.withIncomming(obj)"); }};

   public static final String PROPERTY_incomming = "incomming";

   private java.util.ArrayList<Flow> incomming = null;

   public java.util.ArrayList<Flow> getIncomming()
   {
      if (this.incomming == null)
      {
         return EMPTY_incomming;
      }

      return this.incomming;
   }

   public Task withIncomming(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withIncomming(i);
            }
         }
         else if (item instanceof Flow)
         {
            if (this.incomming == null)
            {
               this.incomming = new java.util.ArrayList<Flow>();
            }
            if ( ! this.incomming.contains(item))
            {
               this.incomming.add((Flow)item);
               ((Flow)item).setTarget(this);
               firePropertyChange("incomming", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Task withoutIncomming(Object... value)
   {
      if (this.incomming == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutIncomming(i);
            }
         }
         else if (item instanceof Flow)
         {
            if (this.incomming.contains(item))
            {
               this.incomming.remove((Flow)item);
               ((Flow)item).setTarget(null);
               firePropertyChange("incomming", item, null);
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

      this.withoutOutgoing(this.getOutgoing().clone());


      this.withoutIncomming(this.getIncomming().clone());


      this.withoutKids(this.getKids().clone());


   }

   public static final java.util.ArrayList<Task> EMPTY_kids = new java.util.ArrayList<Task>()
   { @Override public boolean add(Task value){ throw new UnsupportedOperationException("No direct add! Use xy.withKids(obj)"); }};

   public static final String PROPERTY_kids = "kids";

   private java.util.ArrayList<Task> kids = null;

   public java.util.ArrayList<Task> getKids()
   {
      if (this.kids == null)
      {
         return EMPTY_kids;
      }

      return this.kids;
   }

   public Task withKids(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withKids(i);
            }
         }
         else if (item instanceof Task)
         {
            if (this.kids == null)
            {
               this.kids = new java.util.ArrayList<Task>();
            }
            if ( ! this.kids.contains(item))
            {
               this.kids.add((Task)item);
               ((Task)item).setParent(this);
               firePropertyChange("kids", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Task withoutKids(Object... value)
   {
      if (this.kids == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutKids(i);
            }
         }
         else if (item instanceof Task)
         {
            if (this.kids.contains(item))
            {
               this.kids.remove((Task)item);
               ((Task)item).setParent(null);
               firePropertyChange("kids", item, null);
            }
         }
      }
      return this;
   }

   public static final String PROPERTY_parent = "parent";

   private Task parent = null;

   public Task getParent()
   {
      return this.parent;
   }

   public Task setParent(Task value)
   {
      if (this.parent != value)
      {
         Task oldValue = this.parent;
         if (this.parent != null)
         {
            this.parent = null;
            oldValue.withoutKids(this);
         }
         this.parent = value;
         if (value != null)
         {
            value.withKids(this);
         }
         firePropertyChange("parent", oldValue, value);
      }
      return this;
   }

}
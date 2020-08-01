package de.hub.mse.ttc2020.solution.M2;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class PatternObject  
{

   public static final String PROPERTY_poId = "poId";

   private String poId;

   public String getPoId()
   {
      return poId;
   }

   public PatternObject setPoId(String value)
   {
      if (value == null ? this.poId != null : ! value.equals(this.poId))
      {
         String oldValue = this.poId;
         this.poId = value;
         firePropertyChange("poId", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_handleObjectClass = "handleObjectClass";

   private Class handleObjectClass;

   public Class getHandleObjectClass()
   {
      return handleObjectClass;
   }

   public PatternObject setHandleObjectClass(Class value)
   {
      if (value != this.handleObjectClass)
      {
         Class oldValue = this.handleObjectClass;
         this.handleObjectClass = value;
         firePropertyChange("handleObjectClass", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_handleObject = "handleObject";

   private Object handleObject;

   public Object getHandleObject()
   {
      return handleObject;
   }

   public PatternObject setHandleObject(Object value)
   {
      if (value != this.handleObject)
      {
         Object oldValue = this.handleObject;
         this.handleObject = value;
         firePropertyChange("handleObject", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_kind = "kind";

   private String kind;

   public String getKind()
   {
      return kind;
   }

   public PatternObject setKind(String value)
   {
      if (value == null ? this.kind != null : ! value.equals(this.kind))
      {
         String oldValue = this.kind;
         this.kind = value;
         firePropertyChange("kind", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_pattern = "pattern";

   private Pattern pattern = null;

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public PatternObject setPattern(Pattern value)
   {
      if (this.pattern != value)
      {
         Pattern oldValue = this.pattern;
         if (this.pattern != null)
         {
            this.pattern = null;
            oldValue.withoutObjects(this);
         }
         this.pattern = value;
         if (value != null)
         {
            value.withObjects(this);
         }
         firePropertyChange("pattern", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<PatternAttribute> EMPTY_attributes = new java.util.ArrayList<PatternAttribute>()
   { @Override public boolean add(PatternAttribute value){ throw new UnsupportedOperationException("No direct add! Use xy.withAttributes(obj)"); }};

   public static final String PROPERTY_attributes = "attributes";

   private java.util.ArrayList<PatternAttribute> attributes = null;

   public java.util.ArrayList<PatternAttribute> getAttributes()
   {
      if (this.attributes == null)
      {
         return EMPTY_attributes;
      }

      return this.attributes;
   }

   public PatternObject withAttributes(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withAttributes(i);
            }
         }
         else if (item instanceof PatternAttribute)
         {
            if (this.attributes == null)
            {
               this.attributes = new java.util.ArrayList<PatternAttribute>();
            }
            if ( ! this.attributes.contains(item))
            {
               this.attributes.add((PatternAttribute)item);
               ((PatternAttribute)item).setObject(this);
               firePropertyChange("attributes", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public PatternObject withoutAttributes(Object... value)
   {
      if (this.attributes == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutAttributes(i);
            }
         }
         else if (item instanceof PatternAttribute)
         {
            if (this.attributes.contains(item))
            {
               this.attributes.remove((PatternAttribute)item);
               ((PatternAttribute)item).setObject(null);
               firePropertyChange("attributes", item, null);
            }
         }
      }
      return this;
   }

   public static final java.util.ArrayList<PatternLink> EMPTY_links = new java.util.ArrayList<PatternLink>()
   { @Override public boolean add(PatternLink value){ throw new UnsupportedOperationException("No direct add! Use xy.withLinks(obj)"); }};

   public static final String PROPERTY_links = "links";

   private java.util.ArrayList<PatternLink> links = null;

   public java.util.ArrayList<PatternLink> getLinks()
   {
      if (this.links == null)
      {
         return EMPTY_links;
      }

      return this.links;
   }

   public PatternObject withLinks(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withLinks(i);
            }
         }
         else if (item instanceof PatternLink)
         {
            if (this.links == null)
            {
               this.links = new java.util.ArrayList<PatternLink>();
            }
            if ( ! this.links.contains(item))
            {
               this.links.add((PatternLink)item);
               ((PatternLink)item).setSource(this);
               firePropertyChange("links", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public PatternObject withoutLinks(Object... value)
   {
      if (this.links == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutLinks(i);
            }
         }
         else if (item instanceof PatternLink)
         {
            if (this.links.contains(item))
            {
               this.links.remove((PatternLink)item);
               ((PatternLink)item).setSource(null);
               firePropertyChange("links", item, null);
            }
         }
      }
      return this;
   }

   public static final java.util.ArrayList<PatternLink> EMPTY_incommingLinks = new java.util.ArrayList<PatternLink>()
   { @Override public boolean add(PatternLink value){ throw new UnsupportedOperationException("No direct add! Use xy.withIncommingLinks(obj)"); }};

   public static final String PROPERTY_incommingLinks = "incommingLinks";

   private java.util.ArrayList<PatternLink> incommingLinks = null;

   public java.util.ArrayList<PatternLink> getIncommingLinks()
   {
      if (this.incommingLinks == null)
      {
         return EMPTY_incommingLinks;
      }

      return this.incommingLinks;
   }

   public PatternObject withIncommingLinks(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withIncommingLinks(i);
            }
         }
         else if (item instanceof PatternLink)
         {
            if (this.incommingLinks == null)
            {
               this.incommingLinks = new java.util.ArrayList<PatternLink>();
            }
            if ( ! this.incommingLinks.contains(item))
            {
               this.incommingLinks.add((PatternLink)item);
               ((PatternLink)item).setTarget(this);
               firePropertyChange("incommingLinks", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public PatternObject withoutIncommingLinks(Object... value)
   {
      if (this.incommingLinks == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutIncommingLinks(i);
            }
         }
         else if (item instanceof PatternLink)
         {
            if (this.incommingLinks.contains(item))
            {
               this.incommingLinks.remove((PatternLink)item);
               ((PatternLink)item).setTarget(null);
               firePropertyChange("incommingLinks", item, null);
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

      result.append(" ").append(this.getPoId());
      result.append(" ").append(this.getKind());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setPattern(null);

      this.withoutAttributes(this.getAttributes().clone());


      this.withoutLinks(this.getLinks().clone());


      this.withoutIncommingLinks(this.getIncommingLinks().clone());


   }

}
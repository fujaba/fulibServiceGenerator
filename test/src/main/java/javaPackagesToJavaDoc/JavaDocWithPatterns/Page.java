package javaPackagesToJavaDoc.JavaDocWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;

public class Page
{

   public static final String PROPERTY_app = "app";

   private JavaDocWithPatternsApp app;

   public static final java.util.ArrayList<Line> EMPTY_content = new java.util.ArrayList<Line>()
   { @Override public boolean add(Line value){ throw new UnsupportedOperationException("No direct add! Use xy.withContent(obj)"); }};

   public static final String PROPERTY_content = "content";

   private List<Line> content;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_description = "description";
   private String description;

   public JavaDocWithPatternsApp getApp()
   {
      return this.app;
   }

   public Page setApp(JavaDocWithPatternsApp value)
   {
      if (this.app == value)
      {
         return this;
      }

      final JavaDocWithPatternsApp oldValue = this.app;
      if (this.app != null)
      {
         this.app = null;
         oldValue.setContent(null);
      }
      this.app = value;
      if (value != null)
      {
         value.setContent(this);
      }
      this.firePropertyChange(PROPERTY_app, oldValue, value);
      return this;
   }

   public List<Line> getContent()
   {
      return this.content != null ? Collections.unmodifiableList(this.content) : Collections.emptyList();
   }

   public Page withContent(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withContent(i);
            }
         }
         else if (item instanceof Line)
         {
            if (this.content == null)
            {
               this.content = new java.util.ArrayList<Line>();
            }
            if ( ! this.content.contains(item))
            {
               this.content.add((Line)item);
               ((Line)item).setPage(this);
               firePropertyChange("content", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Page withoutContent(Object... value)
   {
      if (this.content == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutContent(i);
            }
         }
         else if (item instanceof Line)
         {
            if (this.content.contains(item))
            {
               this.content.remove((Line)item);
               ((Line)item).setPage(null);
               firePropertyChange("content", item, null);
            }
         }
      }
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getDescription());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setApp(null);
      this.withoutContent(new ArrayList<>(this.getContent()));
   }

public Page withContent(Line value)
   {
      if (this.content == null)
      {
         this.content = new ArrayList<>();
      }
      if (!this.content.contains(value))
      {
         this.content.add(value);
         value.setPage(this);
         this.firePropertyChange(PROPERTY_content, null, value);
      }
      return this;
   }

public Page withContent(Line... value)
   {
      for (final Line item : value)
      {
         this.withContent(item);
      }
      return this;
   }

public Page withContent(Collection<? extends Line> value)
   {
      for (final Line item : value)
      {
         this.withContent(item);
      }
      return this;
   }

public Page withoutContent(Line value)
   {
      if (this.content != null && this.content.remove(value))
      {
         value.setPage(null);
         this.firePropertyChange(PROPERTY_content, value, null);
      }
      return this;
   }

public Page withoutContent(Line... value)
   {
      for (final Line item : value)
      {
         this.withoutContent(item);
      }
      return this;
   }

public Page withoutContent(Collection<? extends Line> value)
   {
      for (final Line item : value)
      {
         this.withoutContent(item);
      }
      return this;
   }

   public String getId()
   {
      return this.id;
   }

   public Page setId(String value)
   {
      if (Objects.equals(value, this.id))
      {
         return this;
      }

      final String oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_id, oldValue, value);
      return this;
   }

   public String getDescription()
   {
      return this.description;
   }

   public Page setDescription(String value)
   {
      if (Objects.equals(value, this.description))
      {
         return this;
      }

      final String oldValue = this.description;
      this.description = value;
      this.firePropertyChange(PROPERTY_description, oldValue, value);
      return this;
   }

}
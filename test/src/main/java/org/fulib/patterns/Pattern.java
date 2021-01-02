package org.fulib.patterns;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Pattern
{
   public static final String PROPERTY_lparent = "lparent";
   private TGGRule lparent;
   public static final String PROPERTY_rparent = "rparent";
   private TGGRule rparent;
   public static final String PROPERTY_objects = "objects";
   private List<PatternObject> objects;
   protected PropertyChangeSupport listeners;

   public TGGRule getLparent()
   {
      return this.lparent;
   }

   public Pattern setLparent(TGGRule value)
   {
      if (this.lparent == value)
      {
         return this;
      }

      final TGGRule oldValue = this.lparent;
      if (this.lparent != null)
      {
         this.lparent = null;
         oldValue.setLeft(null);
      }
      this.lparent = value;
      if (value != null)
      {
         value.setLeft(this);
      }
      this.firePropertyChange(PROPERTY_lparent, oldValue, value);
      return this;
   }

   public TGGRule getRparent()
   {
      return this.rparent;
   }

   public Pattern setRparent(TGGRule value)
   {
      if (this.rparent == value)
      {
         return this;
      }

      final TGGRule oldValue = this.rparent;
      if (this.rparent != null)
      {
         this.rparent = null;
         oldValue.setRight(null);
      }
      this.rparent = value;
      if (value != null)
      {
         value.setRight(this);
      }
      this.firePropertyChange(PROPERTY_rparent, oldValue, value);
      return this;
   }

   public List<PatternObject> getObjects()
   {
      return this.objects != null ? Collections.unmodifiableList(this.objects) : Collections.emptyList();
   }

   public Pattern withObjects(PatternObject value)
   {
      if (this.objects == null)
      {
         this.objects = new ArrayList<>();
      }
      if (!this.objects.contains(value))
      {
         this.objects.add(value);
         value.setPattern(this);
         this.firePropertyChange(PROPERTY_objects, null, value);
      }
      return this;
   }

   public Pattern withObjects(PatternObject... value)
   {
      for (final PatternObject item : value)
      {
         this.withObjects(item);
      }
      return this;
   }

   public Pattern withObjects(Collection<? extends PatternObject> value)
   {
      for (final PatternObject item : value)
      {
         this.withObjects(item);
      }
      return this;
   }

   public Pattern withoutObjects(PatternObject value)
   {
      if (this.objects != null && this.objects.remove(value))
      {
         value.setPattern(null);
         this.firePropertyChange(PROPERTY_objects, value, null);
      }
      return this;
   }

   public Pattern withoutObjects(PatternObject... value)
   {
      for (final PatternObject item : value)
      {
         this.withoutObjects(item);
      }
      return this;
   }

   public Pattern withoutObjects(Collection<? extends PatternObject> value)
   {
      for (final PatternObject item : value)
      {
         this.withoutObjects(item);
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

   public void removeYou()
   {
      this.setLparent(null);
      this.setRparent(null);
      this.withoutObjects(new ArrayList<>(this.getObjects()));
   }
}

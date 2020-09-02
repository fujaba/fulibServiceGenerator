package de.hub.mse.ttc2020.solution.M1;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class Pattern
{

   public static final String PROPERTY_objects = "objects";

   private List<PatternObject> objects;

   protected PropertyChangeSupport listeners;

   public List<PatternObject> getObjects()
   {
      return this.objects != null ? Collections.unmodifiableList(this.objects) : Collections.emptyList();
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
      this.withoutObjects(new ArrayList<>(this.getObjects()));
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

}

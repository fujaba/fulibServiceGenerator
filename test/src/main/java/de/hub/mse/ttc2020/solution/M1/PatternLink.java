package de.hub.mse.ttc2020.solution.M1;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class PatternLink
{

   public static final String PROPERTY_source = "source";

   private PatternObject source;

   public static final String PROPERTY_target = "target";

   private PatternObject target;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_handleLinkName = "handleLinkName";
   private String handleLinkName;
   public static final String PROPERTY_kind = "kind";
   private String kind = "core";

   public PatternObject getSource()
   {
      return this.source;
   }

   public PatternLink setSource(PatternObject value)
   {
      if (this.source == value)
      {
         return this;
      }

      final PatternObject oldValue = this.source;
      if (this.source != null)
      {
         this.source = null;
         oldValue.withoutLinks(this);
      }
      this.source = value;
      if (value != null)
      {
         value.withLinks(this);
      }
      this.firePropertyChange(PROPERTY_source, oldValue, value);
      return this;
   }

   public PatternObject getTarget()
   {
      return this.target;
   }

   public PatternLink setTarget(PatternObject value)
   {
      if (this.target == value)
      {
         return this;
      }

      final PatternObject oldValue = this.target;
      if (this.target != null)
      {
         this.target = null;
         oldValue.withoutIncommingLinks(this);
      }
      this.target = value;
      if (value != null)
      {
         value.withIncommingLinks(this);
      }
      this.firePropertyChange(PROPERTY_target, oldValue, value);
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
      result.append(' ').append(this.getHandleLinkName());
      result.append(' ').append(this.getKind());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setSource(null);
      this.setTarget(null);
   }

   public String getHandleLinkName()
   {
      return this.handleLinkName;
   }

   public PatternLink setHandleLinkName(String value)
   {
      if (Objects.equals(value, this.handleLinkName))
      {
         return this;
      }

      final String oldValue = this.handleLinkName;
      this.handleLinkName = value;
      this.firePropertyChange(PROPERTY_handleLinkName, oldValue, value);
      return this;
   }

   public String getKind()
   {
      return this.kind;
   }

   public PatternLink setKind(String value)
   {
      if (Objects.equals(value, this.kind))
      {
         return this;
      }

      final String oldValue = this.kind;
      this.kind = value;
      this.firePropertyChange(PROPERTY_kind, oldValue, value);
      return this;
   }

}
package de.hub.mse.ttc2020.solution.M1;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class PatternLink  
{

   public static final String PROPERTY_handleLinkName = "handleLinkName";

   private String handleLinkName;

   public String getHandleLinkName()
   {
      return handleLinkName;
   }

   public PatternLink setHandleLinkName(String value)
   {
      if (value == null ? this.handleLinkName != null : ! value.equals(this.handleLinkName))
      {
         String oldValue = this.handleLinkName;
         this.handleLinkName = value;
         firePropertyChange("handleLinkName", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_source = "source";

   private PatternObject source = null;

   public PatternObject getSource()
   {
      return this.source;
   }

   public PatternLink setSource(PatternObject value)
   {
      if (this.source != value)
      {
         PatternObject oldValue = this.source;
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
         firePropertyChange("source", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_target = "target";

   private PatternObject target = null;

   public PatternObject getTarget()
   {
      return this.target;
   }

   public PatternLink setTarget(PatternObject value)
   {
      if (this.target != value)
      {
         PatternObject oldValue = this.target;
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

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getHandleLinkName());
      result.append(" ").append(this.getKind());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setSource(null);
      this.setTarget(null);

   }

   public static final String PROPERTY_kind = "kind";

   private String kind = "core";

   public String getKind()
   {
      return kind;
   }

   public PatternLink setKind(String value)
   {
      if (value == null ? this.kind != null : ! value.equals(this.kind))
      {
         String oldValue = this.kind;
         this.kind = value;
         firePropertyChange("kind", oldValue, value);
      }
      return this;
   }

}
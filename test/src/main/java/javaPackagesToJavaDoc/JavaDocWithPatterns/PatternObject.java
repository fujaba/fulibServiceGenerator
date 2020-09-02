package javaPackagesToJavaDoc.JavaDocWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;

public class PatternObject
{

   public static final String PROPERTY_pattern = "pattern";

   private Pattern pattern;

   public static final String PROPERTY_attributes = "attributes";

   private List<PatternAttribute> attributes;

   public static final String PROPERTY_links = "links";

   private List<PatternLink> links;

   public static final String PROPERTY_incommingLinks = "incommingLinks";

   private List<PatternLink> incommingLinks;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_poId = "poId";
   private String poId;
   public static final String PROPERTY_handleObject = "handleObject";
   private Object handleObject;
   public static final String PROPERTY_kind = "kind";
   private String kind;
   public static final String PROPERTY_handleObjectClass = "handleObjectClass";
   private Class<?> handleObjectClass;

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public PatternObject setPattern(Pattern value)
   {
      if (this.pattern == value)
      {
         return this;
      }

      final Pattern oldValue = this.pattern;
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
      this.firePropertyChange(PROPERTY_pattern, oldValue, value);
      return this;
   }

   public List<PatternAttribute> getAttributes()
   {
      return this.attributes != null ? Collections.unmodifiableList(this.attributes) : Collections.emptyList();
   }

   public List<PatternLink> getLinks()
   {
      return this.links != null ? Collections.unmodifiableList(this.links) : Collections.emptyList();
   }

   public List<PatternLink> getIncommingLinks()
   {
      return this.incommingLinks != null ? Collections.unmodifiableList(this.incommingLinks) : Collections.emptyList();
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
      result.append(' ').append(this.getPoId());
      result.append(' ').append(this.getKind());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setPattern(null);
      this.withoutAttributes(new ArrayList<>(this.getAttributes()));
      this.withoutLinks(new ArrayList<>(this.getLinks()));
      this.withoutIncommingLinks(new ArrayList<>(this.getIncommingLinks()));
   }

public PatternObject withAttributes(PatternAttribute value)
   {
      if (this.attributes == null)
      {
         this.attributes = new ArrayList<>();
      }
      if (!this.attributes.contains(value))
      {
         this.attributes.add(value);
         value.setObject(this);
         this.firePropertyChange(PROPERTY_attributes, null, value);
      }
      return this;
   }

public PatternObject withAttributes(PatternAttribute... value)
   {
      for (final PatternAttribute item : value)
      {
         this.withAttributes(item);
      }
      return this;
   }

public PatternObject withAttributes(Collection<? extends PatternAttribute> value)
   {
      for (final PatternAttribute item : value)
      {
         this.withAttributes(item);
      }
      return this;
   }

public PatternObject withoutAttributes(PatternAttribute value)
   {
      if (this.attributes != null && this.attributes.remove(value))
      {
         value.setObject(null);
         this.firePropertyChange(PROPERTY_attributes, value, null);
      }
      return this;
   }

public PatternObject withoutAttributes(PatternAttribute... value)
   {
      for (final PatternAttribute item : value)
      {
         this.withoutAttributes(item);
      }
      return this;
   }

public PatternObject withoutAttributes(Collection<? extends PatternAttribute> value)
   {
      for (final PatternAttribute item : value)
      {
         this.withoutAttributes(item);
      }
      return this;
   }

public PatternObject withLinks(PatternLink value)
   {
      if (this.links == null)
      {
         this.links = new ArrayList<>();
      }
      if (!this.links.contains(value))
      {
         this.links.add(value);
         value.setSource(this);
         this.firePropertyChange(PROPERTY_links, null, value);
      }
      return this;
   }

public PatternObject withLinks(PatternLink... value)
   {
      for (final PatternLink item : value)
      {
         this.withLinks(item);
      }
      return this;
   }

public PatternObject withLinks(Collection<? extends PatternLink> value)
   {
      for (final PatternLink item : value)
      {
         this.withLinks(item);
      }
      return this;
   }

public PatternObject withoutLinks(PatternLink value)
   {
      if (this.links != null && this.links.remove(value))
      {
         value.setSource(null);
         this.firePropertyChange(PROPERTY_links, value, null);
      }
      return this;
   }

public PatternObject withoutLinks(PatternLink... value)
   {
      for (final PatternLink item : value)
      {
         this.withoutLinks(item);
      }
      return this;
   }

public PatternObject withoutLinks(Collection<? extends PatternLink> value)
   {
      for (final PatternLink item : value)
      {
         this.withoutLinks(item);
      }
      return this;
   }

public PatternObject withIncommingLinks(PatternLink value)
   {
      if (this.incommingLinks == null)
      {
         this.incommingLinks = new ArrayList<>();
      }
      if (!this.incommingLinks.contains(value))
      {
         this.incommingLinks.add(value);
         value.setTarget(this);
         this.firePropertyChange(PROPERTY_incommingLinks, null, value);
      }
      return this;
   }

public PatternObject withIncommingLinks(PatternLink... value)
   {
      for (final PatternLink item : value)
      {
         this.withIncommingLinks(item);
      }
      return this;
   }

public PatternObject withIncommingLinks(Collection<? extends PatternLink> value)
   {
      for (final PatternLink item : value)
      {
         this.withIncommingLinks(item);
      }
      return this;
   }

public PatternObject withoutIncommingLinks(PatternLink value)
   {
      if (this.incommingLinks != null && this.incommingLinks.remove(value))
      {
         value.setTarget(null);
         this.firePropertyChange(PROPERTY_incommingLinks, value, null);
      }
      return this;
   }

public PatternObject withoutIncommingLinks(PatternLink... value)
   {
      for (final PatternLink item : value)
      {
         this.withoutIncommingLinks(item);
      }
      return this;
   }

public PatternObject withoutIncommingLinks(Collection<? extends PatternLink> value)
   {
      for (final PatternLink item : value)
      {
         this.withoutIncommingLinks(item);
      }
      return this;
   }

   public String getPoId()
   {
      return this.poId;
   }

   public PatternObject setPoId(String value)
   {
      if (Objects.equals(value, this.poId))
      {
         return this;
      }

      final String oldValue = this.poId;
      this.poId = value;
      this.firePropertyChange(PROPERTY_poId, oldValue, value);
      return this;
   }

   public Object getHandleObject()
   {
      return this.handleObject;
   }

   public PatternObject setHandleObject(Object value)
   {
      if (Objects.equals(value, this.handleObject))
      {
         return this;
      }

      final Object oldValue = this.handleObject;
      this.handleObject = value;
      this.firePropertyChange(PROPERTY_handleObject, oldValue, value);
      return this;
   }

   public String getKind()
   {
      return this.kind;
   }

   public PatternObject setKind(String value)
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

   public Class<?> getHandleObjectClass()
   {
      return this.handleObjectClass;
   }

   public PatternObject setHandleObjectClass(Class<?> value)
   {
      if (Objects.equals(value, this.handleObjectClass))
      {
         return this;
      }

      final Class<?> oldValue = this.handleObjectClass;
      this.handleObjectClass = value;
      this.firePropertyChange(PROPERTY_handleObjectClass, oldValue, value);
      return this;
   }

}

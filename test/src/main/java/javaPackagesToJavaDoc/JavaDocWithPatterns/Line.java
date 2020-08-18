package javaPackagesToJavaDoc.JavaDocWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class Line
{

   public static final String PROPERTY_page = "page";

   private Page page;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_description = "description";
   private String description;
   public static final String PROPERTY_action = "action";
   private String action;
   public static final String PROPERTY_value = "value";
   private String value;

   public Page getPage()
   {
      return this.page;
   }

   public Line setPage(Page value)
   {
      if (this.page == value)
      {
         return this;
      }

      final Page oldValue = this.page;
      if (this.page != null)
      {
         this.page = null;
         oldValue.withoutContent(this);
      }
      this.page = value;
      if (value != null)
      {
         value.withContent(this);
      }
      this.firePropertyChange(PROPERTY_page, oldValue, value);
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
      result.append(' ').append(this.getAction());
      result.append(' ').append(this.getValue());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setPage(null);
   }

   public String getId()
   {
      return this.id;
   }

   public Line setId(String value)
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

   public Line setDescription(String value)
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

   public String getAction()
   {
      return this.action;
   }

   public Line setAction(String value)
   {
      if (Objects.equals(value, this.action))
      {
         return this;
      }

      final String oldValue = this.action;
      this.action = value;
      this.firePropertyChange(PROPERTY_action, oldValue, value);
      return this;
   }

   public String getValue()
   {
      return this.value;
   }

   public Line setValue(String value)
   {
      if (Objects.equals(value, this.value))
      {
         return this;
      }

      final String oldValue = this.value;
      this.value = value;
      this.firePropertyChange(PROPERTY_value, oldValue, value);
      return this;
   }

}
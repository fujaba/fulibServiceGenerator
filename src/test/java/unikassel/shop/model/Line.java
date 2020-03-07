package unikassel.shop.model;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Line  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Line setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_description = "description";

   private String description;

   public String getDescription()
   {
      return description;
   }

   public Line setDescription(String value)
   {
      if (value == null ? this.description != null : ! value.equals(this.description))
      {
         String oldValue = this.description;
         this.description = value;
         firePropertyChange("description", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_action = "action";

   private String action;

   public String getAction()
   {
      return action;
   }

   public Line setAction(String value)
   {
      if (value == null ? this.action != null : ! value.equals(this.action))
      {
         String oldValue = this.action;
         this.action = value;
         firePropertyChange("action", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_page = "page";

   private Page page = null;

   public Page getPage()
   {
      return this.page;
   }

   public Line setPage(Page value)
   {
      if (this.page != value)
      {
         Page oldValue = this.page;
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
         firePropertyChange("page", oldValue, value);
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
      result.append(" ").append(this.getDescription());
      result.append(" ").append(this.getAction());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setPage(null);

   }

}
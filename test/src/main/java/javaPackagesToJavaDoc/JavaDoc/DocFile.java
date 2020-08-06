package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class DocFile  
{

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

   public void removeYou()
   {
      this.setUp(null);

   }

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public DocFile setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_content = "content";

   private String content;

   public String getContent()
   {
      return content;
   }

   public DocFile setContent(String value)
   {
      if (value == null ? this.content != null : ! value.equals(this.content))
      {
         String oldValue = this.content;
         this.content = value;
         firePropertyChange("content", oldValue, value);
      }
      return this;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getId());
      result.append(" ").append(this.getVersion());
      result.append(" ").append(this.getContent());


      return result.substring(1);
   }

   public static final String PROPERTY_up = "up";

   private Folder up = null;

   public Folder getUp()
   {
      return this.up;
   }

   public DocFile setUp(Folder value)
   {
      if (this.up != value)
      {
         Folder oldValue = this.up;
         if (this.up != null)
         {
            this.up = null;
            oldValue.withoutFiles(this);
         }
         this.up = value;
         if (value != null)
         {
            value.withFiles(this);
         }
         firePropertyChange("up", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_version = "version";

   private String version;

   public String getVersion()
   {
      return version;
   }

   public DocFile setVersion(String value)
   {
      if (value == null ? this.version != null : ! value.equals(this.version))
      {
         String oldValue = this.version;
         this.version = value;
         firePropertyChange("version", oldValue, value);
      }
      return this;
   }

}
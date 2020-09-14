package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class DocFile
{

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_version = "version";
   private String version;
   public static final String PROPERTY_content = "content";
   private String content;
   public static final String PROPERTY_folder = "folder";
   private Folder folder;

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
      this.setFolder(null);
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getVersion());
      result.append(' ').append(this.getContent());
      return result.substring(1);
   }

   public String getId()
   {
      return this.id;
   }

   public DocFile setId(String value)
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

   public String getVersion()
   {
      return this.version;
   }

   public DocFile setVersion(String value)
   {
      if (Objects.equals(value, this.version))
      {
         return this;
      }

      final String oldValue = this.version;
      this.version = value;
      this.firePropertyChange(PROPERTY_version, oldValue, value);
      return this;
   }

   public String getContent()
   {
      return this.content;
   }

   public DocFile setContent(String value)
   {
      if (Objects.equals(value, this.content))
      {
         return this;
      }

      final String oldValue = this.content;
      this.content = value;
      this.firePropertyChange(PROPERTY_content, oldValue, value);
      return this;
   }

   public Folder getFolder()
   {
      return this.folder;
   }

   public DocFile setFolder(Folder value)
   {
      if (this.folder == value)
      {
         return this;
      }

      final Folder oldValue = this.folder;
      if (this.folder != null)
      {
         this.folder = null;
         oldValue.withoutFiles(this);
      }
      this.folder = value;
      if (value != null)
      {
         value.withFiles(this);
      }
      this.firePropertyChange(PROPERTY_folder, oldValue, value);
      return this;
   }

}
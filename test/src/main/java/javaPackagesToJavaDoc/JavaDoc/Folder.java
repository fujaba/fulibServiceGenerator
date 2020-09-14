package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;

public class Folder
{

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_subFolders = "subFolders";
   private List<Folder> subFolders;
   public static final String PROPERTY_pFolder = "pFolder";
   private Folder pFolder;
   public static final String PROPERTY_files = "files";
   private List<DocFile> files;

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
      this.withoutSubFolders(new ArrayList<>(this.getSubFolders()));
      this.setPFolder(null);
      this.withoutFiles(new ArrayList<>(this.getFiles()));
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getId());
      return result.substring(1);
   }

   public DocFile getFromFiles(String fileId)
   {
      for (DocFile file : getFiles()) {
         if (file.getId().equals(fileId)) {
            return file;
         }
      }
      return null;
   }

   public String getId()
   {
      return this.id;
   }

   public Folder setId(String value)
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

   public List<Folder> getSubFolders()
   {
      return this.subFolders != null ? Collections.unmodifiableList(this.subFolders) : Collections.emptyList();
   }

   public Folder withSubFolders(Folder value)
   {
      if (this.subFolders == null)
      {
         this.subFolders = new ArrayList<>();
      }
      if (!this.subFolders.contains(value))
      {
         this.subFolders.add(value);
         value.setPFolder(this);
         this.firePropertyChange(PROPERTY_subFolders, null, value);
      }
      return this;
   }

   public Folder withSubFolders(Folder... value)
   {
      for (final Folder item : value)
      {
         this.withSubFolders(item);
      }
      return this;
   }

   public Folder withSubFolders(Collection<? extends Folder> value)
   {
      for (final Folder item : value)
      {
         this.withSubFolders(item);
      }
      return this;
   }

   public Folder withoutSubFolders(Folder value)
   {
      if (this.subFolders != null && this.subFolders.remove(value))
      {
         value.setPFolder(null);
         this.firePropertyChange(PROPERTY_subFolders, value, null);
      }
      return this;
   }

   public Folder withoutSubFolders(Folder... value)
   {
      for (final Folder item : value)
      {
         this.withoutSubFolders(item);
      }
      return this;
   }

   public Folder withoutSubFolders(Collection<? extends Folder> value)
   {
      for (final Folder item : value)
      {
         this.withoutSubFolders(item);
      }
      return this;
   }

   public Folder getPFolder()
   {
      return this.pFolder;
   }

   public Folder setPFolder(Folder value)
   {
      if (this.pFolder == value)
      {
         return this;
      }

      final Folder oldValue = this.pFolder;
      if (this.pFolder != null)
      {
         this.pFolder = null;
         oldValue.withoutSubFolders(this);
      }
      this.pFolder = value;
      if (value != null)
      {
         value.withSubFolders(this);
      }
      this.firePropertyChange(PROPERTY_pFolder, oldValue, value);
      return this;
   }

   public List<DocFile> getFiles()
   {
      return this.files != null ? Collections.unmodifiableList(this.files) : Collections.emptyList();
   }

   public Folder withFiles(DocFile value)
   {
      if (this.files == null)
      {
         this.files = new ArrayList<>();
      }
      if (!this.files.contains(value))
      {
         this.files.add(value);
         value.setFolder(this);
         this.firePropertyChange(PROPERTY_files, null, value);
      }
      return this;
   }

   public Folder withFiles(DocFile... value)
   {
      for (final DocFile item : value)
      {
         this.withFiles(item);
      }
      return this;
   }

   public Folder withFiles(Collection<? extends DocFile> value)
   {
      for (final DocFile item : value)
      {
         this.withFiles(item);
      }
      return this;
   }

   public Folder withoutFiles(DocFile value)
   {
      if (this.files != null && this.files.remove(value))
      {
         value.setFolder(null);
         this.firePropertyChange(PROPERTY_files, value, null);
      }
      return this;
   }

   public Folder withoutFiles(DocFile... value)
   {
      for (final DocFile item : value)
      {
         this.withoutFiles(item);
      }
      return this;
   }

   public Folder withoutFiles(Collection<? extends DocFile> value)
   {
      for (final DocFile item : value)
      {
         this.withoutFiles(item);
      }
      return this;
   }

}

package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Folder  
{

   public static final java.util.ArrayList<Folder> EMPTY_subFolders = new java.util.ArrayList<Folder>()
   { @Override public boolean add(Folder value){ throw new UnsupportedOperationException("No direct add! Use xy.withSubFolders(obj)"); }};

   public static final String PROPERTY_subFolders = "subFolders";

   private java.util.ArrayList<Folder> subFolders = null;

   public java.util.ArrayList<Folder> getSubFolders()
   {
      if (this.subFolders == null)
      {
         return EMPTY_subFolders;
      }

      return this.subFolders;
   }

   public Folder withSubFolders(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withSubFolders(i);
            }
         }
         else if (item instanceof Folder)
         {
            if (this.subFolders == null)
            {
               this.subFolders = new java.util.ArrayList<Folder>();
            }
            if ( ! this.subFolders.contains(item))
            {
               this.subFolders.add((Folder)item);
               ((Folder)item).setUp(this);
               firePropertyChange("subFolders", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Folder withoutSubFolders(Object... value)
   {
      if (this.subFolders == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutSubFolders(i);
            }
         }
         else if (item instanceof Folder)
         {
            if (this.subFolders.contains(item))
            {
               this.subFolders.remove((Folder)item);
               ((Folder)item).setUp(null);
               firePropertyChange("subFolders", item, null);
            }
         }
      }
      return this;
   }

   public static final String PROPERTY_up = "up";

   private Folder up = null;

   public Folder getUp()
   {
      return this.up;
   }

   public Folder setUp(Folder value)
   {
      if (this.up != value)
      {
         Folder oldValue = this.up;
         if (this.up != null)
         {
            this.up = null;
            oldValue.withoutSubFolders(this);
         }
         this.up = value;
         if (value != null)
         {
            value.withSubFolders(this);
         }
         firePropertyChange("up", oldValue, value);
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

   public void removeYou()
   {
      this.setUp(null);

      this.withoutSubFolders(this.getSubFolders().clone());


      this.withoutFiles(this.getFiles().clone());


   }

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Folder setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getId());


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
   public static final java.util.ArrayList<DocFile> EMPTY_files = new java.util.ArrayList<DocFile>()
   { @Override public boolean add(DocFile value){ throw new UnsupportedOperationException("No direct add! Use xy.withFiles(obj)"); }};

   public static final String PROPERTY_files = "files";

   private java.util.ArrayList<DocFile> files = null;

   public java.util.ArrayList<DocFile> getFiles()
   {
      if (this.files == null)
      {
         return EMPTY_files;
      }

      return this.files;
   }

   public Folder withFiles(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withFiles(i);
            }
         }
         else if (item instanceof DocFile)
         {
            if (this.files == null)
            {
               this.files = new java.util.ArrayList<DocFile>();
            }
            if ( ! this.files.contains(item))
            {
               this.files.add((DocFile)item);
               ((DocFile)item).setUp(this);
               firePropertyChange("files", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Folder withoutFiles(Object... value)
   {
      if (this.files == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutFiles(i);
            }
         }
         else if (item instanceof DocFile)
         {
            if (this.files.contains(item))
            {
               this.files.remove((DocFile)item);
               ((DocFile)item).setUp(null);
               firePropertyChange("files", item, null);
            }
         }
      }
      return this;
   }

}

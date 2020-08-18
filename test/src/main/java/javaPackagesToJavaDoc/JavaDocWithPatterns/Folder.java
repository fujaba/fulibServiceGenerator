package javaPackagesToJavaDoc.JavaDocWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;

public class Folder
{

   public static final java.util.ArrayList<Folder> EMPTY_subFolders = new java.util.ArrayList<Folder>()
   { @Override public boolean add(Folder value){ throw new UnsupportedOperationException("No direct add! Use xy.withSubFolders(obj)"); }};

   public static final String PROPERTY_subFolders = "subFolders";

   private List<Folder> subFolders;

   public static final String PROPERTY_up = "up";

   private Folder up;

   public static final java.util.ArrayList<DocFile> EMPTY_files = new java.util.ArrayList<DocFile>()
   { @Override public boolean add(DocFile value){ throw new UnsupportedOperationException("No direct add! Use xy.withFiles(obj)"); }};

   public static final String PROPERTY_files = "files";

   private List<DocFile> files;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;

   public List<Folder> getSubFolders()
   {
      return this.subFolders != null ? Collections.unmodifiableList(this.subFolders) : Collections.emptyList();
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

   public Folder getUp()
   {
      return this.up;
   }

   public Folder setUp(Folder value)
   {
      if (this.up == value)
      {
         return this;
      }

      final Folder oldValue = this.up;
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
      this.firePropertyChange(PROPERTY_up, oldValue, value);
      return this;
   }

   public List<DocFile> getFiles()
   {
      return this.files != null ? Collections.unmodifiableList(this.files) : Collections.emptyList();
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
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutSubFolders(new ArrayList<>(this.getSubFolders()));
      this.setUp(null);
      this.withoutFiles(new ArrayList<>(this.getFiles()));
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
         value.setUp(this);
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
         value.setUp(null);
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

public Folder withFiles(DocFile value)
   {
      if (this.files == null)
      {
         this.files = new ArrayList<>();
      }
      if (!this.files.contains(value))
      {
         this.files.add(value);
         value.setUp(this);
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
         value.setUp(null);
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

}
package javaPackagesToJavaDoc.JavaPackagesWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;

public class JavaPackage
{

   public static final String PROPERTY_subPackages = "subPackages";

   private List<JavaPackage> subPackages;

   public static final String PROPERTY_up = "up";

   private JavaPackage up;

   public static final String PROPERTY_classes = "classes";

   private List<JavaClass> classes;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;

   public List<JavaPackage> getSubPackages()
   {
      return this.subPackages != null ? Collections.unmodifiableList(this.subPackages) : Collections.emptyList();
   }

   public JavaPackage withSubPackages(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withSubPackages(i);
            }
         }
         else if (item instanceof JavaPackage)
         {
            if (this.subPackages == null)
            {
               this.subPackages = new java.util.ArrayList<JavaPackage>();
            }
            if ( ! this.subPackages.contains(item))
            {
               this.subPackages.add((JavaPackage)item);
               ((JavaPackage)item).setUp(this);
               firePropertyChange("subPackages", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public JavaPackage withoutSubPackages(Object... value)
   {
      if (this.subPackages == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutSubPackages(i);
            }
         }
         else if (item instanceof JavaPackage)
         {
            if (this.subPackages.contains(item))
            {
               this.subPackages.remove((JavaPackage)item);
               ((JavaPackage)item).setUp(null);
               firePropertyChange("subPackages", item, null);
            }
         }
      }
      return this;
   }

   public JavaPackage getUp()
   {
      return this.up;
   }

   public JavaPackage setUp(JavaPackage value)
   {
      if (this.up == value)
      {
         return this;
      }

      final JavaPackage oldValue = this.up;
      if (this.up != null)
      {
         this.up = null;
         oldValue.withoutSubPackages(this);
      }
      this.up = value;
      if (value != null)
      {
         value.withSubPackages(this);
      }
      this.firePropertyChange(PROPERTY_up, oldValue, value);
      return this;
   }

   public List<JavaClass> getClasses()
   {
      return this.classes != null ? Collections.unmodifiableList(this.classes) : Collections.emptyList();
   }

   public JavaPackage withClasses(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withClasses(i);
            }
         }
         else if (item instanceof JavaClass)
         {
            if (this.classes == null)
            {
               this.classes = new java.util.ArrayList<JavaClass>();
            }
            if ( ! this.classes.contains(item))
            {
               this.classes.add((JavaClass)item);
               ((JavaClass)item).setUp(this);
               firePropertyChange("classes", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public JavaPackage withoutClasses(Object... value)
   {
      if (this.classes == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutClasses(i);
            }
         }
         else if (item instanceof JavaClass)
         {
            if (this.classes.contains(item))
            {
               this.classes.remove((JavaClass)item);
               ((JavaClass)item).setUp(null);
               firePropertyChange("classes", item, null);
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
      this.withoutSubPackages(new ArrayList<>(this.getSubPackages()));
      this.setUp(null);
      this.withoutClasses(new ArrayList<>(this.getClasses()));
   }

public JavaPackage withSubPackages(JavaPackage value)
   {
      if (this.subPackages == null)
      {
         this.subPackages = new ArrayList<>();
      }
      if (!this.subPackages.contains(value))
      {
         this.subPackages.add(value);
         value.setUp(this);
         this.firePropertyChange(PROPERTY_subPackages, null, value);
      }
      return this;
   }

public JavaPackage withSubPackages(JavaPackage... value)
   {
      for (final JavaPackage item : value)
      {
         this.withSubPackages(item);
      }
      return this;
   }

public JavaPackage withSubPackages(Collection<? extends JavaPackage> value)
   {
      for (final JavaPackage item : value)
      {
         this.withSubPackages(item);
      }
      return this;
   }

public JavaPackage withoutSubPackages(JavaPackage value)
   {
      if (this.subPackages != null && this.subPackages.remove(value))
      {
         value.setUp(null);
         this.firePropertyChange(PROPERTY_subPackages, value, null);
      }
      return this;
   }

public JavaPackage withoutSubPackages(JavaPackage... value)
   {
      for (final JavaPackage item : value)
      {
         this.withoutSubPackages(item);
      }
      return this;
   }

public JavaPackage withoutSubPackages(Collection<? extends JavaPackage> value)
   {
      for (final JavaPackage item : value)
      {
         this.withoutSubPackages(item);
      }
      return this;
   }

public JavaPackage withClasses(JavaClass value)
   {
      if (this.classes == null)
      {
         this.classes = new ArrayList<>();
      }
      if (!this.classes.contains(value))
      {
         this.classes.add(value);
         value.setUp(this);
         this.firePropertyChange(PROPERTY_classes, null, value);
      }
      return this;
   }

public JavaPackage withClasses(JavaClass... value)
   {
      for (final JavaClass item : value)
      {
         this.withClasses(item);
      }
      return this;
   }

public JavaPackage withClasses(Collection<? extends JavaClass> value)
   {
      for (final JavaClass item : value)
      {
         this.withClasses(item);
      }
      return this;
   }

public JavaPackage withoutClasses(JavaClass value)
   {
      if (this.classes != null && this.classes.remove(value))
      {
         value.setUp(null);
         this.firePropertyChange(PROPERTY_classes, value, null);
      }
      return this;
   }

public JavaPackage withoutClasses(JavaClass... value)
   {
      for (final JavaClass item : value)
      {
         this.withoutClasses(item);
      }
      return this;
   }

public JavaPackage withoutClasses(Collection<? extends JavaClass> value)
   {
      for (final JavaClass item : value)
      {
         this.withoutClasses(item);
      }
      return this;
   }

   public String getId()
   {
      return this.id;
   }

   public JavaPackage setId(String value)
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

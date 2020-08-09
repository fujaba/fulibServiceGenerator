package javaPackagesToJavaDoc.JavaPackagesWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class JavaPackage 
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public JavaPackage setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<JavaPackage> EMPTY_subPackages = new java.util.ArrayList<JavaPackage>()
   { @Override public boolean add(JavaPackage value){ throw new UnsupportedOperationException("No direct add! Use xy.withSubPackages(obj)"); }};

   public static final String PROPERTY_subPackages = "subPackages";

   private java.util.ArrayList<JavaPackage> subPackages = null;

   public java.util.ArrayList<JavaPackage> getSubPackages()
   {
      if (this.subPackages == null)
      {
         return EMPTY_subPackages;
      }

      return this.subPackages;
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

   public static final String PROPERTY_up = "up";

   private JavaPackage up = null;

   public JavaPackage getUp()
   {
      return this.up;
   }

   public JavaPackage setUp(JavaPackage value)
   {
      if (this.up != value)
      {
         JavaPackage oldValue = this.up;
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
         firePropertyChange("up", oldValue, value);
      }
      return this;
   }


   public static final java.util.ArrayList<JavaClass> EMPTY_classes = new java.util.ArrayList<JavaClass>()
   { @Override public boolean add(JavaClass value){ throw new UnsupportedOperationException("No direct add! Use xy.withClasses(obj)"); }};

   public static final String PROPERTY_classes = "classes";

   private java.util.ArrayList<JavaClass> classes = null;

   public java.util.ArrayList<JavaClass> getClasses()
   {
      if (this.classes == null)
      {
         return EMPTY_classes;
      }

      return this.classes;
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


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setUp(null);

      this.withoutSubPackages(this.getSubPackages().clone());


      this.withoutClasses(this.getClasses().clone());


   }

}
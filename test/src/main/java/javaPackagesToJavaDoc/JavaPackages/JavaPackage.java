package javaPackagesToJavaDoc.JavaPackages;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;

public class JavaPackage
{

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_subPackages = "subPackages";
   private List<JavaPackage> subPackages;
   public static final String PROPERTY_pPack = "pPack";
   private JavaPackage pPack;
   public static final String PROPERTY_classes = "classes";
   private List<JavaClass> classes;

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
      this.withoutSubPackages(new ArrayList<>(this.getSubPackages()));
      this.setPPack(null);
      this.withoutClasses(new ArrayList<>(this.getClasses()));
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getId());
      return result.substring(1);
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

   public List<JavaPackage> getSubPackages()
   {
      return this.subPackages != null ? Collections.unmodifiableList(this.subPackages) : Collections.emptyList();
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
         value.setPPack(this);
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
         value.setPPack(null);
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

   public JavaPackage getPPack()
   {
      return this.pPack;
   }

   public JavaPackage setPPack(JavaPackage value)
   {
      if (this.pPack == value)
      {
         return this;
      }

      final JavaPackage oldValue = this.pPack;
      if (this.pPack != null)
      {
         this.pPack = null;
         oldValue.withoutSubPackages(this);
      }
      this.pPack = value;
      if (value != null)
      {
         value.withSubPackages(this);
      }
      this.firePropertyChange(PROPERTY_pPack, oldValue, value);
      return this;
   }

   public List<JavaClass> getClasses()
   {
      return this.classes != null ? Collections.unmodifiableList(this.classes) : Collections.emptyList();
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
         value.setPack(this);
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
         value.setPack(null);
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

}

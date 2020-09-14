package javaPackagesToJavaDoc.JavaPackages;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class JavaClass
{

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_vTag = "vTag";
   private String vTag;
   public static final String PROPERTY_pack = "pack";
   private JavaPackage pack;

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
      result.append(' ').append(this.getVTag());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setPack(null);
   }

   public String getId()
   {
      return this.id;
   }

   public JavaClass setId(String value)
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

   public String getVTag()
   {
      return this.vTag;
   }

   public JavaClass setVTag(String value)
   {
      if (Objects.equals(value, this.vTag))
      {
         return this;
      }

      final String oldValue = this.vTag;
      this.vTag = value;
      this.firePropertyChange(PROPERTY_vTag, oldValue, value);
      return this;
   }

   public JavaPackage getPack()
   {
      return this.pack;
   }

   public JavaClass setPack(JavaPackage value)
   {
      if (this.pack == value)
      {
         return this;
      }

      final JavaPackage oldValue = this.pack;
      if (this.pack != null)
      {
         this.pack = null;
         oldValue.withoutClasses(this);
      }
      this.pack = value;
      if (value != null)
      {
         value.withClasses(this);
      }
      this.firePropertyChange(PROPERTY_pack, oldValue, value);
      return this;
   }

}
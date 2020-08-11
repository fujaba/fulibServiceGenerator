package javaPackagesToJavaDoc.JavaPackagesWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class JavaClass  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public JavaClass setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_vTag = "vTag";

   private String vTag;

   public String getVTag()
   {
      return vTag;
   }

   public JavaClass setVTag(String value)
   {
      if (value == null ? this.vTag != null : ! value.equals(this.vTag))
      {
         String oldValue = this.vTag;
         this.vTag = value;
         firePropertyChange("vTag", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_up = "up";

   private JavaPackage up = null;

   public JavaPackage getUp()
   {
      return this.up;
   }

   public JavaClass setUp(JavaPackage value)
   {
      if (this.up != value)
      {
         JavaPackage oldValue = this.up;
         if (this.up != null)
         {
            this.up = null;
            oldValue.withoutClasses(this);
         }
         this.up = value;
         if (value != null)
         {
            value.withClasses(this);
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

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getId());
      result.append(" ").append(this.getVTag());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setUp(null);

   }

}
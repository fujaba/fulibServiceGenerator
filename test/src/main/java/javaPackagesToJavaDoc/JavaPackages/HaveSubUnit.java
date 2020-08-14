package javaPackagesToJavaDoc.JavaPackages;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveSubUnit extends ModelCommand  
{
   @Override
   public Object run(JavaPackagesEditor editor)
   {
      JavaPackage obj = (JavaPackage) editor.getOrCreate(JavaPackage.class, getId());
      JavaPackage parent = (JavaPackage) editor.getObjectFrame(JavaPackage.class, this.parent);
      obj.setUp(parent);

      return obj;
   }

   @Override
   public void undo(JavaPackagesEditor editor)
   {
      JavaPackage obj = (JavaPackage) editor.removeModelObject(getId());
      obj.setUp(null);
   }

   @Override
   public ModelCommand parse(Object currentObject)
   {
      if ( ! (currentObject instanceof JavaPackage)) {
         return null;
      }

      JavaPackage currentPackage = (JavaPackage) currentObject;

      if (currentPackage.getUp() == null) {
         return null;
      }

      ModelCommand modelCommand = new HaveSubUnit().setParent(currentPackage.getUp().getId()).setId(currentPackage.getId());

      return modelCommand;
   }

   public static final String PROPERTY_parent = "parent";

   private String parent;

   public String getParent()
   {
      return parent;
   }

   public HaveSubUnit setParent(String value)
   {
      if (value == null ? this.parent != null : ! value.equals(this.parent))
      {
         String oldValue = this.parent;
         this.parent = value;
         firePropertyChange("parent", oldValue, value);
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

      result.append(" ").append(this.getParent());


      return result.substring(1);
   }

}
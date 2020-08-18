package javaPackagesToJavaDoc.JavaPackages;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class HaveLeaf extends ModelCommand
{
   @Override
   public Object run(JavaPackagesEditor editor)
   {
      JavaClass obj = (JavaClass) editor.getOrCreate(JavaClass.class, getId());
      JavaPackage parent = (JavaPackage) editor.getObjectFrame(JavaPackage.class, this.parent);
      obj.setUp(parent);
      obj.setVTag(vTag);
      return obj;
   }

   @Override
   public void undo(JavaPackagesEditor editor)
   {
      JavaClass obj = (JavaClass) editor.removeModelObject(getId());
      obj.setUp(null);
   }

   @Override
   public ModelCommand parse(Object currentObject)
   {
      if ( ! (currentObject instanceof JavaClass)) {
         return null;
      }

      JavaClass currentClass = (JavaClass) currentObject;

      ModelCommand modelCommand = new HaveLeaf().setParent(currentClass.getUp().getId()).setId(currentClass.getId());

      return modelCommand;
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
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getParent());
      result.append(' ').append(this.getVTag());
      return result.toString();
   }

   public String getParent()
   {
      return this.parent;
   }

   public HaveLeaf setParent(String value)
   {
      if (Objects.equals(value, this.parent))
      {
         return this;
      }

      final String oldValue = this.parent;
      this.parent = value;
      this.firePropertyChange(PROPERTY_parent, oldValue, value);
      return this;
   }

   public String getVTag()
   {
      return this.vTag;
   }

   public HaveLeaf setVTag(String value)
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

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_parent = "parent";
   private String parent;
   public static final String PROPERTY_vTag = "vTag";
   private String vTag;

}
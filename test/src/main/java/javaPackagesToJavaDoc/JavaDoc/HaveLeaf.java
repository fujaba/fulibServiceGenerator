package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveLeaf extends ModelCommand  
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      DocFile obj = (DocFile) editor.getOrCreate(DocFile.class, getId());
      Folder up = (Folder) editor.getObjectFrame(Folder.class, parent);
      obj.setUp(up);
      obj.setVersion(vTag);

      return obj;
   }

   @Override
   public void undo(JavaDocEditor editor)
   {
      DocFile obj = (DocFile) editor.removeModelObject(getId());
      obj.setUp(null);
   }

   public static final String PROPERTY_parent = "parent";

   private String parent;

   public String getParent()
   {
      return parent;
   }

   public HaveLeaf setParent(String value)
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
      result.append(" ").append(this.getVTag());


      return result.substring(1);
   }

   public static final String PROPERTY_vTag = "vTag";

   private String vTag;

   public String getVTag()
   {
      return vTag;
   }

   public HaveLeaf setVTag(String value)
   {
      if (value == null ? this.vTag != null : ! value.equals(this.vTag))
      {
         String oldValue = this.vTag;
         this.vTag = value;
         firePropertyChange("vTag", oldValue, value);
      }
      return this;
   }

}
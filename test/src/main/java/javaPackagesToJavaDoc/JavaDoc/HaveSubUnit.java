package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class HaveSubUnit extends ModelCommand
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      Folder obj = (Folder) editor.getOrCreate(Folder.class, getId());
      Folder up = (Folder) editor.getObjectFrame(Folder.class, parent);
      obj.setUp(up);

      String docId = getId() + "Doc";
      DocFile file = (DocFile) editor.getOrCreate(DocFile.class, docId);
      file.setContent(getId());
      obj.withFiles(file);

      return obj;
   }

   @Override
   public void undo(JavaDocEditor editor)
   {
      Folder obj = (Folder) editor.removeModelObject(getId());
      obj.setUp(null);

      String docId = getId() + "Doc";
      DocFile file = (DocFile) editor.removeModelObject(docId);
      file.setUp(null);
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
      return result.toString();
   }

   public String getParent()
   {
      return this.parent;
   }

   public HaveSubUnit setParent(String value)
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

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_parent = "parent";
   private String parent;

}
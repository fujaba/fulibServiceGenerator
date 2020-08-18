package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class HaveContent extends ModelCommand
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      DocFile obj = (DocFile) editor.getObjectFrame(DocFile.class, owner);
      obj.setContent(content);

      return obj;
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
      result.append(' ').append(this.getOwner());
      result.append(' ').append(this.getContent());
      return result.toString();
   }

   public String getOwner()
   {
      return this.owner;
   }

   public HaveContent setOwner(String value)
   {
      if (Objects.equals(value, this.owner))
      {
         return this;
      }

      final String oldValue = this.owner;
      this.owner = value;
      this.firePropertyChange(PROPERTY_owner, oldValue, value);
      return this;
   }

   public String getContent()
   {
      return this.content;
   }

   public HaveContent setContent(String value)
   {
      if (Objects.equals(value, this.content))
      {
         return this;
      }

      final String oldValue = this.content;
      this.content = value;
      this.firePropertyChange(PROPERTY_content, oldValue, value);
      return this;
   }

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_owner = "owner";
   private String owner;
   public static final String PROPERTY_content = "content";
   private String content;

}
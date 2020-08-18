package javaPackagesToJavaDoc.JavaDocWithPatterns;
import org.fulib.servicegenerator.FulibPatternDiagram;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class HaveLeaf extends ModelCommand
{
   private static Pattern pattern = null;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_parent = "parent";
   private String parent;
   public static final String PROPERTY_vTag = "vTag";
   private String vTag;

   @Override
   public Pattern havePattern()
   {
      if (pattern == null) {
         pattern = new Pattern();

         PatternObject d = new PatternObject().setPattern(pattern).setPoId("d").setHandleObjectClass(DocFile.class).setKind("core");
         new PatternAttribute().setObject(d).setHandleAttrName(DocFile.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);
         new PatternAttribute().setObject(d).setHandleAttrName(DocFile.PROPERTY_version).setCommandParamName(HaveLeaf.PROPERTY_vTag);

         PatternObject f = new PatternObject().setPattern(pattern).setPoId("f").setHandleObjectClass(Folder.class).setKind("context");
         new PatternAttribute().setObject(f).setHandleAttrName(Folder.PROPERTY_id).setCommandParamName(HaveLeaf.PROPERTY_parent);

         new PatternLink().setSource(d).setHandleLinkName(DocFile.PROPERTY_up).setTarget(f);

         new FulibPatternDiagram().dump("tmp/HaveDocLeaf.svg", pattern);
      }
      return pattern;
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

}
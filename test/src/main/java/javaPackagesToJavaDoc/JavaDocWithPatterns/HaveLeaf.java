package javaPackagesToJavaDoc.JavaDocWithPatterns;
import org.fulib.servicegenerator.FulibPatternDiagram;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveLeaf extends ModelCommand  
{
   private static Pattern pattern = null;

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

}

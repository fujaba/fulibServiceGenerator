package javaPackagesToJavaDoc.JavaDocWithPatterns;
import org.fulib.servicegenerator.FulibPatternDiagram;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class HaveSubUnit extends ModelCommand  
{
   private static Pattern pattern = null;

   @Override
   public Pattern havePattern()
   {
      if (pattern == null) {
         pattern = new Pattern();

         PatternObject f = new PatternObject().setPattern(pattern).setPoId("f").setHandleObjectClass(Folder.class).setKind("core");
         new PatternAttribute().setObject(f).setHandleAttrName(Folder.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);

         PatternObject d = new PatternObject().setPattern(pattern).setPoId("d").setHandleObjectClass(DocFile.class).setKind("core");
         new PatternAttribute().setObject(d).setHandleAttrName(Folder.PROPERTY_id).setCommandParamName("docId");

         new PatternLink().setSource(f).setHandleLinkName(Folder.PROPERTY_files).setTarget(d);

         PatternObject sf = new PatternObject().setPattern(pattern).setPoId("sf").setHandleObjectClass(Folder.class).setKind("context");
         new PatternAttribute().setObject(sf).setHandleAttrName(Folder.PROPERTY_id).setCommandParamName(HaveSubUnit.PROPERTY_parent);

         new PatternLink().setSource(f).setHandleLinkName(Folder.PROPERTY_up).setTarget(sf);

         new FulibPatternDiagram().dump("tmp/HaveDocSubUnit.svg", pattern);
      }
      return pattern;
   }

   public String getDocId() {
      return getId() + "Doc";
   }

   public void setDocId() {
      // derived from id
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

package javaPackagesToJavaDoc.JavaPackagesWithPatterns;
import javaPackagesToJavaDoc.JavaDocWithPatterns.JavaDocWithPatternsEditor;
import org.fulib.servicegenerator.FulibPatternDiagram;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class HaveSubUnit extends ModelCommand
{
   private static Pattern pattern = null;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_parent = "parent";
   private String parent;

   @Override
   public Pattern havePattern()
   {
      if (pattern == null) {
         pattern = new Pattern();

         PatternObject p = new PatternObject().setPattern(pattern).setPoId("p").setHandleObjectClass(JavaPackage.class).setKind("core");
         new PatternAttribute().setObject(p).setHandleAttrName(JavaPackage.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);

         PatternObject sp = new PatternObject().setPattern(pattern).setPoId("sp").setHandleObjectClass(JavaPackage.class).setKind("context");
         new PatternAttribute().setObject(sp).setHandleAttrName(JavaPackage.PROPERTY_id).setCommandParamName(HaveSubUnit.PROPERTY_parent);

         new PatternLink().setSource(p).setHandleLinkName(JavaPackage.PROPERTY_up).setTarget(sp);

         new FulibPatternDiagram().dump("tmp/HaveSubUnit.svg", pattern);
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

}
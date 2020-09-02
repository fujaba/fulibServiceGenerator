package javaPackagesToJavaDoc.JavaDocWithPatterns;

import org.fulib.servicegenerator.FulibPatternDiagram;

import java.util.Objects;

public class HaveSubUnit extends ModelCommand
{
   private static Pattern pattern = null;
   public static final String PROPERTY_parent = "parent";
   private String parent;

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

package javaPackagesToJavaDoc.JavaDocWithPatterns;

import org.fulib.services.FulibPatternDiagram;

public class HaveLeaf extends ModelCommand
{
   private static Pattern pattern = null;
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

         new PatternLink().setSource(d).setHandleLinkName(DocFile.PROPERTY_folder).setTarget(f);

         new FulibPatternDiagram().dump("tmp/HaveDocLeaf.svg", pattern);
      }
      return pattern;
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
      this.parent = value;
      return this;
   }

   public String getVTag()
   {
      return this.vTag;
   }

   public HaveLeaf setVTag(String value)
   {
      this.vTag = value;
      return this;
   }

}

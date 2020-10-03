package javaPackagesToJavaDoc.JavaDocWithPatterns;

import org.fulib.services.FulibPatternDiagram;

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

         PatternObject pf = new PatternObject().setPattern(pattern).setPoId("pf").setHandleObjectClass(Folder.class).setKind("context");
         new PatternAttribute().setObject(pf).setHandleAttrName(Folder.PROPERTY_id).setCommandParamName(HaveSubUnit.PROPERTY_parent);

         new PatternLink().setSource(f).setHandleLinkName(Folder.PROPERTY_pFolder).setTarget(pf);

         new FulibPatternDiagram().dump("tmp/HaveDocSubUnit.svg", pattern);
      }
      return pattern;
   }

   public String getDocId() {
      return getId() + ".Doc";
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
      this.parent = value;
      return this;
   }

}

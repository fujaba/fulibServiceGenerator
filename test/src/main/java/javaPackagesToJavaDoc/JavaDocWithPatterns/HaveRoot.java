package javaPackagesToJavaDoc.JavaDocWithPatterns;

import org.fulib.servicegenerator.FulibPatternDiagram;

public class HaveRoot extends ModelCommand  
{
   private static Pattern pattern = null;

   @Override
   public Pattern havePattern()
   {
      if (pattern == null) {
         pattern = new Pattern();

         PatternObject f = new PatternObject().setPattern(pattern).setPoId("f").setHandleObjectClass(Folder.class).setKind("core");
         new PatternAttribute().setObject(f).setHandleAttrName(Folder.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);

         PatternObject sf = new PatternObject().setPattern(pattern).setPoId("sf").setHandleObjectClass(Folder.class).setKind("nac");
         new PatternLink().setSource(f).setHandleLinkName(Folder.PROPERTY_up).setTarget(sf).setKind("nac");

         PatternObject d = new PatternObject().setPattern(pattern).setPoId("d").setHandleObjectClass(DocFile.class).setKind("nac");
         new PatternAttribute().setObject(d).setHandleAttrName(DocFile.PROPERTY_id).setCommandParamName("docId");
         new PatternLink().setSource(f).setHandleLinkName(Folder.PROPERTY_files).setTarget(d).setKind("nac");

         new FulibPatternDiagram().dump("tmp/HaveDocRoot.svg", pattern);
      }
      return pattern;
   }

   public String getDocId() {
      return getId() + "Doc";
   }

   public void setDocId() {
      // derived from id
   }

}

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
         new FulibPatternDiagram().dump("tmp/HaveDocRoot.svg", pattern);
      }
      return pattern;
   }
}

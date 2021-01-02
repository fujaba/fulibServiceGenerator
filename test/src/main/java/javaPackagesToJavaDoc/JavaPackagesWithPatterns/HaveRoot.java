package javaPackagesToJavaDoc.JavaPackagesWithPatterns;

import org.fulib.patterns.Pattern;
import org.fulib.patterns.PatternAttribute;
import org.fulib.patterns.PatternLink;
import org.fulib.patterns.PatternObject;
import org.fulib.services.FulibPatternDiagram;
import org.fulib.patterns.*;

public class HaveRoot extends ModelCommand
{
   private static Pattern pattern = null;

   @Override
   public Pattern havePattern()
   {
      if (pattern == null) {
         pattern = new Pattern();

         PatternObject p = new PatternObject().setPattern(pattern).setPoId("p").setHandleObjectClass(JavaPackage.class).setKind("core");
         new PatternAttribute().setObject(p).setHandleAttrName(JavaPackage.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);

         PatternObject pp = new PatternObject().setPattern(pattern).setPoId("pp").setHandleObjectClass(JavaPackage.class).setKind("nac");

         new PatternLink().setSource(p).setHandleLinkName(JavaPackage.PROPERTY_pPack).setTarget(pp).setKind("nac");

         new FulibPatternDiagram().dump("tmp/HaveRoot.svg", pattern);
      }

      return pattern;
   }

}

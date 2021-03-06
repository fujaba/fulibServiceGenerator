package javaPackagesToJavaDoc.JavaPackagesWithPatterns;

import org.fulib.services.FulibPatternDiagram;
import org.fulib.patterns.*;

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

         PatternObject p = new PatternObject().setPattern(pattern).setPoId("p").setHandleObjectClass(JavaPackage.class).setKind("core");
         new PatternAttribute().setObject(p).setHandleAttrName(JavaPackage.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);

         PatternObject pp = new PatternObject().setPattern(pattern).setPoId("pp").setHandleObjectClass(JavaPackage.class).setKind("context");
         new PatternAttribute().setObject(pp).setHandleAttrName(JavaPackage.PROPERTY_id).setCommandParamName(HaveSubUnit.PROPERTY_parent);

         new PatternLink().setSource(p).setHandleLinkName(JavaPackage.PROPERTY_pPack).setTarget(pp);

         new FulibPatternDiagram().dump("tmp/HaveSubUnit.svg", pattern);
      }

      return pattern;
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

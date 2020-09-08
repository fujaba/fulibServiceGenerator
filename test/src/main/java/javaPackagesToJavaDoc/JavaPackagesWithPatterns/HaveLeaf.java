package javaPackagesToJavaDoc.JavaPackagesWithPatterns;

import org.fulib.servicegenerator.FulibPatternDiagram;

import java.util.Objects;

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

         PatternObject c = new PatternObject().setPattern(pattern).setPoId("c").setHandleObjectClass(JavaClass.class).setKind("core");
         new PatternAttribute().setObject(c).setHandleAttrName(JavaClass.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);
         new PatternAttribute().setObject(c).setHandleAttrName(JavaClass.PROPERTY_vTag).setCommandParamName(HaveLeaf.PROPERTY_vTag);

         PatternObject p = new PatternObject().setPattern(pattern).setPoId("p").setHandleObjectClass(JavaPackage.class).setKind("context");
         new PatternAttribute().setObject(p).setHandleAttrName(JavaPackage.PROPERTY_id).setCommandParamName(HaveLeaf.PROPERTY_parent);

         new PatternLink().setSource(c).setHandleLinkName(JavaClass.PROPERTY_up).setTarget(p);

         new FulibPatternDiagram().dump("tmp/HaveLeaf.svg", pattern);
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

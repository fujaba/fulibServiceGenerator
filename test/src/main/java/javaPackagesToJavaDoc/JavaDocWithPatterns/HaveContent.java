package javaPackagesToJavaDoc.JavaDocWithPatterns;

import org.fulib.servicegenerator.FulibPatternDiagram;

import java.util.Objects;

public class HaveContent extends ModelCommand
{
   private static Pattern pattern = null;
   public static final String PROPERTY_owner = "owner";
   private String owner;
   public static final String PROPERTY_content = "content";
   private String content;

   @Override
   public Pattern havePattern()
   {
      if (pattern == null) {
         pattern = new Pattern();

         PatternObject d = new PatternObject().setPattern(pattern).setPoId("d").setHandleObjectClass(DocFile.class).setKind("core");
         new PatternAttribute().setObject(d).setHandleAttrName(DocFile.PROPERTY_id).setCommandParamName(ModelCommand.PROPERTY_id);
         new PatternAttribute().setObject(d).setHandleAttrName(DocFile.PROPERTY_content).setCommandParamName(HaveContent.PROPERTY_content);

         new FulibPatternDiagram().dump("tmp/HaveDocContent.svg", pattern);
      }
      return pattern;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getOwner());
      result.append(' ').append(this.getContent());
      return result.toString();
   }

   public String getOwner()
   {
      return this.owner;
   }

   public HaveContent setOwner(String value)
   {
      if (Objects.equals(value, this.owner))
      {
         return this;
      }

      final String oldValue = this.owner;
      this.owner = value;
      this.firePropertyChange(PROPERTY_owner, oldValue, value);
      return this;
   }

   public String getContent()
   {
      return this.content;
   }

   public HaveContent setContent(String value)
   {
      if (Objects.equals(value, this.content))
      {
         return this;
      }

      final String oldValue = this.content;
      this.content = value;
      this.firePropertyChange(PROPERTY_content, oldValue, value);
      return this;
   }

}

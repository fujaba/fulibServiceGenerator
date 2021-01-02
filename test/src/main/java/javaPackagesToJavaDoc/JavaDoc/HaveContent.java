package javaPackagesToJavaDoc.JavaDoc;

import java.util.Objects;
import org.fulib.patterns.*;

public class HaveContent extends ModelCommand
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      DocFile obj = (DocFile) editor.getObjectFrame(DocFile.class, owner);
      obj.setContent(content);

      return obj;
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
      this.owner = value;
      return this;
   }

   public String getContent()
   {
      return this.content;
   }

   public HaveContent setContent(String value)
   {
      this.content = value;
      return this;
   }
   public static final String PROPERTY_owner = "owner";
   private String owner;
   public static final String PROPERTY_content = "content";
   private String content;

}

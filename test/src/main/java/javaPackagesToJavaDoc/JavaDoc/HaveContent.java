package javaPackagesToJavaDoc.JavaDoc;

import java.util.Objects;

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
   public static final String PROPERTY_owner = "owner";
   private String owner;
   public static final String PROPERTY_content = "content";
   private String content;

}

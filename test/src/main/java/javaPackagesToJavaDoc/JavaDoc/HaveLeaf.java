package javaPackagesToJavaDoc.JavaDoc;

import java.util.Objects;

public class HaveLeaf extends ModelCommand
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      DocFile obj = (DocFile) editor.getOrCreate(DocFile.class, getId());
      Folder up = (Folder) editor.getObjectFrame(Folder.class, parent);
      obj.setFolder(up);
      obj.setVersion(vTag);

      return obj;
   }

   @Override
   public void remove(JavaDocEditor editor)
   {
      DocFile obj = (DocFile) editor.removeModelObject(getId());
      obj.setFolder(null);
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
   public static final String PROPERTY_parent = "parent";
   private String parent;
   public static final String PROPERTY_vTag = "vTag";
   private String vTag;

}

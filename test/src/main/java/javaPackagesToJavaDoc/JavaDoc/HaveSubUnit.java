package javaPackagesToJavaDoc.JavaDoc;

import java.util.Objects;

public class HaveSubUnit extends ModelCommand
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      Folder p = (Folder) editor.getOrCreate(Folder.class, getId());
      Folder sp = (Folder) editor.getObjectFrame(Folder.class, parent);
      p.setPFolder(sp);

      String docId = getId() + "Doc";
      DocFile d = (DocFile) editor.getOrCreate(DocFile.class, docId);
      d.setContent(getId());
      p.withFiles(d);

      return p;
   }

   @Override
   public void remove(JavaDocEditor editor)
   {
      Folder obj = (Folder) editor.removeModelObject(getId());
      obj.setPFolder(null);

      String docId = getId() + "Doc";
      DocFile file = (DocFile) editor.removeModelObject(docId);
      file.setFolder(null);
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
   public static final String PROPERTY_parent = "parent";
   private String parent;

}

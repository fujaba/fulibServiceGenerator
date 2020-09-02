package javaPackagesToJavaDoc.JavaDoc;

import java.util.Objects;

public class HaveSubUnit extends ModelCommand
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      Folder p = (Folder) editor.getOrCreate(Folder.class, getId());
      Folder sp = (Folder) editor.getObjectFrame(Folder.class, parent);
      p.setUp(sp);

      String docId = getId() + "Doc";
      DocFile d = (DocFile) editor.getOrCreate(DocFile.class, docId);
      d.setContent(getId());
      p.withFiles(d);

      return p;
   }

   @Override
   public void undo(JavaDocEditor editor)
   {
      Folder obj = (Folder) editor.removeModelObject(getId());
      obj.setUp(null);

      String docId = getId() + "Doc";
      DocFile file = (DocFile) editor.removeModelObject(docId);
      file.setUp(null);
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
      if (Objects.equals(value, this.parent))
      {
         return this;
      }

      final String oldValue = this.parent;
      this.parent = value;
      this.firePropertyChange(PROPERTY_parent, oldValue, value);
      return this;
   }
   public static final String PROPERTY_parent = "parent";
   private String parent;

}

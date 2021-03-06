package javaPackagesToJavaDoc.JavaDoc;
import org.fulib.patterns.*;

public class HaveRoot extends ModelCommand
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      Folder f = (Folder) editor.getOrCreate(Folder.class, getId());
      f.setPFolder(null);

      String docId = getId() + ".Doc";
      DocFile d = f.getFromFiles(docId);
      if (d != null) {
         editor.removeModelObject(d.getId());
         f.withoutFiles(d);
      }

      return f;
   }

}

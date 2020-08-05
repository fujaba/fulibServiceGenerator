package javaPackagesToJavaDoc.JavaDoc;

public class HaveRoot extends ModelCommand  
{
   @Override
   public Object run(JavaDocEditor editor)
   {
      Folder obj = (Folder) editor.getOrCreate(Folder.class, getId());
      obj.setUp(null);

      String docId = getId() + "Doc";
      DocFile file = obj.getFromFiles(docId);
      if (file != null) {
         editor.removeModelObject(file.getId());
         obj.withoutFiles(file);
      }

      return obj;
   }

}

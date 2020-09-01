package javaPackagesToJavaDoc.JavaDoc;

public class RemoveCommand extends ModelCommand
{

   public Object run(JavaDocEditor editor)
   {
      editor.removeModelObject(getId());
      ModelCommand oldCommand = editor.getActiveCommands().get(getId());
      if (oldCommand != null) {
         oldCommand.undo(editor);
      }
      return null;
   }

}

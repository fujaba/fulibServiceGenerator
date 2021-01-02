package javaPackagesToJavaDoc.JavaDocWithPatterns;
import org.fulib.patterns.*;

public class RemoveCommand extends ModelCommand
{

   @Override
   public Object run(JavaDocWithPatternsEditor editor)
   {
      editor.removeModelObject(getId());
      ModelCommand oldCommand = editor.getActiveCommands().get(getId());
      if (oldCommand != null) {
         oldCommand.remove(editor);
      }
      return null;
   }

}

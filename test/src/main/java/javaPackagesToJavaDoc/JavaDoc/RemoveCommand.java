package javaPackagesToJavaDoc.JavaDoc;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import java.lang.reflect.Method;

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
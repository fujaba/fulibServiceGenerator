package javaPackagesToJavaDoc.JavaDoc;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import java.lang.reflect.Method;

public class RemoveCommand extends ModelCommand  
{

   public Object run(JavaDocEditor editor) { 
      java.util.Map<String,Object> mapOfModelObjects = editor.getMapOfModelObjects();
      java.util.Map<String,Object> mapOfFrames = editor.getMapOfFrames();

      Object oldObject = mapOfModelObjects.remove(getId());

      if (oldObject != null) {
         mapOfFrames.put(getId(), oldObject);
      }

      // call undo on old command
      ModelCommand oldCommand = editor.getActiveCommands().get(getId());
      oldCommand.undo(editor);

      return null;
   }

}
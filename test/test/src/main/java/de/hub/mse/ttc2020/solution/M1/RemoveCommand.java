package de.hub.mse.ttc2020.solution.M1;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import java.lang.reflect.Method;

public class RemoveCommand extends ModelCommand
{

   public boolean preCheck(M1Editor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("RemoveCommand-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("RemoveCommand-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("RemoveCommand-" + this.getId(), this);
      return true;
   }

   public Object run(M1Editor editor) { 
      java.util.Map<String, Object> mapOfModelObjects = editor.getMapOfModelObjects();
      java.util.Map<String, Object> mapOfFrames = editor.getMapOfFrames();

      Object oldObject = mapOfModelObjects.remove(getId());

      if (oldObject != null) {
         mapOfFrames.put(getId(), oldObject);
      }

      return null;
   }

}
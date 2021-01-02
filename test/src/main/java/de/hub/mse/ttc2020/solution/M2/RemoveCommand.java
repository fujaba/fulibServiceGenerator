package de.hub.mse.ttc2020.solution.M2;
import org.fulib.patterns.*;

public class RemoveCommand extends ModelCommand
{

   @Override
   public Object run(M2Editor editor)
   {
      editor.removeModelObject(getId());
      ModelCommand oldCommand = editor.getActiveCommands().get(getId());
      if (oldCommand != null) {
         oldCommand.remove(editor);
      }
      return null;
   }

}

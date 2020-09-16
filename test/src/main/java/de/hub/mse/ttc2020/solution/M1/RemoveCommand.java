package de.hub.mse.ttc2020.solution.M1;

public class RemoveCommand extends ModelCommand
{

   @Override
   public Object run(M1Editor editor)
   {
      editor.removeModelObject(getId());
      ModelCommand oldCommand = editor.getActiveCommands().get(getId());
      if (oldCommand != null) {
         oldCommand.remove(editor);
      }
      return null;
   }

}

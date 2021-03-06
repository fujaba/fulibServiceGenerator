package javaPackagesToJavaDoc.JavaPackages;
import org.fulib.patterns.*;

public class HaveRoot extends ModelCommand
{
   @Override
   public Object run(JavaPackagesEditor editor)
   {
      JavaPackage obj = (JavaPackage) editor.getOrCreate(JavaPackage.class, this.getId());
      obj.setPPack(null);

      return obj;
   }

   @Override
   public void remove(JavaPackagesEditor editor)
   {
      editor.removeModelObject(getId());
   }

   @Override
   public ModelCommand parse(Object currentObject)
   {
      if (! (currentObject instanceof JavaPackage)) {
         return null;
      }

      JavaPackage currentPackage = (JavaPackage) currentObject;

      if (currentPackage.getPPack() != null) {
         return null;
      }

      if (currentPackage.getClasses().isEmpty() && currentPackage.getSubPackages().isEmpty()) {
         ModelCommand modelCommand = new RemoveCommand().setId(currentPackage.getId());
         return modelCommand;
      }

      // yes its me
      ModelCommand modelCommand = new HaveRoot().setId(currentPackage.getId());

      return modelCommand;
   }

}

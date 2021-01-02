package javaPackagesToJavaDoc.JavaPackages;

import java.util.Objects;
import org.fulib.patterns.*;

public class HaveSubUnit extends ModelCommand
{
   @Override
   public Object run(JavaPackagesEditor editor)
   {
      JavaPackage obj = (JavaPackage) editor.getOrCreate(JavaPackage.class, getId());
      JavaPackage parent = (JavaPackage) editor.getObjectFrame(JavaPackage.class, this.parent);
      obj.setPPack(parent);

      return obj;
   }

   @Override
   public void remove(JavaPackagesEditor editor)
   {
      JavaPackage obj = (JavaPackage) editor.removeModelObject(getId());
      obj.setPPack(null);
   }

   @Override
   public ModelCommand parse(Object currentObject)
   {
      if ( ! (currentObject instanceof JavaPackage)) {
         return null;
      }

      JavaPackage currentPackage = (JavaPackage) currentObject;

      if (currentPackage.getPPack() == null) {
         return null;
      }

      ModelCommand modelCommand = new HaveSubUnit().setParent(currentPackage.getPPack().getId()).setId(currentPackage.getId());

      return modelCommand;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getParent());
      return result.toString();
   }

   public String getParent()
   {
      return this.parent;
   }

   public HaveSubUnit setParent(String value)
   {
      this.parent = value;
      return this;
   }
   public static final String PROPERTY_parent = "parent";
   private String parent;

}

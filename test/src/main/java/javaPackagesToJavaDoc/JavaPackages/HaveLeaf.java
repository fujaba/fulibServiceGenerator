package javaPackagesToJavaDoc.JavaPackages;

import java.util.Objects;
import org.fulib.patterns.*;

public class HaveLeaf extends ModelCommand
{
   @Override
   public Object run(JavaPackagesEditor editor)
   {
      JavaClass obj = (JavaClass) editor.getOrCreate(JavaClass.class, getId());
      JavaPackage parent = (JavaPackage) editor.getObjectFrame(JavaPackage.class, this.parent);
      obj.setPack(parent);
      obj.setVTag(vTag);
      return obj;
   }

   @Override
   public void remove(JavaPackagesEditor editor)
   {
      JavaClass obj = (JavaClass) editor.removeModelObject(getId());
      obj.setPack(null);
   }

   @Override
   public ModelCommand parse(Object currentObject)
   {
      if ( ! (currentObject instanceof JavaClass)) {
         return null;
      }

      JavaClass currentClass = (JavaClass) currentObject;

      if (currentClass.getPack() == null) {
         ModelCommand modelCommand = new RemoveCommand().setId(currentClass.getId());
         return modelCommand;
      }

      ModelCommand modelCommand = new HaveLeaf().setParent(currentClass.getPack().getId()).setId(currentClass.getId());

      return modelCommand;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getParent());
      result.append(' ').append(this.getVTag());
      return result.toString();
   }

   public String getParent()
   {
      return this.parent;
   }

   public HaveLeaf setParent(String value)
   {
      this.parent = value;
      return this;
   }

   public String getVTag()
   {
      return this.vTag;
   }

   public HaveLeaf setVTag(String value)
   {
      this.vTag = value;
      return this;
   }
   public static final String PROPERTY_parent = "parent";
   private String parent;
   public static final String PROPERTY_vTag = "vTag";
   private String vTag;

}

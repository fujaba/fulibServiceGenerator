package javaPackagesToJavaDoc.JavaPackages;

public class HaveRoot extends ModelCommand  
{
   @Override
   public Object run(JavaPackagesEditor editor)
   {
      JavaPackage obj = (JavaPackage) editor.getOrCreate(JavaPackage.class, getId());
      obj.setUp(null);

      return obj;
   }

}

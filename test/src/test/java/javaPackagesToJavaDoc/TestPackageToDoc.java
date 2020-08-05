package javaPackagesToJavaDoc;

import javaPackagesToJavaDoc.JavaPackages.*;
import javaPackagesToJavaDoc.JavaDoc.JavaDocEditor;
import org.fulib.FulibTools;
import org.fulib.servicegenerator.FulibPatternDiagram;
import org.fulib.tables.ObjectTable;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings( {"unchecked", "deprecation"} )
public class TestPackageToDoc
{
   @Test
   public void testMultiRootScenario()
   {
      JavaPackagesEditor javaPackagesEditor = new JavaPackagesEditor();

      ModelCommand cmd = new HaveRoot().setId("root");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("root").setId("sub");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("sub").setId("leaf");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("leaf").setId("c");
      javaPackagesEditor.execute(cmd);

      Object root = javaPackagesEditor.getModelObject("root");
      assertThat(root, notNullValue());

      ObjectTable objectTable = new ObjectTable("root", root)
            .expandLink("sub", JavaPackage.PROPERTY_subPackages)
            .expandLink("leaf", JavaPackage.PROPERTY_subPackages)
            .expandLink("c", JavaPackage.PROPERTY_classes);

      assertThat(objectTable.getTable().size(), is(1));

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesScenarioStart.svg", root, javaPackagesEditor.getActiveCommands().values());

      JavaDocEditor javaDocEditor = new JavaDocEditor();
   }
}

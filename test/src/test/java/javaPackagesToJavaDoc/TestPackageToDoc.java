package javaPackagesToJavaDoc;

import javaPackagesToJavaDoc.JavaDoc.Folder;
import javaPackagesToJavaDoc.JavaPackages.*;
import javaPackagesToJavaDoc.JavaDoc.JavaDocEditor;
import org.fulib.FulibTools;
import org.fulib.servicegenerator.FulibPatternDiagram;
import org.fulib.tables.ObjectTable;
import org.fulib.yaml.Yaml;
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

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesScenarioStart.svg",
            root,
            javaPackagesEditor.getActiveCommands().values());

      ObjectTable objectTable = new ObjectTable("root", root)
            .expandLink("sub", JavaPackage.PROPERTY_subPackages)
            .expandLink("leaf", JavaPackage.PROPERTY_subPackages)
            .expandLink("c", JavaPackage.PROPERTY_classes);
      assertThat(objectTable.getTable().size(), is(1));

      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());

      JavaDocEditor javaDocEditor = new JavaDocEditor();
      javaDocEditor.loadYaml(yaml);

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("root");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesScenarioFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      ObjectTable tableFocusedOnSubColumn = new ObjectTable("root", rootFolder)
            .expandLink("sub", Folder.PROPERTY_subFolders);

      tableFocusedOnSubColumn.expandLink("subDoc", Folder.PROPERTY_files);
      ObjectTable tableFocusedOnLeaf = tableFocusedOnSubColumn.expandLink("leaf", Folder.PROPERTY_subFolders);
      tableFocusedOnLeaf.expandLink("leafDoc", Folder.PROPERTY_files);

      assertThat(tableFocusedOnSubColumn.getTable().size(), is(2));

      assertThat(javaDocEditor.getActiveCommands().size(), is(javaPackagesEditor.getActiveCommands().size()));




   }
}

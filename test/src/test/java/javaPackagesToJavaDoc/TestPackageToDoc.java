package javaPackagesToJavaDoc;

import javaPackagesToJavaDoc.JavaDoc.DocFile;
import javaPackagesToJavaDoc.JavaDoc.Folder;
import javaPackagesToJavaDoc.JavaDoc.HaveContent;
import javaPackagesToJavaDoc.JavaDoc.JavaDocEditor;
import javaPackagesToJavaDoc.JavaPackages.*;
import org.fulib.FulibTools;
import org.fulib.tables.PathTable;
import org.fulib.yaml.Yaml;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings( {"unchecked", "deprecation"} )
public class TestPackageToDoc implements PropertyChangeListener
{
   @Test
   public void testOneWaySync()
   {
      JavaPackagesEditor javaPackagesEditor = new JavaPackagesEditor();

      ModelCommand cmd = new HaveRoot().setId("root");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("root").setId("sub");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("sub").setVTag("1.0").setId("c");
      javaPackagesEditor.execute(cmd);

      Object root = javaPackagesEditor.getModelObject("root");
      assertThat(root, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/OneWaySyncStart.svg",
            root,
            javaPackagesEditor.getActiveCommands().values());

      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());

      JavaDocEditor javaDocEditor = new JavaDocEditor();
      javaDocEditor.loadYaml(yaml);

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("root");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/OneWaySyncFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      cmd = new HaveRoot().setId("nRoot");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("nRoot").setId("root");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveRoot().setId("sub");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("sub").setVTag("1.1").setId("c2");
      javaPackagesEditor.execute(cmd);

      FulibTools.objectDiagrams().dumpSVG("tmp/OneWaySyncTwoRoots.svg",
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());
      javaDocEditor.loadYaml(yaml);

      FulibTools.objectDiagrams().dumpSVG("tmp/OneWaySyncTwoRootsForward.svg",
            javaDocEditor.getActiveCommands().values(),
            javaDocEditor.getMapOfModelObjects().values());
   }

   LinkedHashSet changedObjects = new LinkedHashSet();

   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      changedObjects.add(evt.getSource());
   }


   @Test
   public void testManualChangesAndParsing()
   {
      JavaPackagesEditor javaPackagesEditor = new JavaPackagesEditor();

      buildJavaPackagesStartSituation(javaPackagesEditor);

      JavaDocEditor javaDocEditor = new JavaDocEditor();
      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());
      javaDocEditor.loadYaml(yaml);

      addJavaDocComments(javaDocEditor);

      // register listener for incremental parsing
      changedObjects.clear();
      for (Object modelObject : javaPackagesEditor.getMapOfModelObjects().values()) {
         try {
            Method addPropertyChangeListener = modelObject.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class);
            addPropertyChangeListener.invoke(modelObject, this);
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }


      // some manual changes:
      JavaPackage nRoot = new JavaPackage().setId("nRoot");
      changedObjects.add(nRoot);
      JavaPackage root = (JavaPackage) javaPackagesEditor.getModelObject("root");
      nRoot.withSubPackages(root);
      JavaPackage sub = (JavaPackage) javaPackagesEditor.getModelObject("sub");
      sub.setUp(null);
      JavaClass c2 = new JavaClass().setUp(sub).setVTag("1.1").setId("c2");
      changedObjects.add(c2);
      JavaClass c = (JavaClass) javaPackagesEditor.getModelObject("c");
      c.setVTag("1.1");

      javaPackagesEditor.parse(changedObjects);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesAfterParsing.svg",
            nRoot,
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      ModelCommand nRootCmd = javaPackagesEditor.getActiveCommands().get("nRoot");
      assertThat(nRootCmd, instanceOf(HaveRoot.class));

      ModelCommand rootCmd = javaPackagesEditor.getActiveCommands().get("root");
      assertThat(rootCmd, instanceOf(HaveSubUnit.class));

      ModelCommand c2Cmd = javaPackagesEditor.getActiveCommands().get("c2");
      assertThat(c2Cmd, instanceOf(HaveLeaf.class));
   }

   @Test
   public void testFirstForwardExample()
   {
      JavaPackagesEditor javaPackagesEditor = new JavaPackagesEditor();

      buildJavaPackagesStartSituation(javaPackagesEditor);
      ModelCommand cmd;

      JavaPackage root = (JavaPackage) javaPackagesEditor.getModelObject("root");
      assertThat(root, notNullValue());
      JavaPackage sub = (JavaPackage) javaPackagesEditor.getModelObject("sub");
      assertThat(sub, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesScenarioStart.svg",
            root,
            javaPackagesEditor.getActiveCommands().values());

      PathTable pathTable = new PathTable("root", root)
            .expand("root", javaPackagesToJavaDoc.JavaPackagesWithPatterns.JavaPackage.PROPERTY_subPackages, "sub")
            .expand("sub", javaPackagesToJavaDoc.JavaPackagesWithPatterns.JavaPackage.PROPERTY_subPackages, "leaf")
            .expand("leaf", javaPackagesToJavaDoc.JavaPackagesWithPatterns.JavaPackage.PROPERTY_classes, "c");
      assertThat(pathTable.rowCount(), is(1));

      // forward to doc model
      JavaDocEditor javaDocEditor = new JavaDocEditor();
      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());
      javaDocEditor.loadYaml(yaml);

      // add some docu content
      addJavaDocComments(javaDocEditor);

      javaPackagesToJavaDoc.JavaDoc.ModelCommand docCmd;
      DocFile leafDoc;

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("root");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesScenarioFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      PathTable docTable = new PathTable("root", rootFolder)
            .expand("root", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_subFolders, "sub")
            .expand("sub", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_files, "subDoc")
            .expand("sub", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_subFolders, "leaf")
            .expand("leaf", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_files,"leafDoc");
      assertThat(docTable.rowCount(), is(2));
      assertThat(javaDocEditor.getActiveCommands().size(), is(javaPackagesEditor.getActiveCommands().size() + 3));

      // some editing on packages
      assertThat(sub.getUp(), notNullValue());
      ArrayList<ModelCommand> newCommands = new ArrayList<>();

      cmd = new HaveRoot().setId("nRoot");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveSubUnit().setParent("nRoot").setId("root");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveRoot().setId("sub");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveLeaf().setParent("sub").setVTag("1.1").setId("c2");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveLeaf().setParent("leaf").setVTag("1.1").setId("c");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      assertThat(sub.getUp(), nullValue());
      JavaPackage nRoot = (JavaPackage) javaPackagesEditor.getModelObject("nRoot");

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesSecondRoot.svg",
            nRoot,
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // meanwhile at the JavaDocs
      ArrayList<javaPackagesToJavaDoc.JavaDoc.ModelCommand> newDocCommands = new ArrayList<>();
      docCmd = new javaPackagesToJavaDoc.JavaDoc.HaveLeaf().setParent("leaf").setVTag("1.2").setId("c3");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDoc.HaveLeaf().setParent("leaf").setVTag("1.2").setId("c");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesJavaDocAtVersion1.2.svg",
            javaDocEditor.getActiveCommands().values(),
            javaDocEditor.getMapOfModelObjects().values());

      // sync forward
      yaml = Yaml.encode(newCommands);
      javaDocEditor.loadYaml(yaml);
      leafDoc = (DocFile) javaDocEditor.getModelObject("leafDoc");
      assertThat(leafDoc.getContent(), is("leaf docu"));

      Folder nRootFolder = (Folder) javaDocEditor.getModelObject("nRoot");

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesSecondRootForward.svg",
            javaDocEditor.getActiveCommands().values(),
            nRootFolder,
            javaDocEditor.getMapOfModelObjects().values());

      // sync backward
      yaml = Yaml.encode(newDocCommands);
      javaPackagesEditor.loadYaml(yaml);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesSyncVersionBackward.svg",
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // remove nRoot
      newDocCommands.clear();

      docCmd = new javaPackagesToJavaDoc.JavaDoc.RemoveCommand().setId("nRoot");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDoc.RemoveCommand().setId("root");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaDocRootRemoved.svg",
            javaDocEditor.getActiveCommands().values(),
            javaDocEditor.getMapOfModelObjects().values());
      assertThat(nRootFolder.getSubFolders().isEmpty(), is(true));

      // sync remove
      yaml = Yaml.encode(newDocCommands);
      javaPackagesEditor.loadYaml(yaml);
      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesRootRemoved.svg",
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());
      assertThat(root.getUp(), nullValue());
   }

   private void addJavaDocComments(JavaDocEditor javaDocEditor)
   {
      javaPackagesToJavaDoc.JavaDoc.ModelCommand docCmd = new HaveContent().setContent("sub docu")
            .setOwner("subDoc").setId("subDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("leaf docu").setOwner("leafDoc").setId("leafDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("c docu").setOwner("c").setId("c.content"); // TODO: fix overwriting of haveLeaf
      javaDocEditor.execute(docCmd);
      DocFile leafDoc = (DocFile) javaDocEditor.getModelObject("leafDoc");
      assertThat(leafDoc.getContent(), is("leaf docu"));
   }

   private void buildJavaPackagesStartSituation(JavaPackagesEditor javaPackagesEditor)
   {
      // create package model
      ModelCommand cmd = new HaveRoot().setId("root");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("root").setId("sub");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("sub").setId("leaf");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("leaf").setVTag("1.0").setId("c");
      javaPackagesEditor.execute(cmd);
   }


}

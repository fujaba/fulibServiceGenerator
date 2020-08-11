package javaPackagesToJavaDoc;

import javaPackagesToJavaDoc.JavaDocWithPatterns.DocFile;
import javaPackagesToJavaDoc.JavaDocWithPatterns.Folder;
import javaPackagesToJavaDoc.JavaDocWithPatterns.HaveContent;
import javaPackagesToJavaDoc.JavaDocWithPatterns.JavaDocWithPatternsEditor;
import javaPackagesToJavaDoc.JavaPackagesWithPatterns.*;
import org.fulib.FulibTools;
import org.fulib.tables.ObjectTable;
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
public class TestPackageToDocWithPatterns implements PropertyChangeListener
{
   @Test
   public void testOneWaySync()
   {
      JavaPackagesWithPatternsEditor javaPackagesEditor = new JavaPackagesWithPatternsEditor();

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

      JavaDocWithPatternsEditor javaDocEditor = new JavaDocWithPatternsEditor();
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
      JavaPackagesWithPatternsEditor javaPackagesEditor = new JavaPackagesWithPatternsEditor();

      buildJavaPackagesStartSituation(javaPackagesEditor);

      JavaDocWithPatternsEditor javaDocEditor = new JavaDocWithPatternsEditor();
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
      JavaPackagesWithPatternsEditor javaPackagesEditor = new JavaPackagesWithPatternsEditor();

      buildJavaPackagesStartSituation(javaPackagesEditor);
      ModelCommand cmd;

      JavaPackage root = (JavaPackage) javaPackagesEditor.getModelObject("root");
      assertThat(root, notNullValue());
      JavaPackage sub = (JavaPackage) javaPackagesEditor.getModelObject("sub");
      assertThat(sub, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsScenarioStart.svg",
            root,
            javaPackagesEditor.getActiveCommands().values());

      ObjectTable objectTable = new ObjectTable("root", root)
            .expandLink("sub", JavaPackage.PROPERTY_subPackages)
            .expandLink("leaf", JavaPackage.PROPERTY_subPackages)
            .expandLink("c", JavaPackage.PROPERTY_classes);
      assertThat(objectTable.getTable().size(), is(1));

      // forward to doc model
      JavaDocWithPatternsEditor javaDocEditor = new JavaDocWithPatternsEditor();
      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());
      javaDocEditor.loadYaml(yaml);

      // add some docu content
      addJavaDocComments(javaDocEditor);

      javaPackagesToJavaDoc.JavaDocWithPatterns.ModelCommand docCmd;
      DocFile leafDoc;

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("root");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsScenarioFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      ObjectTable tableFocusedOnSubColumn = new ObjectTable("root", rootFolder)
            .expandLink("sub", Folder.PROPERTY_subFolders);
      tableFocusedOnSubColumn.expandLink("subDoc", Folder.PROPERTY_files);
      ObjectTable tableFocusedOnLeaf = tableFocusedOnSubColumn.expandLink("leaf", Folder.PROPERTY_subFolders);
      tableFocusedOnLeaf.expandLink("leafDoc", Folder.PROPERTY_files);
      assertThat(tableFocusedOnSubColumn.getTable().size(), is(2));
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

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsSecondRoot.svg",
            nRoot,
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // meanwhile at the JavaDocs
      ArrayList<javaPackagesToJavaDoc.JavaDocWithPatterns.ModelCommand> newDocCommands = new ArrayList<>();
      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.HaveLeaf().setParent("leaf").setVTag("1.2").setId("c3");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.HaveLeaf().setParent("leaf").setVTag("1.2").setId("c");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsJavaDocAtVersion1.2.svg",
            javaDocEditor.getActiveCommands().values(),
            javaDocEditor.getMapOfModelObjects().values());

      // sync forward
      yaml = Yaml.encode(newCommands);
      javaDocEditor.loadYaml(yaml);
      leafDoc = (DocFile) javaDocEditor.getModelObject("leafDoc");
      assertThat(leafDoc.getContent(), is("leaf docu"));

      Folder nRootFolder = (Folder) javaDocEditor.getModelObject("nRoot");

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsSecondRootForward.svg",
            javaDocEditor.getActiveCommands().values(),
            nRootFolder,
            javaDocEditor.getMapOfModelObjects().values());

      Folder docSub = (Folder) javaDocEditor.getModelObject("sub");
      assertThat(docSub.getUp(), nullValue());
      DocFile subDoc = (DocFile) javaDocEditor.getModelObject("subDoc");
      assertThat(subDoc, nullValue());


      // sync backward
      yaml = Yaml.encode(newDocCommands);
      javaPackagesEditor.loadYaml(yaml);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsSyncVersionBackward.svg",
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // remove nRoot
      newDocCommands.clear();

      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.RemoveCommand().setId("nRoot");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.RemoveCommand().setId("root");
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

   private void addJavaDocComments(JavaDocWithPatternsEditor javaDocEditor)
   {
      javaPackagesToJavaDoc.JavaDocWithPatterns.ModelCommand docCmd = new HaveContent().setContent("sub docu")
            .setOwner("subDoc").setId("subDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("leaf docu").setOwner("leafDoc").setId("leafDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("c docu").setOwner("c").setId("c.content"); // TODO: fix overwriting of haveLeaf
      javaDocEditor.execute(docCmd);
      DocFile leafDoc = (DocFile) javaDocEditor.getModelObject("leafDoc");
      assertThat(leafDoc.getContent(), is("leaf docu"));
   }

   private void buildJavaPackagesStartSituation(JavaPackagesWithPatternsEditor javaPackagesEditor)
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

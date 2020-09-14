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

      ModelCommand cmd = new HaveRoot().setId("org");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("org").setId("fulib");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("fulib").setVTag("1.0").setId("Editor");
      javaPackagesEditor.execute(cmd);

      Object root = javaPackagesEditor.getModelObject("org");
      assertThat(root, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/OneWaySyncStart.svg",
            root,
            javaPackagesEditor.getActiveCommands().values());

      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());

      JavaDocWithPatternsEditor javaDocEditor = new JavaDocWithPatternsEditor();
      javaDocEditor.loadYaml(yaml);

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("org");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/OneWaySyncFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      cmd = new HaveRoot().setId("Editorom");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("Editorom").setId("org");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveRoot().setId("fulib");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("fulib").setVTag("1.1").setId("Editorommand");
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
      JavaPackage com = new JavaPackage().setId("com");
      changedObjects.add(com);
      JavaPackage org = (JavaPackage) javaPackagesEditor.getModelObject("org");
      com.withSubPackages(org);
      JavaPackage fulib = (JavaPackage) javaPackagesEditor.getModelObject("fulib");
      fulib.setPPack(null);
      JavaClass command = new JavaClass().setPack(fulib).setVTag("1.1").setId("Command");
      changedObjects.add(command);
      JavaClass editor = (JavaClass) javaPackagesEditor.getModelObject("Editor");
      editor.setVTag("1.1");

      javaPackagesEditor.parse(changedObjects);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsAfterParsing.svg",
            com,
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      ModelCommand comCmd = javaPackagesEditor.getActiveCommands().get("com");
      assertThat(comCmd, instanceOf(HaveRoot.class));

      ModelCommand orgCmd = javaPackagesEditor.getActiveCommands().get("org");
      assertThat(orgCmd, instanceOf(HaveSubUnit.class));

      ModelCommand commandCmd = javaPackagesEditor.getActiveCommands().get("Command");
      assertThat(commandCmd, instanceOf(HaveLeaf.class));
   }

   @Test
   public void testFirstForwardExample()
   {
      JavaPackagesWithPatternsEditor javaPackagesEditor = new JavaPackagesWithPatternsEditor();

      buildJavaPackagesStartSituation(javaPackagesEditor);
      ModelCommand cmd;

      JavaPackage org = (JavaPackage) javaPackagesEditor.getModelObject("org");
      assertThat(org, notNullValue());
      JavaPackage fulib = (JavaPackage) javaPackagesEditor.getModelObject("fulib");
      assertThat(fulib, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsScenarioStart.svg",
            org,
            javaPackagesEditor.getActiveCommands().values());

      ObjectTable<Object> pathTable = new ObjectTable<>("org", org)
         .expandLink("org", "fulib", JavaPackage.PROPERTY_subPackages)
         .expandLink("fulib", "serv", JavaPackage.PROPERTY_subPackages)
         .expandLink("serv", "Editor", JavaPackage.PROPERTY_classes);
      assertThat(pathTable.rowCount(), is(1));

      // forward to doc model
      JavaDocWithPatternsEditor javaDocEditor = new JavaDocWithPatternsEditor();
      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());
      javaDocEditor.loadYaml(yaml);

      // add some docu content
      addJavaDocComments(javaDocEditor);

      javaPackagesToJavaDoc.JavaDocWithPatterns.ModelCommand docCmd;
      DocFile servDoc;

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("org");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsScenarioFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      ObjectTable<Object> docTable = new ObjectTable<>("org", rootFolder)
         .expandLink("org", "fulib", Folder.PROPERTY_subFolders)
         .expandLink("fulib", "fulibDoc", Folder.PROPERTY_files)
         .expandLink("fulib", "serv", Folder.PROPERTY_subFolders)
         .expandLink("serv", "servDoc", Folder.PROPERTY_files);
      assertThat(docTable.rowCount(), is(2));
      assertThat(javaDocEditor.getActiveCommands().size(), is(javaPackagesEditor.getActiveCommands().size() + 3));

      // some editing on packages
      assertThat(fulib.getPPack(), notNullValue());
      ArrayList<ModelCommand> newCommands = new ArrayList<>();

      cmd = new HaveRoot().setId("com");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveSubUnit().setParent("com").setId("org");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveRoot().setId("fulib");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveLeaf().setParent("fulib").setVTag("1.1").setId("Command");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      cmd = new HaveLeaf().setParent("serv").setVTag("1.1").setId("Editor");
      javaPackagesEditor.execute(cmd);
      newCommands.add(cmd);

      assertThat(fulib.getPPack(), nullValue());
      JavaPackage com = (JavaPackage) javaPackagesEditor.getModelObject("com");

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsSecondRoot.svg",
            com,
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // meanwhile at the JavaDocs
      ArrayList<javaPackagesToJavaDoc.JavaDocWithPatterns.ModelCommand> newDocCommands = new ArrayList<>();
      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.HaveLeaf().setParent("serv").setVTag("1.2").setId("Util");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.HaveLeaf().setParent("serv").setVTag("1.2").setId("Editor");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsJavaDocAtVersion1.2.svg",
            javaDocEditor.getActiveCommands().values(),
            javaDocEditor.getMapOfModelObjects().values());

      // sync forward
      yaml = Yaml.encode(newCommands);
      javaDocEditor.loadYaml(yaml);
      servDoc = (DocFile) javaDocEditor.getModelObject("servDoc");
      assertThat(servDoc.getContent(), is("serv docu"));

      Folder comFolder = (Folder) javaDocEditor.getModelObject("com");

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsSecondRootForward.svg",
            javaDocEditor.getActiveCommands().values(),
            comFolder,
            javaDocEditor.getMapOfModelObjects().values());

      Folder docSub = (Folder) javaDocEditor.getModelObject("fulib");
      assertThat(docSub.getPFolder(), nullValue());
      DocFile subDoc = (DocFile) javaDocEditor.getModelObject("fulibDoc");
      assertThat(subDoc, nullValue());


      // sync backward
      yaml = Yaml.encode(newDocCommands);
      yaml = Yaml.encode(javaDocEditor.getActiveCommands().values());
      javaPackagesEditor.loadYaml(yaml);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesWithPatternsSyncVersionBackward.svg",
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // remove com
      newDocCommands.clear();

      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.RemoveCommand().setId("com");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDocWithPatterns.RemoveCommand().setId("org");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaDocRootRemoved.svg",
            javaDocEditor.getActiveCommands().values(),
            javaDocEditor.getMapOfModelObjects().values());
      assertThat(comFolder.getSubFolders().isEmpty(), is(true));

      // sync remove
      yaml = Yaml.encode(newDocCommands);
      javaPackagesEditor.loadYaml(yaml);
      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesRootRemoved.svg",
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());
      assertThat(org.getPPack(), nullValue());
   }

   private void addJavaDocComments(JavaDocWithPatternsEditor javaDocEditor)
   {
      javaPackagesToJavaDoc.JavaDocWithPatterns.ModelCommand docCmd = new HaveContent().setContent("fulib docu")
            .setOwner("fulibDoc").setId("fulibDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("serv docu").setOwner("servDoc").setId("servDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("Editor docu").setOwner("Editor").setId("Editor.content"); // TODO: fix overwriting of haveLeaf
      javaDocEditor.execute(docCmd);
      DocFile leafDoc = (DocFile) javaDocEditor.getModelObject("servDoc");
      assertThat(leafDoc.getContent(), is("serv docu"));
   }

   private void buildJavaPackagesStartSituation(JavaPackagesWithPatternsEditor javaPackagesEditor)
   {
      // create package model
      ModelCommand cmd = new HaveRoot().setId("org");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("org").setId("fulib");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("fulib").setId("serv");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("serv").setVTag("1.0").setId("Editor");
      javaPackagesEditor.execute(cmd);
   }


}

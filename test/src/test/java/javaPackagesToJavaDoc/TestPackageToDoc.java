package javaPackagesToJavaDoc;

import javaPackagesToJavaDoc.JavaDoc.DocFile;
import javaPackagesToJavaDoc.JavaDoc.Folder;
import javaPackagesToJavaDoc.JavaDoc.HaveContent;
import javaPackagesToJavaDoc.JavaDoc.JavaDocEditor;
import javaPackagesToJavaDoc.JavaPackages.*;
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
public class TestPackageToDoc implements PropertyChangeListener
{
   @Test
   public void testOneWaySync()
   {
      JavaPackagesEditor javaPackagesEditor = new JavaPackagesEditor();

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

      JavaDocEditor javaDocEditor = new JavaDocEditor();
      javaDocEditor.loadYaml(yaml);

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("org");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/OneWaySyncFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      cmd = new HaveRoot().setId("com");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveSubUnit().setParent("com").setId("org");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveRoot().setId("fulib");
      javaPackagesEditor.execute(cmd);

      cmd = new HaveLeaf().setParent("fulib").setVTag("1.1").setId("c2");
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
      if (evt.getNewValue() == null) {
         return;
      }

      if (evt.getNewValue().getClass().isPrimitive()) {
         return;
      }

      if (evt.getNewValue().getClass().getName().startsWith("java.lang")) {
         return;
      }

      changedObjects.add(evt.getNewValue());
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
      JavaPackage com = new JavaPackage().setId("com");
      // changedObjects.add(com);
      JavaPackage org = (JavaPackage) javaPackagesEditor.getModelObject("org");
      com.withSubPackages(org);
      JavaPackage fulib = (JavaPackage) javaPackagesEditor.getModelObject("fulib");
      fulib.setPPack(null);
      JavaClass command = new JavaClass().setPack(fulib).setVTag("1.1").setId("Command");
      // changedObjects.add(command);
      JavaClass editor = (JavaClass) javaPackagesEditor.getModelObject("Editor");
      editor.setVTag("1.1");

      LinkedHashSet<Object> allObjects = Yaml.findAllObjects(com, fulib);
      javaPackagesEditor.parse(changedObjects);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesAfterParsing.svg",
            com,
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      ModelCommand comCmd = javaPackagesEditor.getActiveCommands().get("com");
      assertThat(comCmd, instanceOf(HaveRoot.class));

      ModelCommand orgCmd = javaPackagesEditor.getActiveCommands().get("org");
      assertThat(orgCmd, instanceOf(HaveSubUnit.class));

      ModelCommand editorCmd = javaPackagesEditor.getActiveCommands().get("Editor");
      assertThat(editorCmd, instanceOf(HaveLeaf.class));
   }

   @Test
   public void testFirstForwardExample()
   {
      JavaPackagesEditor javaPackagesEditor = new JavaPackagesEditor();

      buildJavaPackagesStartSituation(javaPackagesEditor);
      ModelCommand cmd;

      JavaPackage org = (JavaPackage) javaPackagesEditor.getModelObject("org");
      assertThat(org, notNullValue());
      JavaPackage fulib = (JavaPackage) javaPackagesEditor.getModelObject("fulib");
      assertThat(fulib, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesScenarioStart.svg",
            org,
            javaPackagesEditor.getActiveCommands().values());

      ObjectTable<Object> pathTable = new ObjectTable<>("org", org)
         .expandLink("org", "fulib", javaPackagesToJavaDoc.JavaPackagesWithPatterns.JavaPackage.PROPERTY_subPackages)
         .expandLink("fulib", "serv", javaPackagesToJavaDoc.JavaPackagesWithPatterns.JavaPackage.PROPERTY_subPackages)
         .expandLink("serv", "Editor", javaPackagesToJavaDoc.JavaPackagesWithPatterns.JavaPackage.PROPERTY_classes);
      assertThat(pathTable.rowCount(), is(1));

      // forward to doc model
      JavaDocEditor javaDocEditor = new JavaDocEditor();
      String yaml = Yaml.encode(javaPackagesEditor.getActiveCommands().values());
      javaDocEditor.loadYaml(yaml);

      // add some docu content
      addJavaDocComments(javaDocEditor);

      javaPackagesToJavaDoc.JavaDoc.ModelCommand docCmd;
      DocFile servDoc;

      Folder rootFolder = (Folder) javaDocEditor.getModelObject("org");
      assertThat(rootFolder, notNullValue());

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesScenarioFirstForward.svg",
            javaDocEditor.getActiveCommands().values(),
            rootFolder);

      ObjectTable<Object> docTable = new ObjectTable<>("org", rootFolder)
         .expandLink("org", "fulib", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_subFolders)
         .expandLink("fulib", "fulibDoc", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_files)
         .expandLink("fulib", "serv", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_subFolders)
         .expandLink("serv", "servDoc", javaPackagesToJavaDoc.JavaDocWithPatterns.Folder.PROPERTY_files);
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

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesSecondRoot.svg",
            com,
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // meanwhile at the JavaDocs
      ArrayList<javaPackagesToJavaDoc.JavaDoc.ModelCommand> newDocCommands = new ArrayList<>();
      docCmd = new javaPackagesToJavaDoc.JavaDoc.HaveLeaf().setParent("serv").setVTag("1.2").setId("Util");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDoc.HaveLeaf().setParent("serv").setVTag("1.2").setId("Editor");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesJavaDocAtVersion1.2.svg",
            javaDocEditor.getActiveCommands().values(),
            javaDocEditor.getMapOfModelObjects().values());

      // sync forward
      yaml = Yaml.encode(newCommands);
      javaDocEditor.loadYaml(yaml);
      servDoc = (DocFile) javaDocEditor.getModelObject("servDoc");
      assertThat(servDoc.getContent(), is("serv docu"));

      Folder comFolder = (Folder) javaDocEditor.getModelObject("com");

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesSecondRootForward.svg",
            javaDocEditor.getActiveCommands().values(),
            comFolder,
            javaDocEditor.getMapOfModelObjects().values());

      // sync backward
      yaml = Yaml.encode(newDocCommands);
      javaPackagesEditor.loadYaml(yaml);

      FulibTools.objectDiagrams().dumpSVG("tmp/JavaPackagesSyncVersionBackward.svg",
            javaPackagesEditor.getMapOfModelObjects().values(),
            javaPackagesEditor.getActiveCommands().values());

      // remove com
      newDocCommands.clear();

      docCmd = new javaPackagesToJavaDoc.JavaDoc.RemoveCommand().setId("com");
      javaDocEditor.execute(docCmd);
      newDocCommands.add(docCmd);

      docCmd = new javaPackagesToJavaDoc.JavaDoc.RemoveCommand().setId("org");
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

   private void addJavaDocComments(JavaDocEditor javaDocEditor)
   {
      javaPackagesToJavaDoc.JavaDoc.ModelCommand docCmd = new HaveContent().setContent("fulib docu")
            .setOwner("fulibDoc").setId("fulibDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("serv docu").setOwner("servDoc").setId("servDoc");
      javaDocEditor.execute(docCmd);
      docCmd = new HaveContent().setContent("Editor docu").setOwner("Editor").setId("Editor.content"); // TODO: fix overwriting of haveLeaf
      javaDocEditor.execute(docCmd);
      DocFile servDoc = (DocFile) javaDocEditor.getModelObject("servDoc");
      assertThat(servDoc.getContent(), is("serv docu"));
   }

   private void buildJavaPackagesStartSituation(JavaPackagesEditor javaPackagesEditor)
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

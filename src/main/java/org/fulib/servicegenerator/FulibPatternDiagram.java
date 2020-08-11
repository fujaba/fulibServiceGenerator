package org.fulib.servicegenerator;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.fulib.tables.ObjectTable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class FulibPatternDiagram
{
   public void dump(String diagramFileName, Object pattern) {
      STGroupFile group = new STGroupFile(this.getClass().getResource("templates/patternDiagram.stg"));

      ObjectTable tableFocusedOnPattern = new ObjectTable(pattern);
      ObjectTable tableFocusedOnObjects = tableFocusedOnPattern.expandLink("object", "objects");
      tableFocusedOnObjects.expandString("kind", "kind").filter(k -> k.equals("core"));
      LinkedHashSet coreObjects = tableFocusedOnObjects.toSet();

      tableFocusedOnPattern = new ObjectTable(pattern);
      tableFocusedOnObjects = tableFocusedOnPattern.expandLink("object", "objects");
      tableFocusedOnObjects.expandString("kind", "kind").filter(k -> k.equals("context"));
      LinkedHashSet contextObjects = tableFocusedOnObjects.toSet();

      tableFocusedOnPattern = new ObjectTable(pattern);
      tableFocusedOnObjects = tableFocusedOnPattern.expandLink("object", "objects");
      tableFocusedOnObjects.expandString("kind", "kind").filter(k -> k.equals("nac"));
      LinkedHashSet nacObjects = tableFocusedOnObjects.toSet();

      tableFocusedOnObjects = new ObjectTable(pattern).expandLink("object", "objects");
      ObjectTable tableFocusedOnLinks = tableFocusedOnObjects.expandLink("link", "links");
      tableFocusedOnLinks.expandString("linkKind", "kind").filter(k -> k.equals("core"));
      LinkedHashSet coreLinks = tableFocusedOnLinks.toSet();

      tableFocusedOnObjects = new ObjectTable(pattern).expandLink("object", "objects");
      tableFocusedOnLinks = tableFocusedOnObjects.expandLink("link", "links");
      tableFocusedOnLinks.expandString("linkKind", "kind").filter(k -> k.equals("context"));
      LinkedHashSet contextLinks = tableFocusedOnLinks.toSet();

      tableFocusedOnObjects = new ObjectTable(pattern).expandLink("object", "objects");
      tableFocusedOnLinks = tableFocusedOnObjects.expandLink("link", "links");
      tableFocusedOnLinks.expandString("linkKind", "kind").filter(k -> k.equals("nac"));
      LinkedHashSet nacLinks = tableFocusedOnLinks.toSet();

      ST st = group.getInstanceOf("patternDiagram");
      st.add("coreObjects", coreObjects);
      st.add("contextObjects", contextObjects);
      st.add("nacObjects", nacObjects);
      st.add("coreLinks", coreLinks);
      st.add("contextLinks", contextLinks);
      st.add("nacLinks", nacLinks);
      String dotString = st.render();

      try {
         Graphviz.fromString(dotString).scale(1.0).render(Format.SVG).toFile(new File(diagramFileName));
      }
      catch (IOException e) {
         e.printStackTrace();
      }


   }
}

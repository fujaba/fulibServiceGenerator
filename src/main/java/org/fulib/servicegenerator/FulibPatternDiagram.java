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
      ObjectTable patternTable = new ObjectTable(pattern);
      ObjectTable objectTable = patternTable.expandLink("object", "objects");
      objectTable.expandString("kind", "kind").filter(k -> k.equals("core"));
      LinkedHashSet coreObjects = objectTable.toSet();
      patternTable = new ObjectTable(pattern);
      objectTable = patternTable.expandLink("object", "objects");
      objectTable.expandString("kind", "kind").filter(k -> ! k.equals("core"));
      LinkedHashSet contextObjects = objectTable.toSet();
      objectTable = new ObjectTable(pattern).expandLink("object", "objects").expandLink("link", "links");
      LinkedHashSet links = objectTable.toSet();

      ST st = group.getInstanceOf("patternDiagram");
      st.add("coreObjects", coreObjects);
      st.add("contextObjects", contextObjects);
      st.add("links", links);
      String dotString = st.render();

      try {
         Graphviz.fromString(dotString).scale(1.0).render(Format.SVG).toFile(new File(diagramFileName));
      }
      catch (IOException e) {
         e.printStackTrace();
      }


   }
}

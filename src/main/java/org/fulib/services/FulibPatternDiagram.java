package org.fulib.services;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.fulib.tables.ObjectTable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FulibPatternDiagram
{
   public void dump(String diagramFileName, Object pattern) {
      STGroupFile group = new STGroupFile(this.getClass().getResource("templates/patternDiagram.stg"));

      final Set<?> coreObjects = new ObjectTable<>("pattern", pattern)
         .expandLink("pattern", "object", "objects")
         .expandLink("object", "kind", "kind")
         .filter("kind", "core"::equals)
         .toSet("object");

      final Set<?> contextObjects = new ObjectTable<>("pattern", pattern)
         .expandLink("pattern", "objects", "object")
         .expandLink("object", "kind", "kind")
         .filter("kind", "context"::equals)
         .toSet("object");

      final Set<?> nacObjects = new ObjectTable<>("pattern", pattern)
         .expandLink("pattern", "object", "objects")
         .expandAttribute("object", "kind", "kind")
         .filter("kind", "nac"::equals)
         .toSet("object");

      final Set<?> coreLinks = new ObjectTable<>("pattern", pattern)
         .expandLink("pattern", "object", "objects")
         .expandLink("object", "link", "links")
         .expandAttribute("link", "kind", "kind")
         .filter("kind", "core"::equals)
         .toSet("link");

      final Set<?> contextLinks = new ObjectTable<>("pattern", pattern)
         .expandLink("pattern", "object", "objects")
         .expandLink("object", "link", "links")
         .expandAttribute("link", "kind", "kind")
         .filter("kind", "context"::equals)
         .toSet("link");

      final Set<?> nacLinks = new ObjectTable<>("pattern", pattern)
         .expandLink("pattern", "object", "objects")
         .expandLink("object", "link", "links")
         .expandAttribute("link", "kind", "kind")
         .filter("kind", "nac"::equals)
         .toSet("link");

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

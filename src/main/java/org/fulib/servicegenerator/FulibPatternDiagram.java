package org.fulib.servicegenerator;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.fulib.tables.PathTable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FulibPatternDiagram
{
   public void dump(String diagramFileName, Object pattern) {
      STGroupFile group = new STGroupFile(this.getClass().getResource("templates/patternDiagram.stg"));

      PathTable pathTable = new PathTable("pattern", pattern)
            .expand("pattern", "objects", "object")
            .expand("object", "kind", "kind")
            .filter("kind", k -> k.equals("core"));

      Set<?> coreObjects = pathTable.toSet("object");

      pathTable = new PathTable("pattern", pattern)
            .expand("pattern", "objects", "object")
            .expand("object", "kind", "kind")
            .filter("kind", k -> k.equals("context"));

      Set<?> contextObjects = pathTable.toSet("object");

      pathTable = new PathTable("pattern", pattern)
            .expand("pattern", "objects", "object")
            .expand("object", "kind", "kind")
            .filter("kind", k -> k.equals("nac"));
      Set<?> nacObjects = pathTable.toSet("object");

      pathTable = new PathTable("pattern", pattern);
      pathTable.expand("pattern", "objects", "object");
      pathTable.expand("object", "links", "link");
      pathTable.expand("link", "kind", "kind");
      pathTable.filter("kind", k -> k.equals("core"));
      Set<?> coreLinks = pathTable.toSet("link");

      pathTable = new PathTable("pattern", pattern)
            .expand("pattern", "objects", "object")
            .expand("object", "links", "link")
            .expand("link", "kind", "kind")
            .filter("kind", k -> k.equals("context"));
      Set<?> contextLinks = pathTable.toSet("link");

      pathTable = new PathTable("pattern", pattern)
            .expand("pattern", "objects", "object")
            .expand("object", "links", "link")
            .expand("link", "kind", "kind")
            .filter("kind", k -> k.equals("nac"));
      Set<?> nacLinks = pathTable.toSet("link");

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

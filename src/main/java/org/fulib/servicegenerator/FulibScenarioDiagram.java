package org.fulib.servicegenerator;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FulibScenarioDiagram
{
   private final STGroupFile group;
   private String htmlFileName;
   private String lanes;

   private LinkedHashMap<String, ArrayList<String>> laneMap = new LinkedHashMap<>();
   private ArrayList<Object> services = new ArrayList<>();
   private Object currentService;
   private Object modelEditor;

   public FulibScenarioDiagram() {
      group = new STGroupFile("org.fulib.templates/scenariodiagram.stg", "UTF-8", '$', '$');
   }

   public FulibScenarioDiagram setHtmlFileName(String htmlFileName)
   {
      this.htmlFileName = htmlFileName;
      return this;
   }


   public FulibScenarioDiagram addServices(Object... services)
   {
      this.services.addAll(Arrays.asList(services));
      return this;
   }

   public FulibScenarioDiagram dump()
   {
      computeLanes();


      // put all together
      ST st = group.getInstanceOf("overallFile");
      st.add("lanes", lanes);
      String body = st.render();

      try {
         Files.writeString(Paths.get(htmlFileName), body, StandardOpenOption.CREATE);
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      // load html template
      return this;
   }

   private void computeLanes()
   {
      StringBuilder buf = new StringBuilder();

      for (Object service : services) {
         ReflectorMap reflectorMap = new ReflectorMap(service.getClass().getPackage().getName());
         Reflector reflector = reflectorMap.getReflector(service);

         String serviceName = getServiceName(service);

         ArrayList<String> entryList = laneMap.computeIfAbsent(serviceName, n -> new ArrayList<>());
         String laneEntries = String.join("\n",entryList);

         ST st = group.getInstanceOf("oneLane");
         st.add("name", serviceName);
         st.add("entries", laneEntries);
         String body = st.render();

         buf.append(body);
      }

      lanes = buf.toString();
   }



   private String getServiceName(Object element)
   {
      String simpleName = element.getClass().getSimpleName();
      if (simpleName.endsWith("Service")) {
         return simpleName.substring(0, simpleName.length() - "Service".length());
      }
      else if (simpleName.endsWith("App")) {
         return simpleName.substring(0, simpleName.length() - "App".length());
      }
      else {
         return simpleName;
      }
   }

   public FulibScenarioDiagram addScreen(String time, Object app)
   {
      String serviceName = getServiceName(app);
      ArrayList<String> entryList = laneMap.computeIfAbsent(serviceName, n -> new ArrayList<>());

      StringBuilder lines = new StringBuilder();

      ReflectorMap reflectorMap = new ReflectorMap(app.getClass().getPackage().getName());

      computeLines(lines, app, reflectorMap);

      ST st = group.getInstanceOf("oneScreen");
      st.add("time", time);
      st.add("lines", lines.toString());
      String body = st.render();

      entryList.add(body);

      return this;
   }

   private void computeLines(StringBuilder lines, Object node, ReflectorMap reflectorMap)
   {
      // get description
      Reflector reflector = reflectorMap.getReflector(node);
      Object description = reflector.getValue(node, "description");
      ArrayList<String> entryList = new ArrayList<>();
      if (description != null) {
         String line = (String) description;
         String[] split = line.split("\\|");
         for (String entry : split) {
            entry = entry.trim();
            if (entry.startsWith("button")) {
               String text = entry.substring("button ".length());
               entryList.add(String.format("[%s]", text));
            }
            else if (entry.startsWith("input")) {
               String word = entry.substring("input ".length());
               entryList.add(String.format("<u>%s</u>", word));
            }
            else {
               entryList.add(entry);
            }
         }
         if (entryList.size() > 0) {
            String entryText = String.join(" ", entryList);
            lines.append("<p align=\"center\">").append(entryText).append("</p>\n");
         }
      }

      // get content
      Object content = reflector.getValue(node, "content");
      if (content != null & content instanceof Collection) {
         Collection kids = (Collection) content;
         for (Object kid : kids) {
            computeLines(lines, kid, reflectorMap);
         }
      }
      else if (content != null) {
         computeLines(lines, content, reflectorMap);
      }
   }
}

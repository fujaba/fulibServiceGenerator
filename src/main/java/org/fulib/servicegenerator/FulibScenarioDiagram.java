package org.fulib.servicegenerator;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import static com.codeborne.selenide.Selenide.*;


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
         Files.write(Paths.get(htmlFileName), body.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
         int maxNoOfLines = 1;
         int i = 900;
         int maxLength = "<p align=\"center\">We have 10 products on stock</p>".length();
         for (String entry : entryList) {
            int lines = 0;
            String[] split = entry.split("\\n");
            for (String s : split) {
               s = s.trim();
               int length = s.length();
               int newLines = 1;
               if (s.startsWith("<p align=\"center\">") && length >= maxLength) {
                  newLines += length / maxLength;
               }
               lines += newLines;
            }
            System.out.println("" + i++ + " " + lines);
            maxNoOfLines = Math.max(maxNoOfLines, lines);

         }

         maxNoOfLines -= 3; // do not count <div>, \n, </div>
         System.out.println(maxNoOfLines);

         int laneHeaderLines = 1;
         int lineHeight = 20;
         int paddingAndBorder = 12;
         int laneHeight = maxNoOfLines * lineHeight + paddingAndBorder + laneHeaderLines * lineHeight + paddingAndBorder;

         String laneEntries = String.join("\n",entryList);

         ST st = group.getInstanceOf("oneLane");
         st.add("name", serviceName);
         st.add("height", "" + laneHeight);
         st.add("entries", laneEntries);
         String body = st.render();

         buf.append(body);

         // plus messagelane
         st = group.getInstanceOf("oneMsgLane");
         st.add("name", serviceName);
         st.add("entries", "msglaneEntries");
         body = st.render();

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

   public FulibScenarioDiagram addScreen(String time, Object app, String... buttonWithMousePointer)
   {
      String serviceName = getServiceName(app);
      ArrayList<String> entryList = laneMap.computeIfAbsent(serviceName, n -> new ArrayList<>());

      String title = serviceName;
      Reflector reflector = new Reflector().setClazz(app.getClass());
      Object description = reflector.getValue(app, "description");
      if (description != null) {
         title = " " + description;
         app = reflector.getValue(app, "content");
      }

      StringBuilder lines = new StringBuilder();

      ReflectorMap reflectorMap = new ReflectorMap(app.getClass().getPackage().getName());

      computeLines(lines, app, reflectorMap, buttonWithMousePointer);

      ST st = group.getInstanceOf("oneScreen");
      st.add("time", time);
      st.add("title", title);
      st.add("lines", lines.toString());
      String body = st.render();

      entryList.add(body);

      return this;
   }

   private void computeLines(StringBuilder lines, Object node, ReflectorMap reflectorMap, String... buttonWithMousePointer)
   {
      String allButtonsWithMousePointer = "";
      if (buttonWithMousePointer != null) {
         allButtonsWithMousePointer += String.join(" ", buttonWithMousePointer);
      }
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
               String buttonText = String.format("[%s]", text);
               if (allButtonsWithMousePointer.indexOf(buttonText) >= 0) {
                  buttonText += "<i class=\"fa fa-hand-o-left\"></i>";
               }
               entryList.add(buttonText);
            }
            else if (entry.startsWith("input")) {
               // prompt text
               String word = entry.substring("input ".length());
               // prototype value
               Object value = reflector.getValue(node, "value");
               if (value != null) {
                  word = value.toString();
               }
               // gui value
               try {
                  String id = (String) reflector.getValue(node, "id");
                  word = $("#" + id).$("input").getValue();
               }
               catch (Exception e) {
                  // nice try
               }
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
            computeLines(lines, kid, reflectorMap, buttonWithMousePointer);
         }
      }
      else if (content != null) {
         computeLines(lines, content, reflectorMap, buttonWithMousePointer);
      }
   }

   public FulibScenarioDiagram addData(String time, String lane, Collection objects)
   {
      StringBuilder lines = new StringBuilder();

      if (objects.size() == 0) {
         lines.append("<p> no data </p>\n");
      }
      else {
         for (Object object : objects) {
            lines.append("<p>-");
            Class<?> clazz = object.getClass();
            Reflector reflector = new Reflector().setClassName(object.getClass().getName()).setClazz(clazz);
            for (String property : reflector.getProperties()) {
               Object value = reflector.getValue(object, property);
               if (value != null && (value.getClass().isPrimitive() || value.getClass().getName().startsWith("java.lang"))) {
                  lines.append(" ").append(value);
               }
            }
            lines.append("</p>\n");
         }
      }

      ST st = group.getInstanceOf("oneData");
      st.add("time", time);
      st.add("data", lines.toString());
      String body = st.render();

      ArrayList<String> entries = laneMap.computeIfAbsent(lane, l -> new ArrayList<>());
      entries.add(body);

      return this;
   }

   public FulibScenarioDiagram addMessages(String time, Object source)
   {
      // find streams and messages from source

      // add to msg lanes
      return this;
   }
}

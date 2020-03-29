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
   private LinkedHashMap<String, Integer> laneToIndentMap = new LinkedHashMap<>();
   private LinkedHashMap<String, ArrayList<String>> msgLaneMap = new LinkedHashMap<>();
   private LinkedHashMap<String, Integer> msgLaneToIndentMap = new LinkedHashMap<>();

   private LinkedHashMap<String, String> streamToLastCommandTimeMap = new LinkedHashMap<>();
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
         int maxNoOfLines = getMaxNoOfLines(entryList);

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
         ArrayList<String> messageList = msgLaneMap.computeIfAbsent(serviceName, n -> new ArrayList<>());
         maxNoOfLines = getMaxNoOfLines(messageList);
         laneHeight = maxNoOfLines * lineHeight + paddingAndBorder * 2;

         String messageEntries =  String.join("\n", messageList);
         st = group.getInstanceOf("oneMsgLane");
         st.add("name", serviceName);
         st.add("height", "" + laneHeight);
         st.add("entries", messageEntries);
         body = st.render();

         buf.append(body);

      }

      lanes = buf.toString();
   }

   private int getMaxNoOfLines(ArrayList<String> entryList)
   {
      int maxNoOfLines = 1;
      int maxLength = "<p align=\"center\">We have 10 products on stock</p>".length();
      int maxDataLength = "<p>order_1 Pumps Boots order-</p>".length();
      for (String entry : entryList) {
         int lines = 0;
         String[] split = entry.split("\\n");
         for (String s : split) {
            s = s.trim();
            int length = s.length();
            int newLines = 1;
            if (s.startsWith("<p>-") && length >= maxDataLength) {
               newLines += length / maxDataLength;
            }
            else if (s.startsWith("<p align=\"center\">") && length >= maxLength) {
               newLines += length / maxLength;
            }
            lines += newLines;
         }
         maxNoOfLines = Math.max(maxNoOfLines, lines);

      }

      maxNoOfLines -= 3; // do not count <div>, \n, </div>
      System.out.println(maxNoOfLines);
      return maxNoOfLines;
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
      return addScreen(time, 0, app, buttonWithMousePointer);
   }

   public FulibScenarioDiagram addScreen(String time, int indent, Object app, String... buttonWithMousePointer)
   {
      try {
         Thread.sleep(200);
      }
      catch (InterruptedException e) {
         // no problem
      }
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

      int oldIndent = laneToIndentMap.computeIfAbsent(serviceName, s -> 0);
      indent += oldIndent;
      laneToIndentMap.put(serviceName, indent);


      ST st = group.getInstanceOf("oneScreen");
      st.add("time", time);
      st.add("indent", indent);
      st.add("title", title);
      st.add("lines", lines.toString());
      String body = st.render();

      entryList.add(body);

      dump();

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
      return addData(time, 0, lane, objects);
   }

   public FulibScenarioDiagram addData(String time, int indent, String lane, Collection objects)
   {
      StringBuilder lines = new StringBuilder();

      if (objects.size() == 0) {
         lines.append("<p> no data </p>\n");
      }
      else {
         for (Object object : objects) {
            Class<?> clazz = object.getClass();
            Reflector reflector = new Reflector().setClassName(object.getClass().getName()).setClazz(clazz);
            String id = (String) reflector.getValue(object, "id");
            if (id != null) {
               lines.append("<p>- ").append(id);
            }
            else {
               lines.append("<p>-");
            }
            for (String property : reflector.getProperties()) {
               if ("id".equals(property) || "time".equals(property)) {
                  continue;
               }
               Object value = reflector.getValue(object, property);
               if (value != null && (value.getClass().isPrimitive() || value.getClass().getName().startsWith("java.lang"))) {
                  lines.append(" ").append(value);
               }
            }
            lines.append("</p>\n");
         }
      }

      int oldIndent = laneToIndentMap.computeIfAbsent(lane, s -> 0);
      indent += oldIndent;
      laneToIndentMap.put(lane, indent);

      ST st = group.getInstanceOf("oneData");
      st.add("time", time);
      st.add("indent", indent);
      st.add("data", lines.toString());
      String body = st.render();

      ArrayList<String> entries = laneMap.computeIfAbsent(lane, l -> new ArrayList<>());
      entries.add(body);
      dump();
      return this;
   }

   public FulibScenarioDiagram addMessages(String time, Object source)
   {
      addMessages(time, source, 0);
      return this;
   }
   public FulibScenarioDiagram addMessages(String time, Object source, int indent)
   {
      try {
         Thread.sleep(200);
      }
      catch (InterruptedException e) {
         // no problem
      }
      String serviceName = getServiceName(source);

      // find streams and messages from source
      ReflectorMap reflectorMap = new ReflectorMap(source.getClass().getPackage().getName());
      Reflector sourceReflector = reflectorMap.getReflector(source);
      Collection streams = (Collection) sourceReflector.getValue(source, "streams");
      Reflector streamReflector = null;
      for (Object stream : streams) {
         if (streamReflector == null) {
            streamReflector = new Reflector().setClazz(stream.getClass());
         }
         String targetUrl = (String) streamReflector.getValue(stream, "targetUrl");
         int pos = targetUrl.lastIndexOf('/');
         String streamName = targetUrl.substring(pos + 1);

         // find message lane
         StringBuilder lines = new StringBuilder();
         Collection commands = (Collection) streamReflector.getValue(stream, "oldCommands");
         String lastCmdTime = streamToLastCommandTimeMap.computeIfAbsent(streamName, t -> "0000");
         for (Object command : commands) {
            Reflector reflector = reflectorMap.getReflector(command);
            String newCmdTime = (String) reflector.getValue(command, "time");
            if (lastCmdTime.compareTo(newCmdTime) >= 0) {
               // command is already part of some other message
               continue;
            }
            String id = (String) reflector.getValue(command, "id");
            if (id != null) {
               lines.append("<p>- ").append(id);
            }
            else {
               lines.append("<p>-");
            }
            for (String property : reflector.getProperties()) {
               if ("id".equals(property)) {
                  continue;
               }
               Object value = reflector.getValue(command, property);
               if (property.equals("time")) {
                  streamToLastCommandTimeMap.put(streamName, value.toString());
                  continue;
               }
               if (value != null && (value.getClass().isPrimitive() || value.getClass().getName().startsWith("java.lang"))) {
                  lines.append(" ").append(value);
               }
            }
            lines.append("</p>\n");
         }

         int oldIndent = msgLaneToIndentMap.computeIfAbsent(streamName, s -> 0);
         indent += oldIndent;
         msgLaneToIndentMap.put(streamName, indent);

         // add to msg lanes
         ST st = group.getInstanceOf("oneMessage");
         st.add("time", time + " " + streamName);
         st.add("indent", "" + indent);
         st.add("data", lines.toString());
         String body = st.render();

         ArrayList<String> msgEntryList = findMessageLaneName(streamName);
         msgEntryList.add(body);
      }
      dump();
      return this;
   }

   private ArrayList<String> findMessageLaneName(String streamName)
   {
      String messageLaneName = null;
      for (String laneName : laneMap.keySet()) {
         if (streamName.contains(laneName)) {
            messageLaneName = laneName;
            break;
         }
      }

      ArrayList<String> msgLaneEntries = msgLaneMap.computeIfAbsent(messageLaneName, n -> new ArrayList<>());
      return msgLaneEntries;
   }
}

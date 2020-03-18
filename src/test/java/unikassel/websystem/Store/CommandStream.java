package unikassel.websystem.Store;

import org.fulib.yaml.Yaml;
import spark.Request;
import spark.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;

import static spark.Spark.post;

public class CommandStream
{
   private String targetUrl;
   private StoreService service;

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();


   public CommandStream start(String streamName, String targetUrl, StoreService service) {
      this.targetUrl = targetUrl;
      this.service = service;
      post("/" + streamName, (req, res) -> handlePostRequest(req, res));
      return this;
   }

   private String handlePostRequest(Request req, Response res)
   {
      String body = req.body();
      LinkedHashMap<String, Object> commandMap = Yaml.forPackage(this.getClass().getPackage().getName())
            .decode(body);

      Collection values = commandMap.values();
      executeCommands(values);

      return "OK";
   }

   private void executeCommands(Collection values)
   {
      for (Object value : values) {
         try {
            ModelCommand cmd = (ModelCommand) value;
            this.service.getExecutor().submit(() -> cmd.run(this.service.getModelEditor()));
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public void publish(ModelCommand cmd) {
      String yaml = Yaml.encode(cmd);
      System.out.println("Publishing: \n" + yaml);
      activeCommands.put(cmd.getId(), cmd);

      send();
   }

   public void send()
   {
      try {
         String yaml = Yaml.encode(activeCommands.values());
         URL url = new URL(targetUrl);
         HttpURLConnection con = (HttpURLConnection) url.openConnection();
         con.setRequestMethod("POST");
         con.setDoOutput(true);
         DataOutputStream out = new DataOutputStream(con.getOutputStream());
         out.writeBytes(yaml);
         out.flush();

         InputStream inputStream = con.getInputStream();
         InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
         BufferedReader in = new BufferedReader(inputStreamReader);
         String inputLine;
         StringBuffer content = new StringBuffer();
         while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
         }
         in.close();
         out.close();
         con.disconnect();

         // got an answer, clear active commands
         activeCommands.clear();
         LinkedHashMap<String, Object> map = Yaml.forPackage(service.getClass().getPackage().getName())
               .decode(yaml);
         executeCommands(map.values());
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public CommandStream subscribe(StoreService service) {
      this.service = service;
      return this;
   }
}

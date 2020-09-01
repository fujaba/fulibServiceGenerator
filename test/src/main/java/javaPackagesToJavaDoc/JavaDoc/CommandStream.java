package javaPackagesToJavaDoc.JavaDoc;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.fulib.yaml.Yaml;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import java.util.*;
import spark.Service;
import spark.Request;
import spark.Response;
import java.util.Objects;

public class CommandStream
{

   public static final String PROPERTY_service = "service";

   private JavaDocService service;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_activeCommands = "activeCommands";
   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();
   public static final String PROPERTY_targetUrlList = "targetUrlList";
   private List<String> targetUrlList = new ArrayList<>();
   public static final String PROPERTY_oldCommands = "oldCommands";
   private List<ModelCommand> oldCommands = new ArrayList<>();

   public JavaDocService getService()
   {
      return this.service;
   }

   public CommandStream setService(JavaDocService value)
   {
      if (this.service == value)
      {
         return this;
      }

      final JavaDocService oldValue = this.service;
      if (this.service != null)
      {
         this.service = null;
         oldValue.withoutStreams(this);
      }
      this.service = value;
      if (value != null)
      {
         value.withStreams(this);
      }
      this.firePropertyChange(PROPERTY_service, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   public void removeYou()
   {
      this.setService(null);
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public void publish(ModelCommand cmd)
   {
      String yaml = Yaml.encode(cmd);
      activeCommands.put(cmd.getId(), cmd);
      oldCommands.add(cmd);
      send();
   }

   public void send()
   {
      String yaml = Yaml.encode(activeCommands.values());
      for (String targetUrl : targetUrlList) {
         try {
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
            LinkedHashMap<String,Object> map = Yaml.forPackage(service.getClass().getPackage().getName())
                  .decode(content.toString());
            executeCommands(map.values());

         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public void executeCommands(Collection values)
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

   public CommandStream start()
   {
      service.getSpark().post("/" + name, (req, res) -> handlePostRequest(req, res));
      return this;
   }

   private String handlePostRequest(Request req, Response res)
   {
      String body = req.body();
      LinkedHashMap<String,Object> commandMap = Yaml.forPackage(this.getClass().getPackage().getName())
            .decode(body);

      Collection values = commandMap.values();
      executeCommands(values);

      return "OK";
   }

   public void addCommandsToBeStreamed(String... commandList) { 
      for (String cmd : commandList) {
         service.getModelEditor().addCommandListener(cmd, this);
      }
   }

   public String getName()
   {
      return this.name;
   }

   public CommandStream setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_name, oldValue, value);
      return this;
   }

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return this.activeCommands;
   }

   public CommandStream setActiveCommands(java.util.Map<String, ModelCommand> value)
   {
      if (Objects.equals(value, this.activeCommands))
      {
         return this;
      }

      final java.util.Map<String, ModelCommand> oldValue = this.activeCommands;
      this.activeCommands = value;
      this.firePropertyChange(PROPERTY_activeCommands, oldValue, value);
      return this;
   }

   public List<String> getTargetUrlList()
   {
      return this.targetUrlList;
   }

   public CommandStream setTargetUrlList(List<String> value)
   {
      if (Objects.equals(value, this.targetUrlList))
      {
         return this;
      }

      final List<String> oldValue = this.targetUrlList;
      this.targetUrlList = value;
      this.firePropertyChange(PROPERTY_targetUrlList, oldValue, value);
      return this;
   }

   public List<ModelCommand> getOldCommands()
   {
      return this.oldCommands;
   }

   public CommandStream setOldCommands(List<ModelCommand> value)
   {
      if (Objects.equals(value, this.oldCommands))
      {
         return this;
      }

      final List<ModelCommand> oldValue = this.oldCommands;
      this.oldCommands = value;
      this.firePropertyChange(PROPERTY_oldCommands, oldValue, value);
      return this;
   }

}
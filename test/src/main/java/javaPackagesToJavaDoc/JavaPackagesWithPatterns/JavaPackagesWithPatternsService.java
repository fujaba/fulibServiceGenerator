package javaPackagesToJavaDoc.JavaPackagesWithPatterns;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import spark.Service;
import org.fulib.yaml.ReflectorMap;
import java.util.concurrent.ExecutorService;
import spark.Request;
import spark.Response;
import org.fulib.scenarios.MockupTools;
import org.json.JSONObject;
import org.fulib.yaml.Reflector;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.io.StringWriter;
import org.fulib.yaml.Yaml;

public class JavaPackagesWithPatternsService
{

   public static final String PROPERTY_modelEditor = "modelEditor";

   private JavaPackagesWithPatternsEditor modelEditor;

   public static final String PROPERTY_streams = "streams";

   private List<CommandStream> streams;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_myPort = "myPort";
   private int myPort;
   public static final String PROPERTY_reflectorMap = "reflectorMap";
   private ReflectorMap reflectorMap;
   public static final String PROPERTY_currentSession = "currentSession";
   private String currentSession;
   public static final String PROPERTY_executor = "executor";
   private ExecutorService executor;
   public static final String PROPERTY_spark = "spark";
   private Service spark;
   public static final String PROPERTY_sessionToAppMap = "sessionToAppMap";
   private Map<String, JavaPackagesWithPatternsApp> sessionToAppMap = new LinkedHashMap<>();

   public JavaPackagesWithPatternsService setExecutor(ExecutorService value)
   {
      if (Objects.equals(value, this.executor))
      {
         return this;
      }

      final ExecutorService oldValue = this.executor;
      this.executor = value;
      this.firePropertyChange(PROPERTY_executor, oldValue, value);
      return this;
   }

   public JavaPackagesWithPatternsEditor getModelEditor()
   {
      return this.modelEditor;
   }

   public JavaPackagesWithPatternsService setModelEditor(JavaPackagesWithPatternsEditor value)
   {
      if (this.modelEditor == value)
      {
         return this;
      }

      final JavaPackagesWithPatternsEditor oldValue = this.modelEditor;
      if (this.modelEditor != null)
      {
         this.modelEditor = null;
         oldValue.setService(null);
      }
      this.modelEditor = value;
      if (value != null)
      {
         value.setService(this);
      }
      this.firePropertyChange(PROPERTY_modelEditor, oldValue, value);
      return this;
   }

   public List<CommandStream> getStreams()
   {
      return this.streams != null ? Collections.unmodifiableList(this.streams) : Collections.emptyList();
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
      this.setModelEditor(null);
      this.withoutStreams(new ArrayList<>(this.getStreams()));
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getCurrentSession());
      return result.substring(1);
   }

   public void start()
   {
      if (myPort <= 0) {
         myPort = 4571;
      }
      String envPort = System.getenv("PORT");
      if (envPort != null) {
         myPort = Integer.parseInt(envPort);
      }
      executor = Executors.newSingleThreadExecutor();
      setModelEditor(new JavaPackagesWithPatternsEditor());
      reflectorMap = new ReflectorMap(this.getClass().getPackage().getName());
      spark = Service.ignite();
      spark.port(myPort);
      spark.get("/", (req, res) -> executor.submit( () -> this.getFirstRoot(req, res)).get());
      spark.get("/JavaPackagesWithPatterns", (req, res) -> executor.submit( () -> this.getFirstRoot(req, res)).get());
      spark.post("/cmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());
      spark.post("/JavaPackagesWithPatternscmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());
      spark.post("/connect", (req, res) -> executor.submit( () -> this.connect(req, res)).get());
      // there are no streams

      spark.notFound((req, resp) -> "404 not found: " + req.requestMethod() + req.url() + req.body());

      Logger.getGlobal().info("JavaPackagesWithPatterns Service is listening on port " + myPort);
   }

   public String getFirstRoot(Request req, Response res)
   {
      currentSession = "" + (sessionToAppMap.size() + 1);
      return root(req, res);
   }

   public String root(Request req, Response res)
   {
      try
      {
         JavaPackagesWithPatternsApp myApp = this.sessionToAppMap.get(currentSession);
         if (myApp == null) {
            myApp = new JavaPackagesWithPatternsApp().init(this.modelEditor);
            sessionToAppMap.put(currentSession, myApp);
         }

         StringWriter stringWriter = new StringWriter();
         stringWriter.write(
               "<html>\n" +
                     "<head>\n" +
                     "    <meta charset=\"utf-8\">\n" +
                     "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                     "\n" +
                     "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                     "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\n" +
                     "</head>\n\n"
         );
         MockupTools.htmlTool().dumpScreen(stringWriter, myApp);
         stringWriter.write("\n</html>\n");
         StringBuilder page = new StringBuilder(stringWriter.toString());
         int paramPos = page.indexOf("_cmd: words[0],");
         String sessionParam = String.format("_session: '%s', ", currentSession);
         page.insert(paramPos, sessionParam);
         int cmdUrlPos = page.indexOf("'/cmd'");
         page.insert(cmdUrlPos + 2, "JavaPackagesWithPatterns");
         return page.toString();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return "404 " + e.getMessage();
      }
   }

   public String cmd(Request req, Response res)
   {
      String cmd = req.body();
      JSONObject jsonObject = new JSONObject(cmd);

      this.currentSession = jsonObject.getString("_session");

      JavaPackagesWithPatternsApp app = sessionToAppMap.get(currentSession);

      if (app == null) {
         return "404 could not find session " + currentSession;
      }

      String cmdClassName = jsonObject.getString("_cmd");
      if (cmdClassName.indexOf('?') > 0) {
         String[] split = cmdClassName.split("\\?");
         cmdClassName = split[0];
         jsonObject.put("_cmd", cmdClassName);
         String params = split[1];
         String[] paramArray = params.split("&");
         for (String oneParam : paramArray) {
            String[] keyValue = oneParam.split("=");
            jsonObject.put(keyValue[0], keyValue[1]);
         }
      }

      if (jsonObject.keySet().size() > 3) {
         cmdClassName = jsonObject.getString("_cmd");
         Reflector reflector = reflectorMap.getReflector(cmdClassName);
         Object cmdObject = reflector.newInstance();
         reflector.setValue(cmdObject, "_app", app);
         for (String key : jsonObject.keySet()) {
            if (key.startsWith("_")) {
               continue;
            }

            // assign to command attribute
            String attrName = key;
            if (key.endsWith("In")) {
               attrName = key.substring(0, key.length() - 2);
            }
            String value = jsonObject.getString(key);
            reflector.setValue(cmdObject, attrName, value);
         }
         // call command
         try {
            Method runMethod = cmdObject.getClass().getMethod("run", JavaPackagesWithPatternsEditor.class);
            runMethod.invoke(cmdObject, modelEditor);

         }
         catch (Exception e) {
            e.printStackTrace();
            return "404 " + e.getMessage();
         }
      }

      // next page
      String newPage = jsonObject.getString("_newPage");
      try {
         Method method = app.getClass().getMethod(newPage);
         method.invoke(app);
      }
      catch (Exception e) {
         return "404 app has no method to compute page " + newPage+ "\n" + e.getMessage();
      }

      return root(req, res);
   }

   public String connect(Request req, Response res)
   {
      String body = req.body();
      Map<String, Object> cmdList = Yaml.forPackage(AddStreamCommand.class.getPackage().getName()).decode(body);
      for (Object value : cmdList.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(modelEditor);
      }
      return "200";
   }

   public CommandStream getStream(String streamName)
   {
      for (CommandStream stream : this.getStreams()) {
         if (stream.getName().equals(streamName)) {
            return stream;
         }
      }
      CommandStream newStream = new CommandStream().setName(streamName);
      newStream.setService(this);
      withStreams(newStream);
      newStream.start();
      return newStream;
   }

public JavaPackagesWithPatternsService withStreams(CommandStream value)
   {
      if (this.streams == null)
      {
         this.streams = new ArrayList<>();
      }
      if (!this.streams.contains(value))
      {
         this.streams.add(value);
         value.setService(this);
         this.firePropertyChange(PROPERTY_streams, null, value);
      }
      return this;
   }

public JavaPackagesWithPatternsService withStreams(CommandStream... value)
   {
      for (final CommandStream item : value)
      {
         this.withStreams(item);
      }
      return this;
   }

public JavaPackagesWithPatternsService withStreams(Collection<? extends CommandStream> value)
   {
      for (final CommandStream item : value)
      {
         this.withStreams(item);
      }
      return this;
   }

public JavaPackagesWithPatternsService withoutStreams(CommandStream value)
   {
      if (this.streams != null && this.streams.remove(value))
      {
         value.setService(null);
         this.firePropertyChange(PROPERTY_streams, value, null);
      }
      return this;
   }

public JavaPackagesWithPatternsService withoutStreams(CommandStream... value)
   {
      for (final CommandStream item : value)
      {
         this.withoutStreams(item);
      }
      return this;
   }

public JavaPackagesWithPatternsService withoutStreams(Collection<? extends CommandStream> value)
   {
      for (final CommandStream item : value)
      {
         this.withoutStreams(item);
      }
      return this;
   }

   public int getMyPort()
   {
      return this.myPort;
   }

   public JavaPackagesWithPatternsService setMyPort(int value)
   {
      if (value == this.myPort)
      {
         return this;
      }

      final int oldValue = this.myPort;
      this.myPort = value;
      this.firePropertyChange(PROPERTY_myPort, oldValue, value);
      return this;
   }

   public ReflectorMap getReflectorMap()
   {
      return this.reflectorMap;
   }

   public JavaPackagesWithPatternsService setReflectorMap(ReflectorMap value)
   {
      if (Objects.equals(value, this.reflectorMap))
      {
         return this;
      }

      final ReflectorMap oldValue = this.reflectorMap;
      this.reflectorMap = value;
      this.firePropertyChange(PROPERTY_reflectorMap, oldValue, value);
      return this;
   }

   public String getCurrentSession()
   {
      return this.currentSession;
   }

   public JavaPackagesWithPatternsService setCurrentSession(String value)
   {
      if (Objects.equals(value, this.currentSession))
      {
         return this;
      }

      final String oldValue = this.currentSession;
      this.currentSession = value;
      this.firePropertyChange(PROPERTY_currentSession, oldValue, value);
      return this;
   }

   public ExecutorService getExecutor()
   {
      return this.executor;
   }

   public Service getSpark()
   {
      return this.spark;
   }

   public JavaPackagesWithPatternsService setSpark(Service value)
   {
      if (Objects.equals(value, this.spark))
      {
         return this;
      }

      final Service oldValue = this.spark;
      this.spark = value;
      this.firePropertyChange(PROPERTY_spark, oldValue, value);
      return this;
   }

   public Map<String, JavaPackagesWithPatternsApp> getSessionToAppMap()
   {
      return this.sessionToAppMap;
   }

   public JavaPackagesWithPatternsService setSessionToAppMap(Map<String, JavaPackagesWithPatternsApp> value)
   {
      if (Objects.equals(value, this.sessionToAppMap))
      {
         return this;
      }

      final Map<String, JavaPackagesWithPatternsApp> oldValue = this.sessionToAppMap;
      this.sessionToAppMap = value;
      this.firePropertyChange(PROPERTY_sessionToAppMap, oldValue, value);
      return this;
   }

}

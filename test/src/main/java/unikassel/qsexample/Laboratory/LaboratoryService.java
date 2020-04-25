package unikassel.qsexample.Laboratory;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import static spark.Spark.*;
import org.fulib.yaml.ReflectorMap;
import java.util.concurrent.ExecutorService;
import spark.Request;
import spark.Response;
import org.fulib.scenarios.MockupTools;
import org.json.JSONObject;
import org.fulib.yaml.Reflector;
import java.lang.reflect.Method;
import spark.Service;
import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class LaboratoryService  
{

   public static final String PROPERTY_myPort = "myPort";

   private int myPort;

   public int getMyPort()
   {
      return myPort;
   }

   public LaboratoryService setMyPort(int value)
   {
      if (value != this.myPort)
      {
         int oldValue = this.myPort;
         this.myPort = value;
         firePropertyChange("myPort", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_reflectorMap = "reflectorMap";

   private ReflectorMap reflectorMap;

   public ReflectorMap getReflectorMap()
   {
      return reflectorMap;
   }

   public LaboratoryService setReflectorMap(ReflectorMap value)
   {
      if (value != this.reflectorMap)
      {
         ReflectorMap oldValue = this.reflectorMap;
         this.reflectorMap = value;
         firePropertyChange("reflectorMap", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_currentSession = "currentSession";

   private String currentSession;

   public String getCurrentSession()
   {
      return currentSession;
   }

   public LaboratoryService setCurrentSession(String value)
   {
      if (value == null ? this.currentSession != null : ! value.equals(this.currentSession))
      {
         String oldValue = this.currentSession;
         this.currentSession = value;
         firePropertyChange("currentSession", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_executor = "executor";

   private ExecutorService  executor;

   public ExecutorService  getExecutor()
   {
      return executor;
   }

   public LaboratoryService setExecutor(ExecutorService  value)
   {
      if (value != this.executor)
      {
         ExecutorService  oldValue = this.executor;
         this.executor = value;
         firePropertyChange("executor", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_sessionToAppMap = "sessionToAppMap";

   private LinkedHashMap<String, LaboratoryApp> sessionToAppMap = new LinkedHashMap();

   public LinkedHashMap<String, LaboratoryApp> getSessionToAppMap()
   {
      return sessionToAppMap;
   }

   public LaboratoryService setSessionToAppMap(LinkedHashMap<String, LaboratoryApp> value)
   {
      if (value != this.sessionToAppMap)
      {
         LinkedHashMap<String, LaboratoryApp> oldValue = this.sessionToAppMap;
         this.sessionToAppMap = value;
         firePropertyChange("sessionToAppMap", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<CommandStream> EMPTY_streams = new java.util.ArrayList<CommandStream>()
   { @Override public boolean add(CommandStream value){ throw new UnsupportedOperationException("No direct add! Use xy.withStreams(obj)"); }};

   public static final String PROPERTY_streams = "streams";

   private java.util.ArrayList<CommandStream> streams = null;

   public java.util.ArrayList<CommandStream> getStreams()
   {
      if (this.streams == null)
      {
         return EMPTY_streams;
      }

      return this.streams;
   }

   public LaboratoryService withStreams(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withStreams(i);
            }
         }
         else if (item instanceof CommandStream)
         {
            if (this.streams == null)
            {
               this.streams = new java.util.ArrayList<CommandStream>();
            }
            if ( ! this.streams.contains(item))
            {
               this.streams.add((CommandStream)item);
               ((CommandStream)item).setService(this);
               firePropertyChange("streams", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public LaboratoryService withoutStreams(Object... value)
   {
      if (this.streams == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutStreams(i);
            }
         }
         else if (item instanceof CommandStream)
         {
            if (this.streams.contains(item))
            {
               this.streams.remove((CommandStream)item);
               ((CommandStream)item).setService(null);
               firePropertyChange("streams", item, null);
            }
         }
      }
      return this;
   }

   protected PropertyChangeSupport listeners = null;

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (listeners != null)
      {
         listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   public void removeYou()
   {
      this.setModelEditor(null);

      this.withoutStreams(this.getStreams().clone());


   }

   public static final String PROPERTY_spark = "spark";

   private Service spark;

   public Service getSpark()
   {
      return spark;
   }

   public LaboratoryService setSpark(Service value)
   {
      if (value != this.spark)
      {
         Service oldValue = this.spark;
         this.spark = value;
         firePropertyChange("spark", oldValue, value);
      }
      return this;
   }

   public void start() { 
      if (myPort <= 0) {
         myPort = 4571;
      }
      String envPort = System.getenv("PORT");
      if (envPort != null) {
         myPort = Integer.parseInt(envPort);
      }
      executor = java.util.concurrent.Executors.newSingleThreadExecutor();
      modelEditor = new LaboratoryEditor();
      reflectorMap = new ReflectorMap(this.getClass().getPackage().getName());
      spark = Service.ignite();
      try { spark.port(myPort);} catch (Exception e) {};
      spark.get("/", (req, res) -> executor.submit( () -> this.getFirstRoot(req, res)).get());
      spark.get("/Laboratory", (req, res) -> executor.submit( () -> this.getFirstRoot(req, res)).get());
      spark.post("/cmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());
      spark.post("/Laboratorycmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());
      // no streams

      spark.notFound((req, resp) -> {
         return "404 not found: " + req.requestMethod() + req.url() + req.body();
      });

      java.util.logging.Logger.getGlobal().info("Laboratory Service is listening on port " + myPort);
   }

   public static final String PROPERTY_modelEditor = "modelEditor";

   private LaboratoryEditor modelEditor = null;

   public LaboratoryEditor getModelEditor()
   {
      return this.modelEditor;
   }

   public LaboratoryService setModelEditor(LaboratoryEditor value)
   {
      if (this.modelEditor != value)
      {
         LaboratoryEditor oldValue = this.modelEditor;
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
         firePropertyChange("modelEditor", oldValue, value);
      }
      return this;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getCurrentSession());


      return result.substring(1);
   }

   public String getFirstRoot(Request req, Response res) { 
      currentSession = "" + (sessionToAppMap.size() + 1);
      return root(req, res);
   }

   public String root(Request req, Response res) { 
      try
      {
         LaboratoryApp myApp = this.sessionToAppMap.get(currentSession);
         if (myApp == null) {
            myApp = new LaboratoryApp().init(this.modelEditor);
            sessionToAppMap.put(currentSession, myApp);
         }

         java.util.Map<String, String> params = req.params();
         java.io.StringWriter stringWriter = new java.io.StringWriter();
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
         page.insert(cmdUrlPos + 2, "Laboratory");
         String sessionPage = page.toString();
         return sessionPage;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return "404 " + e.getMessage();
      }
   }

   public String cmd(Request req, Response res) { 
      String cmd = req.body();
      JSONObject jsonObject = new JSONObject(cmd);

      this.currentSession = jsonObject.getString("_session");

      LaboratoryApp app = sessionToAppMap.get(currentSession);

      if (app == null) {
         return "404 could not find session " + currentSession;
      }

      String cmdClassName = jsonObject.getString("_cmd");
      String[] split = new String[0];
      if (cmdClassName.indexOf('?') > 0) {
         split = cmdClassName.split("\\?");
         cmdClassName = split[0];
         jsonObject.put("_cmd", cmdClassName);
         String params = split[1];
         String[] paramArray = params.split("\\&");
         for (String oneParam : paramArray) {
            String[] keyValue = oneParam.split("\\=");
            jsonObject.put(keyValue[0], keyValue[1]);
         }
      }

      if (jsonObject.keySet().size() > 3) {
         cmdClassName = jsonObject.getString("_cmd");
         Reflector reflector = reflectorMap.getReflector(cmdClassName);
         Object cmdObject = reflector.newInstance();
         reflector.setValue(cmdObject, "_app", app, null);
         for (String key : jsonObject.keySet()) {
            if (key.startsWith("_")) {
               continue;
            }
            else {
               // assign to command attribute
               String attrName = key;
               if (key.endsWith("In")) {
                  attrName = key.substring(0, key.length() - 2);
               }
               String value = jsonObject.getString(key);
               reflector.setValue(cmdObject, attrName, value, null);
            }
         }
         // call command
         try {
            Method runMethod = cmdObject.getClass().getMethod("run", LaboratoryEditor.class);
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
         Method method = app.getClass().getMethod(newPage, new Class[0]);
         method.invoke(app);
      }
      catch (Exception e) {
         return "404 app has no method to compute page " + newPage+ "\n" + e.getMessage();
      }

      return root(req, res);
   }

   public String connect(Request req, Response res) { 
      String body = req.body();
      LinkedHashMap<String, Object> cmdList = org.fulib.yaml.Yaml.forPackage(AddStreamCommand.class.getPackage().getName()).decode(body);
      for (Object value : cmdList.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(modelEditor);
      }
      return "200";
   }

   public CommandStream getStream(String streamName) { 
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

}

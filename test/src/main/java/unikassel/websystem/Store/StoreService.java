package unikassel.websystem.Store;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import spark.*;
import org.fulib.yaml.ReflectorMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.fulib.scenarios.MockupTools;
import org.json.JSONObject;
import org.fulib.yaml.Reflector;
import spark.Spark;
import unikassel.websystem.Shop.ShopService;

import java.lang.reflect.Method;
import java.util.ServiceConfigurationError;
import java.util.concurrent.ExecutorService;

import static spark.Spark.port;
import spark.Service;
import spark.Request;
import spark.Response;

public class StoreService  
{

   private Service spark;

   public Service getSpark()
   {
      return spark;
   }

   public static void main(String[] args)
   {
      new StoreService().setMyPort(15016).start();
   }

   public StoreService() {
      String streams = System.getenv("streams");
   }

   private java.util.ArrayList<CommandStream> streams = null;

   public java.util.ArrayList<CommandStream> getStreams()
   {
      if (this.streams == null)
      {
         return EMPTY_streams;
      }

      return this.streams;
   }

   public static final String PROPERTY_myPort = "myPort";

   private int myPort;

   public int getMyPort()
   {
      return myPort;
   }

   public StoreService setMyPort(int value)
   {
      if (value != this.myPort)
      {
         int oldValue = this.myPort;
         this.myPort = value;
         firePropertyChange("myPort", oldValue, value);
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

   public static final String PROPERTY_modelEditor = "modelEditor";

   private StoreEditor modelEditor;

   public StoreEditor getModelEditor()
   {
      return modelEditor;
   }

   public StoreService setModelEditor(StoreEditor value)
   {
      if (value != this.modelEditor)
      {
         StoreEditor oldValue = this.modelEditor;
         this.modelEditor = value;
         firePropertyChange("modelEditor", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_currentSession = "currentSession";

   private String currentSession;

   public String getCurrentSession()
   {
      return currentSession;
   }

   public StoreService setCurrentSession(String value)
   {
      if (value == null ? this.currentSession != null : ! value.equals(this.currentSession))
      {
         String oldValue = this.currentSession;
         this.currentSession = value;
         firePropertyChange("currentSession", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_sessionToAppMap = "sessionToAppMap";

   private LinkedHashMap<String, StoreApp> sessionToAppMap = new LinkedHashMap();

   public LinkedHashMap<String, StoreApp> getSessionToAppMap()
   {
      return sessionToAppMap;
   }

   public StoreService setSessionToAppMap(LinkedHashMap<String, StoreApp> value)
   {
      if (value != this.sessionToAppMap)
      {
         LinkedHashMap<String, StoreApp> oldValue = this.sessionToAppMap;
         this.sessionToAppMap = value;
         firePropertyChange("sessionToAppMap", oldValue, value);
      }
      return this;
   }

   public void start() // no fulib
   {
      if (myPort <= 0) {
         myPort = 4571;
      }
      String envPort = System.getenv("PORT");
      if (envPort != null) {
         myPort = Integer.parseInt(envPort);
      }
      executor = java.util.concurrent.Executors.newSingleThreadExecutor();
      modelEditor = new StoreEditor();
      reflectorMap = new ReflectorMap(this.getClass().getPackage().getName());
      spark = Service.ignite();
      try { spark.port(myPort);} catch (Exception e) {};
      spark.get("/", (req, res) -> executor.submit( () -> this.getFirstRoot(req, res)).get());
      spark.get("/Store", (req, res) -> executor.submit( () -> this.getFirstRoot(req, res)).get());
      spark.post("/cmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());
      spark.post("/Storecmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());
      spark.notFound((req, resp) -> {
         return "404 not found: " + req.requestMethod() + req.url() + req.body();
      });

      java.util.logging.Logger.getGlobal().info("Store Service is listening on port " + myPort);
   }

   public static final String PROPERTY_reflectorMap = "reflectorMap";

   private ReflectorMap reflectorMap;

   public ReflectorMap getReflectorMap()
   {
      return reflectorMap;
   }

   public StoreService setReflectorMap(ReflectorMap value)
   {
      if (value != this.reflectorMap)
      {
         ReflectorMap oldValue = this.reflectorMap;
         this.reflectorMap = value;
         firePropertyChange("reflectorMap", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_executor = "executor";

   private ExecutorService  executor;

   public ExecutorService  getExecutor()
   {
      return executor;
   }

   public StoreService setExecutor(ExecutorService  value)
   {
      if (value != this.executor)
      {
         ExecutorService  oldValue = this.executor;
         this.executor = value;
         firePropertyChange("executor", oldValue, value);
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

   public static final java.util.ArrayList<CommandStream> EMPTY_streams = new java.util.ArrayList<CommandStream>()
   { @Override public boolean add(CommandStream value){ throw new UnsupportedOperationException("No direct add! Use xy.withStreams(obj)"); }};

   public static final String PROPERTY_streams = "streams";

   public StoreService withStreams(Object... value)
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

   public StoreService withoutStreams(Object... value)
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

   public void removeYou()
   {
      this.withoutStreams(this.getStreams().clone());


   }

   public static final String PROPERTY_spark = "spark";

   public StoreService setSpark(Service value)
   {
      if (value != this.spark)
      {
         Service oldValue = this.spark;
         this.spark = value;
         firePropertyChange("spark", oldValue, value);
      }
      return this;
   }

   public String getFirstRoot(Request req, Response res) { 
      currentSession = "" + (sessionToAppMap.size() + 1);
      return root(req, res);
   }

   public String root(Request req, Response res) { 
      try
      {
         StoreApp myApp = this.sessionToAppMap.get(currentSession);
         if (myApp == null) {
            myApp = new StoreApp().init(this.modelEditor);
            sessionToAppMap.put(currentSession, myApp);
         }

         java.util.Map<String, String> params = req.params();
         java.io.StringWriter stringWriter = new java.io.StringWriter();
         MockupTools.htmlTool().dumpScreen(stringWriter, myApp);
         StringBuilder page = new StringBuilder(stringWriter.toString());
         int paramPos = page.indexOf("_cmd: words[0],");
         String sessionParam = String.format("_session: '%s', ", currentSession);
         page.insert(paramPos, sessionParam);
         int cmdUrlPos = page.indexOf("'/cmd'");
         page.insert(cmdUrlPos + 2, "Store");
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

      StoreApp app = sessionToAppMap.get(currentSession);

      if (app == null) {
         return "404 could not find session " + currentSession;
      }

      if (jsonObject.keySet().size() > 3) {
         String cmdClassName = jsonObject.getString("_cmd");
         Reflector reflector = reflectorMap.getReflector(cmdClassName);
         Object cmdObject = reflector.newInstance();
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
            Method runMethod = cmdObject.getClass().getMethod("run", StoreEditor.class);
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
         return "404 app has no method to compute page " + newPage;
      }

      return root(req, res);
   }

   public void addStream(String incommingRoute, String outgoingURL, String... commandList) { 
      CommandStream stream = new CommandStream().setService(this);
      stream.start(incommingRoute, outgoingURL, this);
      for (String command : commandList) {
         modelEditor.addCommandListener(command, stream);
      }
   }

}

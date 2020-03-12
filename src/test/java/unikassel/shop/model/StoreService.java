package unikassel.shop.model;

import org.fulib.scenarios.MockupTools;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.*;

public class StoreService
{
   private LinkedHashMap<String, StoreApp> sessionToAppMap = new LinkedHashMap();
   private ReflectorMap reflectorMap;
   private StoreEditor storeEditor;
   private int myPort;

   public static void main(String[] args)
   {
      StoreService service = new StoreService();
      service.init(args);


   }

   public void setMyPort(int myPort)
   {
      this.myPort = myPort;
   }

   public void init(String[] args)
   {
      myPort = 4571;
      String envPort = System.getenv("PORT");
      if (envPort != null) {
         myPort = Integer.parseInt(envPort);
      }

      java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
      storeEditor = new StoreEditor();
      reflectorMap = new ReflectorMap(StoreApp.class.getPackage().getName());

      port(myPort);
       get("/", (req, res) -> executor.submit( () -> this.getFirstRoot(req, res)).get());
       post("/cmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());

      notFound((req, resp) -> {
         return "404 not found: " + req.requestMethod() + req.url() + req.body();
      });

      Logger.getGlobal().info("Store Serice is listening on port " + myPort);
   }

   private String cmd(Request req, Response res)
   {
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
            runMethod.invoke(cmdObject, storeEditor);

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

   private String currentSession = "1";

   private String getFirstRoot(Request req, Response res)
   {
      currentSession = "" + (sessionToAppMap.size() + 1);

      return root(req, res);
   }

   private String root(Request req, Response res)
   {
      try
      {
         StoreApp storeApp = this.sessionToAppMap.get(currentSession);
         if (storeApp == null) {
            storeApp = new StoreApp().init();
            sessionToAppMap.put(currentSession, storeApp);
         }

         Map<String, String> params = req.params();
         StringWriter stringWriter = new StringWriter();
         MockupTools.htmlTool().dumpScreen(stringWriter, storeApp);
         StringBuilder page = new StringBuilder(stringWriter.toString());
         int paramPos = page.indexOf("_cmd: words[0],");
         String sessionParam = String.format("_session: '%s', ", currentSession);
         page.insert(paramPos, sessionParam);
         String sessionPage = page.toString();
         return sessionPage;
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return "404 " + e.getMessage();
      }
   }
}

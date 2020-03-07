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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static spark.Spark.*;

public class StoreService
{

   private StoreApp app;
   private ExecutorService executor;
   private static       int myPort = 4571;
   private ReflectorMap reflectorMap;
   private StoreEditor storeEditor;

   public static void main(String[] args)
   {
      StoreService service = new StoreService();
      service.init(args);

      System.out.println("Store Serice is listening on port " + myPort);
   }

   private void init(String[] args)
   {
      executor = Executors.newSingleThreadExecutor();
      storeEditor = new StoreEditor();
      app = new StoreApp().init();
      reflectorMap = new ReflectorMap(app.getClass().getPackage().getName());

      port(myPort);
       get("/", (req, res) -> executor.submit( () -> this.root(req, res)).get());
       post("/cmd", (req, res) -> executor.submit( () -> this.cmd(req, res)).get());

      notFound((req, resp) -> {
         return "404 not found: " + req.requestMethod() + req.url() + req.body();
      });

   }

   private String cmd(Request req, Response res)
   {
      String cmd = req.body();
      JSONObject jsonObject = new JSONObject(cmd);

      if (jsonObject.keySet().size() > 2) {
         String cmdClassName = jsonObject.getString("_cmd");
         Reflector reflector = reflectorMap.getReflector(cmdClassName);
         Object cmdObject = reflector.newInstance();
         for (String key : jsonObject.keySet()) {
            if (key.equals("_cmd")) {
               continue;
            }
            else if (key.equals("_newPage")) {
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
         Method method = this.app.getClass().getMethod(newPage, new Class[0]);
         method.invoke(app);
      }
      catch (Exception e) {
         return "404 app has no method to compute page " + newPage;
      }

      return root(req, res);
   }


   private String root(Request req, Response res)
   {
      try
      {
         StringWriter stringWriter = new StringWriter();
         MockupTools.htmlTool().dumpScreen(stringWriter, app);
         String page = stringWriter.toString();
         return page.toString();
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return "404 " + e.getMessage();
      }
   }
}

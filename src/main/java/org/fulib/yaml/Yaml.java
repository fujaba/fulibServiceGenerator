package org.fulib.yaml;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

/**
 * Simple yaml encoding
 */
public class Yaml
{
   public static String encode(Object... objects) {
      return new Yaml().doEncode(objects);
   }

   public static Yaml forPackage(String... packageNames) {
      return new Yaml(packageNames);
   }


   private static final String REMOVE = "remove";
   private static final String REMOVE_YOU = "removeYou";
   private ArrayList<String> packageNames;
   private String yaml;
   private String userId = null;
   private boolean decodingPropertyChange;
   private LinkedHashMap<String, Object> idToObjectMap = new LinkedHashMap<>();
   private LinkedHashMap<Object, String> objectToIdMap = new LinkedHashMap<>();
   private int maxUsedIdNum = 1;
   private Yamler yamler = new Yamler();
   private HashMap<String, String> attrTimeStamps = new HashMap<>();

   public LinkedHashMap<String, Object> getIdToObjectMap()
   {
      return idToObjectMap;
   }

   public LinkedHashMap<Object, String> getObjectToIdMap()
   {
      return objectToIdMap;
   }

   public HashMap<String, String> getAttrTimeStamps()
   {
      return attrTimeStamps;
   }


   /**

    */
   private Yaml()
   {
      // always pass package to constructor
   }


   /**
    *
    */
   public Yaml(String... packageNames)
   {
      Objects.requireNonNull(packageNames);
      List<String> list = Arrays.asList(packageNames);
      this.packageNames = new ArrayList<String>(list);
      reflectorMap = new ReflectorMap(this.packageNames);
   }


   public LinkedHashMap<String, Object> decode(String yaml)
   {
      this.yaml = yaml;
      Object root = null;
      yamler = new Yamler();
      ArrayList<LinkedHashMap<String, String>> hashMaps = yamler.decodeList(this.yaml);
      createObjects(hashMaps);
      fillAttributes(hashMaps);

      return idToObjectMap;
   }

   private void fillAttributes(ArrayList<LinkedHashMap<String, String>> hashMaps)
   {
      for (LinkedHashMap<String, String> map : hashMaps) {
         fillAttributes(map);
      }
   }

   private void fillAttributes(LinkedHashMap<String, String> map)
   {
      String id = map.get("id");
      Object currentObject = idToObjectMap.get(id);
      Reflector reflector = reflectorMap.getReflector(currentObject);
      for (Entry<String, String> entry : map.entrySet()) {
         String key = entry.getKey();
         String value = entry.getValue();

         if (key.equals("id") || key.equals("class")) {
            continue;
         }

         reflector.setValue(currentObject, key, value, null);
      }
   }

   private void createObjects(ArrayList<LinkedHashMap<String, String>> hashMaps)
   {
      for (LinkedHashMap<String, String> map : hashMaps) {
         createOneObject(map);
      }
   }

   private void createOneObject(LinkedHashMap<String, String> map)
   {
      String id = map.get("id");
      String clazz = map.get("class");

      Reflector reflector = reflectorMap.getReflector(clazz);
      Object newObject = reflector.newInstance();
      reflector.setValue(newObject, "id", id, "String");

      idToObjectMap.put(id, newObject);
   }


   ReflectorMap reflectorMap;

   public Reflector getReflector(Object obj)
   {
      if (reflectorMap == null) {
         reflectorMap = new ReflectorMap(obj.getClass().getPackage().getName());
      }
      return reflectorMap.getReflector(obj);
   }


   public Object getObject(String objId)
   {
      return idToObjectMap.get(objId);
   }

   private String doEncode(Object... rootObjList)
   {
      Objects.requireNonNull(rootObjList);

      StringBuilder buf = new StringBuilder();

      collectObjects(rootObjList);

      for (Entry<String, Object> entry : idToObjectMap.entrySet()) {
         String key = entry.getKey();
         Object obj = entry.getValue();
         String className = obj.getClass().getSimpleName();


         buf.append("- id:    \t").append(key).append("\n");
         buf.append("  class: \t").append(className).append("\n");

         // attrs
         Reflector creator = getReflector(obj);

         for (String prop : creator.getProperties()) {
            if (prop.equals("id")) {
               continue;
            }

            Object value = creator.getValue(obj, prop);

            if (value == null) {
               continue;
            }

            if (value instanceof Collection) {
               if (((Collection) value).isEmpty()) {
                  continue;
               }

               buf.append("  ").append(prop).append(": \t");
               for (Object valueObj : (Collection) value) {
                  String valueKey = objectToIdMap.get(valueObj);
                  buf.append(valueKey).append(" \t");
               }
               buf.append("\n");
            }
            else if (value instanceof Map) {
               continue;
            }
            else {
               String valueKey = objectToIdMap.get(value);

               if (valueKey != null) {
                  buf.append("  ").append(prop).append(": \t").append(valueKey).append("\n");
               }
               else {
                  if (value instanceof String) {
                     value = yamler.encapsulate((String) value);
                  }
                  buf.append("  ").append(prop).append(": \t").append(value).append("\n");
               }

               // add time stamp?
               if (userId != null) {
                  String timeKey = key + "." + prop;
                  String timeStamp = attrTimeStamps.get(timeKey);

                  if (timeStamp != null) {
                     buf.append("  ").append(prop).append(".time: \t").append(timeStamp).append("\n");
                  }
               }
            }
         }
         buf.append("\n");
      }

      return buf.toString();
   }


   public LinkedHashSet<Object> collectObjects(Object... rootObjList)
   {
      LinkedList<Object> simpleList = new LinkedList<>();
      LinkedHashSet<Object> collectedObjects = new LinkedHashSet<>();

      for (Object obj : rootObjList) {
         if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            for (Object o : collection) {
               simpleList.add(o);
            }
         }
         else if (obj instanceof Map) {
            Map map = (Map) obj;
            for (Object value : map.values()) {
               simpleList.add(value);
            }
         }
         else {
            simpleList.add(obj);
         }
      }


      // collect objects
      while (!simpleList.isEmpty()) {
         Object obj = simpleList.get(0);
         simpleList.remove(0);
         collectedObjects.add(obj);

         // already known?
         String key = objectToIdMap.get(obj);

         if (key == null) {
            // add to map
            key = addToObjIdMap(obj);

            // find neighbors
            Reflector reflector = getReflector(obj);

            for (String prop : reflector.getProperties()) {
               Object value = reflector.getValue(obj, prop);

               if (value == null) {
                  continue;
               }

               Class valueClass = value.getClass();

               if (value instanceof Collection) {
                  for (Object valueObj : (Collection) value) {
                     valueClass = valueObj.getClass();

                     if (valueClass.getName().startsWith("java.lang"))
                        break;

                     simpleList.add(valueObj);
                  }
               }
               else if (valueClass.getName().startsWith("java.util.")) {
                  continue; // not (yet) supported
               }
               else if (valueClass.getName().startsWith("java.lang.")) {
                  continue;
               }
               else {
                  simpleList.add(value);
               }
            }
         }

      } // collect objects
      return collectedObjects;
   }



   private String addToObjIdMap(Object obj)
   {
      String className = obj.getClass().getSimpleName();

      String key = null;

      if (obj instanceof YamlObject) {
         YamlObject yamlObj = (YamlObject) obj;
         Object mapId = yamlObj.getMap().get(".id");
         key = (String) mapId;
      }

      if (key == null) {
         key = className.substring(0, 1).toLowerCase();
         Class<?> clazz = obj.getClass();
         try {
            Method getId = clazz.getMethod("getId");
            Object id = getId.invoke(obj);
            if (id != null) {
               key = id.toString().replaceAll("\\W+", "_");
            }
         }
         catch (Exception e) {
            try {
               Method getId = clazz.getMethod("getName");
               Object id = getId.invoke(obj);
               if (id != null) {
                  key = id.toString().replaceAll("\\W+", "_");
               }
            }
            catch (Exception e2) {
               // go with old key
            }
         }

         if (key.length() == 1) {
            key = key.substring(0, 1).toLowerCase();
         }
         else {
            key = key.substring(0, 1).toLowerCase() + key.substring(1);
         }

         if (idToObjectMap.get(key) != null) {
            // key is already in use
            maxUsedIdNum++;
            key += maxUsedIdNum;
         }

         if (maxUsedIdNum > 1 && userId != null) {
            // all but the first get a userId prefix
            key = userId + "." + key;
         }

      }
      idToObjectMap.put(key, obj);
      objectToIdMap.put(obj, key);

      return key;
   }



}

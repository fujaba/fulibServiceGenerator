package javaPackagesToJavaDoc.JavaDoc;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.StrUtil;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.*;

import org.fulib.tables.ObjectTable;
import org.fulib.yaml.Yaml;
import org.fulib.patterns.*;

public class ModelCommand
{
   public static final String PROPERTY_id = "id";
   private String id;
   public static final String PROPERTY_time = "time";
   private String time;

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getTime());
      return result.substring(1);
   }

   public ModelCommand parse(Object currentObject)
   {
      Pattern pattern = havePattern();

      if (pattern == null) {
         return null;
      }

      PatternObject firstPatternObject = pattern.getObjects().get(0);
      if ( ! firstPatternObject.readHandleObjectClass().equals(currentObject.getClass())) {
         // not my business
         return null;
      }

      ObjectTable<Object> pathTable = new ObjectTable<>(firstPatternObject.getPoId(), currentObject);

      matchAttributesAndLinks(pattern, firstPatternObject, pathTable);

      // retrieve command
      if (pathTable.rowCount() == 0) {
         return null;
      }

      Map<String, Object> firstRow = new LinkedHashMap<>();
      pathTable.filterRows(m -> {
         firstRow.putAll(m);
         return true;
      });

      ModelCommand newCommand = null;
      try {
         newCommand = this.getClass().getConstructor().newInstance();
         Reflector commandReflector = new Reflector().setClazz(newCommand.getClass());
         for (PatternObject patternObject : pattern.getObjects()) {
            String poId = patternObject.getPoId();
            for (PatternAttribute attribute : patternObject.getAttributes()) {
               String commandParamName = attribute.getCommandParamName();
               Object value = firstRow.get(poId + "." + attribute.getHandleAttrName());
               commandReflector.setValue(newCommand, commandParamName, "" + value);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return newCommand;
   }

   public Object run(JavaDocEditor editor)
   {
      Pattern pattern = havePattern();

      if (pattern == null) {
         return null;
      }

      // have handle objects
      for (PatternObject patternObject : pattern.getObjects()) {
         String handleObjectId = (String) getHandleObjectAttributeValue(patternObject, "id");
         if (handleObjectId == null) {
            // do not handle
            continue;
         }
         Class handleObjectClass = patternObject.readHandleObjectClass();
         Object handleObject = null;
         if (patternObject.getKind().equals("core") ) {
            handleObject = editor.getOrCreate(handleObjectClass, handleObjectId);
         }
         else if (patternObject.getKind().equals("context")) {
            handleObject = editor.getObjectFrame(handleObjectClass, handleObjectId);
         }
         else { // nac
            handleObject = editor.getObjectFrame(handleObjectClass, handleObjectId);
            editor.removeModelObject(handleObjectId);
         }

         patternObject.setHandleObject(handleObject);
      }

      for (PatternObject patternObject : pattern.getObjects()) {
         if (patternObject.getHandleObject() == null) {
            continue;
         }

         Reflector commandReflector = new Reflector().setClassName(this.getClass().getName());
         Reflector handleObjectReflector = new Reflector().setClassName(patternObject.getHandleObject().getClass().getName());

         for (PatternAttribute patternAttribute : patternObject.getAttributes()) {
            if ("id".equals(patternAttribute.getHandleAttrName())) {
               continue;
            }

            Object paramValue = commandReflector.getValue(this, patternAttribute.getCommandParamName());
            paramValue = paramValue.toString();
            handleObjectReflector.setValue(patternObject.getHandleObject(), patternAttribute.getHandleAttrName(), paramValue, null);
         }

         Object sourceHandleObject = patternObject.getHandleObject();
         for (PatternLink patternLink : patternObject.getLinks()) {
            String linkName = patternLink.getHandleLinkName();
            Object targetHandleObject = patternLink.getTarget().getHandleObject();
            if (patternLink.getKind().equals("core")) {
               handleObjectReflector.setValue(sourceHandleObject, linkName, targetHandleObject, null);
            }
            else if (patternLink.getKind().equals("nac")) {
               // remove link
               if (targetHandleObject != null) {
                  try {
                     Method withoutMethod = sourceHandleObject.getClass().getMethod("without" + StrUtil.cap(linkName), targetHandleObject.getClass());
                     if (withoutMethod != null) {
                        withoutMethod.invoke(sourceHandleObject, targetHandleObject);
                     }
                     else {
                        Method setMethod = sourceHandleObject.getClass().getMethod("set" + StrUtil.cap(linkName), patternLink.getTarget().readHandleObjectClass());
                        setMethod.invoke(sourceHandleObject, new Object[] {null});
                     }
                  }
                  catch (Exception e) {
                     e.printStackTrace();
                  }
               }
               else {
                  try {
                     Method setMethod = sourceHandleObject.getClass().getMethod("set" + StrUtil.cap(linkName), patternLink.getTarget().readHandleObjectClass());
                     setMethod.invoke(sourceHandleObject, new Object[] {null});
                  }
                  catch (Exception e) {
                     e.printStackTrace();
                  }
               }
            }
         }
      }
      return null;
   }

   public Set getSetOfTargetHandles(Map map, String poId, String linkName)
   {
      Object sourceHandleObject = map.get(poId);
      ObjectTable<Object> pathTable = new ObjectTable<>(poId, sourceHandleObject);
      pathTable.expandLink(poId, linkName, linkName);

      return pathTable.toSet(linkName);
   }

   public Pattern havePattern()
   {
      return null;
   }

   private Object getHandleObjectAttributeValue(PatternObject patternObject, String handleAttributeName)
   {
      Reflector reflector = new Reflector().setClassName(this.getClass().getName());
      for (PatternAttribute patternAttribute : patternObject.getAttributes()) {
         if (patternAttribute.getHandleAttrName().equals(handleAttributeName)) {
            String commandParamName = patternAttribute.getCommandParamName();
            Object value = reflector.getValue(this, commandParamName);
            return value;
         }
      }
      return null;
   }

   public String getId()
   {
      return this.id;
   }

   public ModelCommand setId(String value)
   {
      this.id = value;
      return this;
   }

   public String getTime()
   {
      return this.time;
   }

   public ModelCommand setTime(String value)
   {
      this.time = value;
      return this;
   }

   public void matchAttributesAndLinks(Pattern pattern, PatternObject currentPatternObject, ObjectTable<Object> pathTable)
   {
      String poId = currentPatternObject.getPoId();

      // match attributes
      for (PatternAttribute attribute : currentPatternObject.getAttributes()) {
         String attrName = attribute.getHandleAttrName();
         pathTable.expandAttribute(poId, poId + "." + attrName, attrName);
      }

      // match links
      for (PatternLink link : currentPatternObject.getLinks()) {
         PatternObject source = link.getSource();
         PatternObject target = link.getTarget();

         if ("core".equals(link.getKind())) {
            if (pathTable.getColumnMap().containsKey(target.getPoId())) {
               pathTable.hasLink(source.getPoId(), target.getPoId(), link.getHandleLinkName());
            }
            else {
               pathTable.expandLink(source.getPoId(), link.getTarget().getPoId(), link.getHandleLinkName());
               matchAttributesAndLinks(pattern, target, pathTable);
            }
         }
         else if ("nac".equals(link.getKind())) {
            if (pathTable.getColumnMap().containsKey(target.getPoId())) {
               pathTable.filterRows(map -> getSetOfTargetHandles(map, poId, link.getHandleLinkName()).contains(
                  map.get(source.getPoId())));
            }
            else {
               pathTable.filterRows(map -> getSetOfTargetHandles(map, poId, link.getHandleLinkName()).isEmpty());
            }
         }
      }
   }

   public boolean overwrites(ModelCommand oldCommand)
   {
         if (oldCommand.getTime().compareTo(time) > 0) {
            return false;
         } else if (oldCommand.getTime().equals(time)) {
            String oldYaml = Yaml.encode(oldCommand);
            String newYaml = Yaml.encode(this);
            if (oldYaml.compareTo(newYaml) >= 0) {
               return false;
            }
         }
      return true;
   }

   public void remove(JavaDocEditor editor)
   {
      Pattern pattern = havePattern();

      if (pattern == null) {
         return;
      }

      for (PatternObject patternObject : pattern.getObjects()) {
         if (patternObject.getKind() == "core") {
            String id = (String) getHandleObjectAttributeValue(patternObject, "id");
            Object handleObject = editor.getObjectFrame(null, id);
            for (PatternLink link : patternObject.getLinks()) {
               String linkName = link.getHandleLinkName();
               Reflector handleObjectReflector = new Reflector().setClassName(handleObject.getClass().getName());
               Object value = handleObjectReflector.getValue(handleObject, linkName);
               if (value != null && value instanceof java.util.Collection) {
                  try {
                     if (((Collection) value).isEmpty()) {
                        continue;
                     }

                     java.lang.reflect.Method withoutMethod = handleObject.getClass()
                        .getMethod("without" + linkName.substring(0, 1).toUpperCase() + linkName.substring(1), Collection.class);
                     ArrayList newValue = new ArrayList((Collection)value);
                     withoutMethod.invoke(handleObject, newValue);
                  }
                  catch (Exception e) {
                     e.printStackTrace();
                  }
               }
               else {
                  try {
                     java.lang.reflect.Method setMethod = handleObject.getClass().getMethod("set" + linkName.substring(0, 1).toUpperCase() + linkName.substring(1),
                           link.getTarget().readHandleObjectClass());
                     setMethod.invoke(handleObject, new Object[]{null});
                  }
                  catch (Exception e) {
                     e.printStackTrace();
                  }
               }
            }
         }
      }
   }

}

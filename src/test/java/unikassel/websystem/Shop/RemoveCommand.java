package unikassel.websystem.Shop;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import java.lang.reflect.Method;

public class RemoveCommand extends ModelCommand  
{

   public static final String PROPERTY_targetClassName = "targetClassName";

   private String targetClassName;

   public String getTargetClassName()
   {
      return targetClassName;
   }

   public RemoveCommand setTargetClassName(String value)
   {
      if (value == null ? this.targetClassName != null : ! value.equals(this.targetClassName))
      {
         String oldValue = this.targetClassName;
         this.targetClassName = value;
         firePropertyChange("targetClassName", oldValue, value);
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

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getTargetClassName());


      return result.substring(1);
   }

   public ModelCommand run(ShopEditor editor) { 
      // allready removed?
      RemoveCommand oldRemoveCommand = editor.getRemoveCommands().get(this.getTargetClassName() + "-" + this.getId());
      if (oldRemoveCommand != null) {
         return null;
      }

      // find the target object
      ReflectorMap reflectorMap = new ReflectorMap(editor.getClass().getPackage().getName());
      Reflector reflector = reflectorMap.getReflector(editor);
      Object value = reflector.getValue(editor, this.getTargetClassName() + "s");
      java.util.Map objects = (java.util.Map) value;
      Object target = objects.get(this.getId());
      try {
         Method removeYouMethod = target.getClass().getMethod("removeYou", new Class[0]);
         removeYouMethod.invoke(target, new Object[0]);
      }
      catch (Exception e) {
         // ignore
      }
      objects.remove(this.getId());
      editor.getRemoveCommands().put(this.getTargetClassName() + "-" + this.getId(), this);
      editor.fireCommandExecuted(this);

      return null;
   }

}
package org.fulib.patterns;
import java.util.ArrayList;
import java.util.Objects;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.TreeSet;

public class TGGRule
{
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_left = "left";
   private Pattern left;
   public static final String PROPERTY_right = "right";
   private Pattern right;
   protected PropertyChangeSupport listeners;

   public String getName()
   {
      return this.name;
   }

   public TGGRule setName(String value)
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

   public Pattern getLeft()
   {
      return this.left;
   }

   public TGGRule setLeft(Pattern value)
   {
      if (this.left == value)
      {
         return this;
      }

      final Pattern oldValue = this.left;
      if (this.left != null)
      {
         this.left = null;
         oldValue.setLparent(null);
      }
      this.left = value;
      if (value != null)
      {
         value.setLparent(this);
      }
      this.firePropertyChange(PROPERTY_left, oldValue, value);
      return this;
   }

   public Pattern getRight()
   {
      return this.right;
   }

   public TGGRule setRight(Pattern value)
   {
      if (this.right == value)
      {
         return this;
      }

      final Pattern oldValue = this.right;
      if (this.right != null)
      {
         this.right = null;
         oldValue.setRparent(null);
      }
      this.right = value;
      if (value != null)
      {
         value.setRparent(this);
      }
      this.firePropertyChange(PROPERTY_right, oldValue, value);
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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setLeft(null);
      this.setRight(null);
   }

   public TreeSet<String> incrementProperties()
   {
      TreeSet<String> result = new TreeSet<>();

      patternIncrementProperties(result, "left", this.left);
      patternIncrementProperties(result, "right", this.right);

      return result;
   }

   private void patternIncrementProperties(TreeSet<String> result, String prefix, Pattern pattern)
   {
      for (PatternObject patternObject : pattern.getObjects()) {
         if (patternObject.getKind().equals("core") || patternObject.getKind().equals("nac")) {
            PatternAttribute idAttr = patternObject.getFromAttributes("id");
            if (idAttr == null) {
               continue;

            }
            String paramName = idAttr.getCommandParamName();
            String className = patternObject.readHandleObjectClass().getSimpleName();
            String prop = String.format("%s.%s.%s", prefix, paramName, className);
            result.add(prop);

            // attributes
            for (PatternAttribute attribute : patternObject.getAttributes()) {
               if (attribute.getHandleAttrName().equals("id")) {
                  continue;
               }
               prop = String.format("%s.%s.%s", prefix, paramName, attribute.getHandleAttrName());
               result.add(prop);
            }

            // links
            for (PatternLink link : patternObject.getLinks()) {
               prop = String.format("%s.%s.%s", prefix, paramName, link.getHandleLinkName());
               PatternAttribute targetId = link.getTarget().getFromAttributes("id");
               if (targetId != null && targetId.getCommandParamName().endsWith("Id")) {
                  prop = String.format("%s.%s", prop, targetId.getCommandParamName());
               }
               result.add(prop);
            }
         }
      }
   }

   public TreeSet<String> fitInto(ArrayList<TreeSet<String>> list)
   {
      TreeSet<String> newPropertySet = this.incrementProperties();
      for (TreeSet<String> oldPropertySet : list) {
         if (newPropertySet.equals(oldPropertySet)) {
            continue;
         }
         for (String prop : newPropertySet) {
            if (oldPropertySet.contains(prop)) {
               return oldPropertySet;
            }
         }
      }

      list.add(newPropertySet);

      return null;
   }
}

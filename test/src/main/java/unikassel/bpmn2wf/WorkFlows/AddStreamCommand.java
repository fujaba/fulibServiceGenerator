package unikassel.bpmn2wf.WorkFlows;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class AddStreamCommand extends ModelCommand  
{

   public static final String PROPERTY_incommingRoute = "incommingRoute";

   private String incommingRoute;

   public String getIncommingRoute()
   {
      return incommingRoute;
   }

   public AddStreamCommand setIncommingRoute(String value)
   {
      if (value == null ? this.incommingRoute != null : ! value.equals(this.incommingRoute))
      {
         String oldValue = this.incommingRoute;
         this.incommingRoute = value;
         firePropertyChange("incommingRoute", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_outgoingUrl = "outgoingUrl";

   private String outgoingUrl;

   public String getOutgoingUrl()
   {
      return outgoingUrl;
   }

   public AddStreamCommand setOutgoingUrl(String value)
   {
      if (value == null ? this.outgoingUrl != null : ! value.equals(this.outgoingUrl))
      {
         String oldValue = this.outgoingUrl;
         this.outgoingUrl = value;
         firePropertyChange("outgoingUrl", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_commandList = "commandList";

   private String commandList;

   public String getCommandList()
   {
      return commandList;
   }

   public AddStreamCommand setCommandList(String value)
   {
      if (value == null ? this.commandList != null : ! value.equals(this.commandList))
      {
         String oldValue = this.commandList;
         this.commandList = value;
         firePropertyChange("commandList", oldValue, value);
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

      result.append(" ").append(this.getIncommingRoute());
      result.append(" ").append(this.getOutgoingUrl());
      result.append(" ").append(this.getCommandList());


      return result.substring(1);
   }

   public Object run(WorkFlowsEditor editor) { 
      String[] split = commandList.split(" ");
      editor.getService().addStream(incommingRoute, outgoingUrl, split);
      return null;
   }

}
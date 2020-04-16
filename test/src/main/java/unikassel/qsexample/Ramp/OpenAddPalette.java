package unikassel.qsexample.Ramp;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class OpenAddPalette extends ModelCommand  
{
   @Override
   public Object run(RampEditor editor)
   {
      _app.setSupplyId(supply);
      return null;
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

   public static final String PROPERTY__app = "_app";

   private RampApp _app;

   public RampApp get_app()
   {
      return _app;
   }

   public OpenAddPalette set_app(RampApp value)
   {
      if (value != this._app)
      {
         RampApp oldValue = this._app;
         this._app = value;
         firePropertyChange("_app", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_supply = "supply";

   private String supply;

   public String getSupply()
   {
      return supply;
   }

   public OpenAddPalette setSupply(String value)
   {
      if (value == null ? this.supply != null : ! value.equals(this.supply))
      {
         String oldValue = this.supply;
         this.supply = value;
         firePropertyChange("supply", oldValue, value);
      }
      return this;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getSupply());


      return result.substring(1);
   }

   public boolean preCheck(RampEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("OpenAddPalette-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("OpenAddPalette-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("OpenAddPalette-" + this.getId(), this);
      return true;
   }

}

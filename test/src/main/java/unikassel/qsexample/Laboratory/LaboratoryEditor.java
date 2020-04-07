package unikassel.qsexample.Laboratory;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import org.fulib.yaml.Yaml;
import java.util.ArrayList;

public class LaboratoryEditor  
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public LaboratoryEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
   {
      if (value != this.activeCommands)
      {
         java.util.Map<String, ModelCommand> oldValue = this.activeCommands;
         this.activeCommands = value;
         firePropertyChange("activeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_removeCommands = "removeCommands";

   private java.util.Map<String, RemoveCommand> removeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, RemoveCommand> getRemoveCommands()
   {
      return removeCommands;
   }

   public LaboratoryEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
   {
      if (value != this.removeCommands)
      {
         java.util.Map<String, RemoveCommand> oldValue = this.removeCommands;
         this.removeCommands = value;
         firePropertyChange("removeCommands", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_isoDateFormat = "isoDateFormat";

   private DateFormat isoDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

   public DateFormat getIsoDateFormat()
   {
      return isoDateFormat;
   }

   public LaboratoryEditor setIsoDateFormat(DateFormat value)
   {
      if (value != this.isoDateFormat)
      {
         DateFormat oldValue = this.isoDateFormat;
         this.isoDateFormat = value;
         firePropertyChange("isoDateFormat", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_lastTime = "lastTime";

   private String lastTime = isoDateFormat.format(new Date());

   public String getLastTime()
   {
      return lastTime;
   }

   public LaboratoryEditor setLastTime(String value)
   {
      if (value == null ? this.lastTime != null : ! value.equals(this.lastTime))
      {
         String oldValue = this.lastTime;
         this.lastTime = value;
         firePropertyChange("lastTime", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_timeDelta = "timeDelta";

   private long timeDelta = 1;

   public long getTimeDelta()
   {
      return timeDelta;
   }

   public LaboratoryEditor setTimeDelta(long value)
   {
      if (value != this.timeDelta)
      {
         long oldValue = this.timeDelta;
         this.timeDelta = value;
         firePropertyChange("timeDelta", oldValue, value);
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

      result.append(" ").append(this.getLastTime());


      return result.substring(1);
   }

   public static final String PROPERTY_commandListeners = "commandListeners";

   private java.util.Map<String, ArrayList<CommandStream>> commandListeners = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ArrayList<CommandStream>> getCommandListeners()
   {
      return commandListeners;
   }

   public LaboratoryEditor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
   {
      if (value != this.commandListeners)
      {
         java.util.Map<String, ArrayList<CommandStream>> oldValue = this.commandListeners;
         this.commandListeners = value;
         firePropertyChange("commandListeners", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_laboratoryProducts = "laboratoryProducts";

   private java.util.Map<String, LaboratoryProduct> laboratoryProducts = new java.util.LinkedHashMap<>();

   public java.util.Map<String, LaboratoryProduct> getLaboratoryProducts()
   {
      return laboratoryProducts;
   }

   public LaboratoryEditor setLaboratoryProducts(java.util.Map<String, LaboratoryProduct> value)
   {
      if (value != this.laboratoryProducts)
      {
         java.util.Map<String, LaboratoryProduct> oldValue = this.laboratoryProducts;
         this.laboratoryProducts = value;
         firePropertyChange("laboratoryProducts", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_laboratorySuppliers = "laboratorySuppliers";

   private java.util.Map<String, LaboratorySupplier> laboratorySuppliers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, LaboratorySupplier> getLaboratorySuppliers()
   {
      return laboratorySuppliers;
   }

   public LaboratoryEditor setLaboratorySuppliers(java.util.Map<String, LaboratorySupplier> value)
   {
      if (value != this.laboratorySuppliers)
      {
         java.util.Map<String, LaboratorySupplier> oldValue = this.laboratorySuppliers;
         this.laboratorySuppliers = value;
         firePropertyChange("laboratorySuppliers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_laboratorySupplys = "laboratorySupplys";

   private java.util.Map<String, LaboratorySupply> laboratorySupplys = new java.util.LinkedHashMap<>();

   public java.util.Map<String, LaboratorySupply> getLaboratorySupplys()
   {
      return laboratorySupplys;
   }

   public LaboratoryEditor setLaboratorySupplys(java.util.Map<String, LaboratorySupply> value)
   {
      if (value != this.laboratorySupplys)
      {
         java.util.Map<String, LaboratorySupply> oldValue = this.laboratorySupplys;
         this.laboratorySupplys = value;
         firePropertyChange("laboratorySupplys", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_laboratoryCustomers = "laboratoryCustomers";

   private java.util.Map<String, LaboratoryCustomer> laboratoryCustomers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, LaboratoryCustomer> getLaboratoryCustomers()
   {
      return laboratoryCustomers;
   }

   public LaboratoryEditor setLaboratoryCustomers(java.util.Map<String, LaboratoryCustomer> value)
   {
      if (value != this.laboratoryCustomers)
      {
         java.util.Map<String, LaboratoryCustomer> oldValue = this.laboratoryCustomers;
         this.laboratoryCustomers = value;
         firePropertyChange("laboratoryCustomers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_service = "service";

   private LaboratoryService service = null;

   public LaboratoryService getService()
   {
      return this.service;
   }

   public LaboratoryEditor setService(LaboratoryService value)
   {
      if (this.service != value)
      {
         LaboratoryService oldValue = this.service;
         if (this.service != null)
         {
            this.service = null;
            oldValue.setModelEditor(null);
         }
         this.service = value;
         if (value != null)
         {
            value.setModelEditor(this);
         }
         firePropertyChange("service", oldValue, value);
      }
      return this;
   }

   public void removeYou()
   {
      this.setService(null);

   }

   public String getTime() { 
      String newTime = isoDateFormat.format(new Date());
      if (newTime.compareTo(lastTime) <= 0) {
         try {
            Date lastDate = isoDateFormat.parse(lastTime);
            long millis = lastDate.getTime();
            millis += timeDelta;
            Date newDate = new Date(millis);
            newTime = isoDateFormat.format(newDate);
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      lastTime = newTime;
      return newTime;
   }

   public void fireCommandExecuted(ModelCommand command) { 
      String commandName = command.getClass().getSimpleName();
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      for (CommandStream stream : listeners) {
         stream.publish(command);
      }
   }

   public LaboratoryEditor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.qsexample.Laboratory").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public LaboratoryProduct getOrCreateLaboratoryProduct(String id) { 
      if (id == null) {
         return null;
      }
      LaboratoryProduct oldObject = this.getLaboratoryProducts().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      LaboratoryProduct newObject = new LaboratoryProduct();
      newObject.setId(id);
      this.getLaboratoryProducts().put(id, newObject);
      return newObject;
   }

   public LaboratorySupplier getOrCreateLaboratorySupplier(String id) { 
      if (id == null) {
         return null;
      }
      LaboratorySupplier oldObject = this.getLaboratorySuppliers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      LaboratorySupplier newObject = new LaboratorySupplier();
      newObject.setId(id);
      this.getLaboratorySuppliers().put(id, newObject);
      return newObject;
   }

   public LaboratorySupply getOrCreateLaboratorySupply(String id) { 
      if (id == null) {
         return null;
      }
      LaboratorySupply oldObject = this.getLaboratorySupplys().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      LaboratorySupply newObject = new LaboratorySupply();
      newObject.setId(id);
      this.getLaboratorySupplys().put(id, newObject);
      return newObject;
   }

   public LaboratoryCustomer getOrCreateLaboratoryCustomer(String id) { 
      if (id == null) {
         return null;
      }
      LaboratoryCustomer oldObject = this.getLaboratoryCustomers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      LaboratoryCustomer newObject = new LaboratoryCustomer();
      newObject.setId(id);
      this.getLaboratoryCustomers().put(id, newObject);
      return newObject;
   }

}

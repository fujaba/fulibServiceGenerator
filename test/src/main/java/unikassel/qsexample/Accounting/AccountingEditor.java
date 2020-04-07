package unikassel.qsexample.Accounting;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import org.fulib.yaml.Yaml;
import java.util.ArrayList;

public class AccountingEditor  
{

   public static final String PROPERTY_activeCommands = "activeCommands";

   private java.util.Map<String, ModelCommand> activeCommands = new java.util.LinkedHashMap<>();

   public java.util.Map<String, ModelCommand> getActiveCommands()
   {
      return activeCommands;
   }

   public AccountingEditor setActiveCommands(java.util.Map<String, ModelCommand> value)
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

   public AccountingEditor setRemoveCommands(java.util.Map<String, RemoveCommand> value)
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

   public AccountingEditor setIsoDateFormat(DateFormat value)
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

   public AccountingEditor setLastTime(String value)
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

   public AccountingEditor setTimeDelta(long value)
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

   public AccountingEditor setCommandListeners(java.util.Map<String, ArrayList<CommandStream>> value)
   {
      if (value != this.commandListeners)
      {
         java.util.Map<String, ArrayList<CommandStream>> oldValue = this.commandListeners;
         this.commandListeners = value;
         firePropertyChange("commandListeners", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_accountingProducts = "accountingProducts";

   private java.util.Map<String, AccountingProduct> accountingProducts = new java.util.LinkedHashMap<>();

   public java.util.Map<String, AccountingProduct> getAccountingProducts()
   {
      return accountingProducts;
   }

   public AccountingEditor setAccountingProducts(java.util.Map<String, AccountingProduct> value)
   {
      if (value != this.accountingProducts)
      {
         java.util.Map<String, AccountingProduct> oldValue = this.accountingProducts;
         this.accountingProducts = value;
         firePropertyChange("accountingProducts", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_accountingSuppliers = "accountingSuppliers";

   private java.util.Map<String, AccountingSupplier> accountingSuppliers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, AccountingSupplier> getAccountingSuppliers()
   {
      return accountingSuppliers;
   }

   public AccountingEditor setAccountingSuppliers(java.util.Map<String, AccountingSupplier> value)
   {
      if (value != this.accountingSuppliers)
      {
         java.util.Map<String, AccountingSupplier> oldValue = this.accountingSuppliers;
         this.accountingSuppliers = value;
         firePropertyChange("accountingSuppliers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_accountingSupplys = "accountingSupplys";

   private java.util.Map<String, AccountingSupply> accountingSupplys = new java.util.LinkedHashMap<>();

   public java.util.Map<String, AccountingSupply> getAccountingSupplys()
   {
      return accountingSupplys;
   }

   public AccountingEditor setAccountingSupplys(java.util.Map<String, AccountingSupply> value)
   {
      if (value != this.accountingSupplys)
      {
         java.util.Map<String, AccountingSupply> oldValue = this.accountingSupplys;
         this.accountingSupplys = value;
         firePropertyChange("accountingSupplys", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_accountingCustomers = "accountingCustomers";

   private java.util.Map<String, AccountingCustomer> accountingCustomers = new java.util.LinkedHashMap<>();

   public java.util.Map<String, AccountingCustomer> getAccountingCustomers()
   {
      return accountingCustomers;
   }

   public AccountingEditor setAccountingCustomers(java.util.Map<String, AccountingCustomer> value)
   {
      if (value != this.accountingCustomers)
      {
         java.util.Map<String, AccountingCustomer> oldValue = this.accountingCustomers;
         this.accountingCustomers = value;
         firePropertyChange("accountingCustomers", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_service = "service";

   private AccountingService service = null;

   public AccountingService getService()
   {
      return this.service;
   }

   public AccountingEditor setService(AccountingService value)
   {
      if (this.service != value)
      {
         AccountingService oldValue = this.service;
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

   public AccountingEditor addCommandListener(String commandName, CommandStream stream) { 
      ArrayList<CommandStream> listeners = commandListeners.computeIfAbsent(commandName, s -> new ArrayList<>());
      listeners.add(stream);
      return this;
   }

   public void loadYaml(String yamlString) { 
      java.util.Map map = Yaml.forPackage("unikassel.qsexample.Accounting").decode(yamlString);
      for (Object value : map.values()) {
         ModelCommand cmd = (ModelCommand) value;
         cmd.run(this);
      }
   }

   public AccountingProduct getOrCreateAccountingProduct(String id) { 
      if (id == null) {
         return null;
      }
      AccountingProduct oldObject = this.getAccountingProducts().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      AccountingProduct newObject = new AccountingProduct();
      newObject.setId(id);
      this.getAccountingProducts().put(id, newObject);
      return newObject;
   }

   public AccountingSupplier getOrCreateAccountingSupplier(String id) { 
      if (id == null) {
         return null;
      }
      AccountingSupplier oldObject = this.getAccountingSuppliers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      AccountingSupplier newObject = new AccountingSupplier();
      newObject.setId(id);
      this.getAccountingSuppliers().put(id, newObject);
      return newObject;
   }

   public AccountingSupply getOrCreateAccountingSupply(String id) { 
      if (id == null) {
         return null;
      }
      AccountingSupply oldObject = this.getAccountingSupplys().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      AccountingSupply newObject = new AccountingSupply();
      newObject.setId(id);
      this.getAccountingSupplys().put(id, newObject);
      return newObject;
   }

   public AccountingCustomer getOrCreateAccountingCustomer(String id) { 
      if (id == null) {
         return null;
      }
      AccountingCustomer oldObject = this.getAccountingCustomers().get(id);
      if (oldObject != null) {
         return oldObject;
      }
      AccountingCustomer newObject = new AccountingCustomer();
      newObject.setId(id);
      this.getAccountingCustomers().put(id, newObject);
      return newObject;
   }

}

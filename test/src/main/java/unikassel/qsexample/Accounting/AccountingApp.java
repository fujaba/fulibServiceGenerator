package unikassel.qsexample.Accounting;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class AccountingApp  
{

   public static final String PROPERTY_modelEditor = "modelEditor";

   private AccountingEditor modelEditor;

   public AccountingEditor getModelEditor()
   {
      return modelEditor;
   }

   public AccountingApp setModelEditor(AccountingEditor value)
   {
      if (value != this.modelEditor)
      {
         AccountingEditor oldValue = this.modelEditor;
         this.modelEditor = value;
         firePropertyChange("modelEditor", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public AccountingApp setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_description = "description";

   private String description;

   public String getDescription()
   {
      return description;
   }

   public AccountingApp setDescription(String value)
   {
      if (value == null ? this.description != null : ! value.equals(this.description))
      {
         String oldValue = this.description;
         this.description = value;
         firePropertyChange("description", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_content = "content";

   private Page content = null;

   public Page getContent()
   {
      return this.content;
   }

   public AccountingApp setContent(Page value)
   {
      if (this.content != value)
      {
         Page oldValue = this.content;
         if (this.content != null)
         {
            this.content = null;
            oldValue.setApp(null);
         }
         this.content = value;
         if (value != null)
         {
            value.setApp(this);
         }
         firePropertyChange("content", oldValue, value);
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

      result.append(" ").append(this.getId());
      result.append(" ").append(this.getDescription());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setContent(null);

   }

   private String toolBar = "button supply";

   public void supply()
   {
      Page page = new Page().setId("supplyPage").setDescription(toolBar).setApp(this);
      if (modelEditor.getAccountingSupplys().size() == 0) {
         addSupply();
         return;
      }
      for (AccountingSupply supply : modelEditor.getAccountingSupplys().values()) {
         new Line().setId(supply.getId()).setPage(page)
               .setDescription(String.format("button %s %s %s", supply.getSupplier(), supply.getProduct().getDescription(), supply.getState()));
      }
      new Line().setId("addButton").setDescription("button addSupply").setPage(page);
   }

   public void addSupply()
   {
      Page page = new Page().setId("supplyPage").setDescription(toolBar).setApp(this);
      new Line().setId("supplierIn").setDescription("input supplier?").setPage(page);
      new Line().setId("productIn").setDescription("input product?").setPage(page);
      new Line().setId("itemsIn").setDescription("input items?").setPage(page);
      new Line().setId("addButton").setDescription("button add").setPage(page)
            .setAction("AddSupply supplierIn productIn itemsIn supply");
   }

   public AccountingApp init(AccountingEditor editor) // no fulib
   {
      this.modelEditor = editor;
      this.setId("root");
      this.setDescription("Accounting App");
      supply();
      return this;
   }

}

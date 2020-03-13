package unikassel.websystem.Store;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class StoreApp  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public StoreApp setId(String value)
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

   public StoreApp setDescription(String value)
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

   public StoreApp setContent(Page value)
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

   public StoreApp init(StoreEditor editor) // no fulib
   {
      this.modelEditor = editor;
      this.setId("root");
      this.setDescription("Store App");
      supplyPage();

      return this;
   }

   public void supplyPage()
   {
      Page supplyPage = new Page().setId("supplyPage")
            .setDescription("New Supply")
            .setApp(this);

      new Line().setId("onStock")
            .setDescription(String.format("We have %d products on stock", this.modelEditor.getStoreProducts().size()))
            .setPage(supplyPage);
      new Line().setId("idIn").setDescription("input product id?").setPage(supplyPage);
      new Line().setId("descriptionIn").setDescription("input product description?").setPage(supplyPage);
      new Line().setId("itemsIn").setDescription("input number of items?").setPage(supplyPage);
      new Line().setId("addButton").setDescription("button add").setPage(supplyPage)
      .setAction("HaveProductCommand idIn descriptionIn itemsIn supplyPage");
   }

   public static final String PROPERTY_modelEditor = "modelEditor";

   private StoreEditor modelEditor;

   public StoreEditor getModelEditor()
   {
      return modelEditor;
   }

   public StoreApp setModelEditor(StoreEditor value)
   {
      if (value != this.modelEditor)
      {
         StoreEditor oldValue = this.modelEditor;
         this.modelEditor = value;
         firePropertyChange("modelEditor", oldValue, value);
      }
      return this;
   }

}

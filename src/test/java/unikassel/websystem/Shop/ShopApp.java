package unikassel.websystem.Shop;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ShopApp  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public ShopApp setId(String value)
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

   public ShopApp setDescription(String value)
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

   public ShopApp setContent(Page value)
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

   public static final String PROPERTY_modelEditor = "modelEditor";

   private ShopEditor modelEditor;

   public ShopEditor getModelEditor()
   {
      return modelEditor;
   }

   public ShopApp setModelEditor(ShopEditor value)
   {
      if (value != this.modelEditor)
      {
         ShopEditor oldValue = this.modelEditor;
         this.modelEditor = value;
         firePropertyChange("modelEditor", oldValue, value);
      }
      return this;
   }

   public ShopApp init(ShopEditor editor) // no fulib
   {
      this.modelEditor = editor;
      this.setId("root");
      this.setDescription("Shop App");

      shop();

      return this;
   }

   public void shop()
   {
      Page shopPage = new Page().setId("shopPage").setDescription("button shop | button card | button orders").setApp(this);
      // show all available products
      for (ShopProduct product : modelEditor.getShopProducts().values()) {
         if (product.getOffers().size() == 0) {
            new HaveOfferCommand().setId(String.format("offer_%s_1", product.getId()))
            .setProduct(product.getId())
            .setPrice(9.99)
            .run(modelEditor);
         }

         ShopOffer offer = product.getOffers().get(0);

         new Line().setId("buy_" + offer.getId())
               .setDescription(String.format("button buy %s %s %s", product.getId(), product.getDescription(), offer.getPrice()))
               .setAction(String.format("addToCard?id=%s shop", product.getId()))
               .setPage(shopPage);
      }
   }

   private ShopOrder card = null;

   public void addToCard(String productId) {
      // have an order
      if (this.card == null) {
         String orderId = "order_" + (modelEditor.getShopOrders().size() + 1);
         card = new HaveOrderCommand().setId(orderId).run(modelEditor);
      }

      // add position
      new HaveOrderPositionCommand().setId(String.format("%s_pos_%d", card.getId(), card.getPositions().size() + 1))
            .setOrder(card.getId())
            .setAmount(1.0);
   }

}

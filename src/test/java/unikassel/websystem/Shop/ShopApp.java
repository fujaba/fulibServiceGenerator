package unikassel.websystem.Shop;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ShopApp  
{

   public static final String PROPERTY_id = "id";
   private final String toolBarDescription = "button shop | button card | button orders | button account";

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
      this.setShoppingCard(null);

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
      Page shopPage = new Page().setId("shopPage").setDescription(toolBarDescription).setApp(this);
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
               .setAction(String.format("AddToCard?offer=%s shop", offer.getId()))
               .setPage(shopPage);
      }
   }

   public void card()
   {
      Page cardPage = new Page().setId("cardPage").setDescription(toolBarDescription).setApp(this);

      if (shoppingCard == null) {
         String orderId = "order_" + (modelEditor.getShopOrders().size() + 1);
         String customerId = null;
         if (customer != null) {
            customerId = customer.getId();
         }
         shoppingCard = new HaveOrderCommand()
               .setId(orderId)
               .setState("collecting-items")
               .setCustomer(customerId)
               .run(modelEditor);
         this.setShoppingCard(shoppingCard);
      }

      // show the order positions
      ShopOrder card = this.getShoppingCard();
      for (ShopOrderPosition position : card.getPositions()) {
         ShopOffer offer = position.getOffer();
         new Line().setId(position.getId()).setPage(cardPage)
               .setDescription(String.format("%s %.2f",
                     offer.getProduct().getDescription(),
                     offer.getPrice()));
      }

      Line nameIn = new Line().setId("nameIn").setDescription("input your name?").setPage(cardPage);
      Line addressIn = new Line().setId("addressIn").setDescription("input delivery address?").setPage(cardPage);
      if (this.getCustomer() != null) {
         nameIn.setValue(customer.getName());
         addressIn.setValue(customer.getAddress());
      }
      new Line().setId("buyButton").setDescription("button Buy")
            .setAction("OrderAction nameIn addressIn orders")
            .setPage(cardPage);
   }

   public void orders() {
      Page ordersPage = new Page().setId("ordersPage").setDescription(toolBarDescription).setApp(this);

      if (customer != null) {
         if (customer.getOrders().size() == 0) {
            new Line().setId("noOrders").setDescription(customer.getName() + ", you have not yet ordered anything").setPage(ordersPage);
         }
         else {
            for (ShopOrder order : customer.getOrders()) {
               displayOneOrder(order, ordersPage);
            }
         }
      }
      else if (this.getShoppingCard() != null) {
         displayOneOrder(this.getShoppingCard(), ordersPage);
      }
      else {
         new Line().setId("noOrders").setDescription("You have not yet ordered anything").setPage(ordersPage);
      }
   }

   private void displayOneOrder(ShopOrder order, Page ordersPage)
   {
      String orderDescription = order.getId();
      for (ShopOrderPosition position : order.getPositions()) {
         orderDescription += " " + position.getOffer().getProduct().getDescription();
      }
      orderDescription += " " + order.getState();
      new Line().setId("positionsOf" + order.getId()).setDescription(orderDescription).setPage(ordersPage);
   }

   public void account() {
      Page accountPage = new Page().setId("accountPage").setDescription(toolBarDescription).setApp(this);
      Line nameIn = new Line().setId("nameIn").setDescription("input your name?").setPage(accountPage);
      Line addressIn = new Line().setId("addressIn").setDescription("input delivery address?").setPage(accountPage);
      if (this.getCustomer() != null) {
         nameIn.setValue(customer.getName());
         addressIn.setValue(customer.getAddress());
      }
      new Line().setId("submitButton").setDescription("button Submit")
            .setAction("CustomerAccount nameIn addressIn shop")
            .setPage(accountPage);
   }

   private ShopOrder card = null;

   public static final String PROPERTY_shoppingCard = "shoppingCard";

   private ShopOrder shoppingCard = null;

   public ShopOrder getShoppingCard()
   {
      return this.shoppingCard;
   }

   public ShopApp setShoppingCard(ShopOrder value)
   {
      if (this.shoppingCard != value)
      {
         ShopOrder oldValue = this.shoppingCard;
         if (this.shoppingCard != null)
         {
            this.shoppingCard = null;
            oldValue.setShopApp(null);
         }
         this.shoppingCard = value;
         if (value != null)
         {
            value.setShopApp(this);
         }
         firePropertyChange("shoppingCard", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_customer = "customer";

   private ShopCustomer customer;

   public ShopCustomer getCustomer()
   {
      return customer;
   }

   public ShopApp setCustomer(ShopCustomer value)
   {
      if (value != this.customer)
      {
         ShopCustomer oldValue = this.customer;
         this.customer = value;
         firePropertyChange("customer", oldValue, value);
      }
      return this;
   }

}

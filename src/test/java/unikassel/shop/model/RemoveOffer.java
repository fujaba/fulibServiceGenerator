package unikassel.shop.model;

public class RemoveOffer extends ModelCommand  
{

   @Override
   public Object run(StoreModelEditor editor)
   {
      ModelCommand oldCommand = editor.getActiveCommands().get("Offer-" + this.getId());

      if (oldCommand == null) {
         // nothing to be removed
         editor.getActiveCommands().put("Offer-" + this.getId(), this);
         return null;
      }

      if (oldCommand.getClass() == RemoveOffer.class) {
         // we don't care for the time
         return null;
      }

      HaveOfferCommand offerCommand = (HaveOfferCommand) oldCommand;
      String id = offerCommand.getId();
      Offer oldOffer = (Offer) editor.getModel().get("Offer-" + this.getId());
      oldOffer.removeYou();
      editor.getModel().remove("Offer-" + this.getId());
      editor.getActiveCommands().put("Offer-" + this.getId(), this);
      return null;
   }

}

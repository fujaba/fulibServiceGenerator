package unikassel.qsexample;

import org.fulib.servicegenerator.FulibScenarioDiagram;
import org.junit.Test;
import unikassel.qsexample.Accounting.AccountingApp;
import unikassel.qsexample.Accounting.AccountingService;
import unikassel.qsexample.Accounting.HaveProductCommand;
import unikassel.qsexample.Ramp.HaveSupplierCommand;
import unikassel.qsexample.Ramp.HaveSupplyCommand;
import unikassel.qsexample.Ramp.RampService;

import java.util.Collection;
import java.util.LinkedHashMap;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestQSExample
{
   @Test
   public void testMessageMerging()
   {
      AccountingService accountingService = new AccountingService().setMyPort(22020);
      accountingService.start();

      RampService rampService = new RampService().setMyPort(22030);
      rampService.start();

      rampService.connectTo("Ramp0", "http://localhost:22030","Accounting", "http://localhost:22020",
            "<->",
            HaveSupplyCommand.class.getSimpleName(),
            HaveSupplierCommand.class.getSimpleName(),
            HaveProductCommand.class.getSimpleName());


      FulibScenarioDiagram scene1 = new FulibScenarioDiagram();
      scene1.setHtmlFileName("tmp/QSScene.html");
      scene1.addServices("Alice's PC", "Accounting", "Ramp0");

      open("http://localhost:22020/Accounting");
      $("#supplierIn").$("input").setValue("Rome Italy");
      $("#productIn").$("input").setValue("p1 pumps");
      $("#itemsIn").$("input").setValue("100");

      LinkedHashMap<String, AccountingApp> sessionToAppMap = accountingService.getSessionToAppMap();
      Collection<AccountingApp> values = sessionToAppMap.values();
      AccountingApp accountingApp = values.iterator().next();
      scene1.addScreen("Alice's PC","9:00", accountingApp);

      $("#addButton").$("button").click();
      scene1.addOneMessage("9:01", "Alice's PC", 0, accountingService.getModelEditor().getActiveCommands().values());

      $("#addButton").$("button").click();

      $("#supplierIn").$("input").setValue("Rome Italy");
      $("#productIn").$("input").setValue("p2 boots");
      $("#itemsIn").$("input").setValue("50");
      scene1.addScreen("Alice's PC","9:02", accountingApp);
      $("#addButton").$("button").click();

      scene1.addOneMessage("9:03", "Alice's PC", 0, accountingService.getModelEditor().getActiveCommands().values());
      scene1.addData("9:04", "Accounting", accountingService.getModelEditor().getActiveCommands().values());
      scene1.addMessages("9:05", accountingService);
      scene1.addData("9:06", "Ramp0", rampService.getModelEditor().getActiveCommands().values());
      System.out.println();
   }
}

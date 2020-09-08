package unikassel.qsexample;

import org.fulib.services.FulibScenarioDiagram;
import org.junit.Test;
import org.openqa.selenium.By;
import unikassel.qsexample.Accounting.AccountingApp;
import unikassel.qsexample.Accounting.AccountingService;
import unikassel.qsexample.Accounting.CommandStream;
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
      CommandStream accountingDataStream = accountingService.getStream("AccountingData4Ramps");
      accountingDataStream.addCommandsToBeStreamed(
            HaveSupplyCommand.class.getSimpleName(),
            HaveSupplierCommand.class.getSimpleName(),
            HaveProductCommand.class.getSimpleName()
      );
      accountingDataStream.getTargetUrlList().add("http://localhost:22030/RampData4Accounting");

      RampService rampService = new RampService().setMyPort(22030);
      rampService.start();
      rampService.getStream("RampData4Accounting");
      unikassel.qsexample.Ramp.CommandStream rampData4RampClients = rampService.getStream("RampData4RampClients");
      rampData4RampClients.addCommandsToBeStreamed(
            HaveSupplyCommand.class.getSimpleName(),
            HaveSupplierCommand.class.getSimpleName(),
            HaveProductCommand.class.getSimpleName()
      );
      rampData4RampClients.getTargetUrlList().add("http://localhost:22031/RampClientData4RampHub");
      rampData4RampClients.getTargetUrlList().add("http://localhost:22032/RampClientData4RampHub");

      RampService rampBobService = new RampService().setMyPort(22031);
      rampBobService.start();
      unikassel.qsexample.Ramp.CommandStream rampClientData4RampHub = rampBobService.getStream("RampClientData4RampHub");



      RampService rampCarliService = new RampService().setMyPort(22032);
      rampCarliService.start();
      rampCarliService.getStream("RampClientData4RampHub");



      FulibScenarioDiagram scene1 = new FulibScenarioDiagram();
      scene1.setHtmlFileName("tmp/QSScene.html");
      scene1.addServices("Alice's PC", "Accounting", "Ramp0", "RampBob", "RampCarli");

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
      scene1.addOneMessage("9:07", "Ramp0ToClientRamps", 0, rampService.getModelEditor().getActiveCommands().values());
      scene1.addData("9:08", "RampBob", rampBobService.getModelEditor().getActiveCommands().values());
      scene1.addData("9:09", "RampCarli", rampCarliService.getModelEditor().getActiveCommands().values());

      open("http://localhost:22031/Ramp");
      scene1.addScreen("RampBob", "10:00", rampBobService.getSessionToAppMap().values().iterator().next(), "[Rome pumps ordered]");
      $(By.xpath("//button[text()='Rome pumps ordered']")).click();
      $("#paletteIn").$("input").setValue("pal1");
      $("#itemsIn").$("input").setValue("50");
      scene1.addScreen("RampBob", "10:05", rampBobService.getSessionToAppMap().values().iterator().next(), "[add]");

      System.out.println();
   }
}

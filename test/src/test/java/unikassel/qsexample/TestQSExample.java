package unikassel.qsexample;

import org.fulib.servicegenerator.FulibScenarioDiagram;
import org.junit.Test;
import unikassel.qsexample.Accounting.AccountingApp;
import unikassel.qsexample.Accounting.AccountingService;
import unikassel.qsexample.Ramp.RampService;

import java.util.Collection;
import java.util.LinkedHashMap;

import static com.codeborne.selenide.Selenide.open;

public class TestQSExample
{
   @Test
   public void testMessageMerging()
   {
      AccountingService accountingService = new AccountingService().setMyPort(22022);
      accountingService.start();

      RampService rampService = new RampService().setMyPort(22022);
      rampService.start();

      FulibScenarioDiagram scene1 = new FulibScenarioDiagram();
      scene1.setHtmlFileName("tmp/QSScene.html");
      scene1.addServices(accountingService, rampService);

      open("http://localhost:22022/Accounting");

      LinkedHashMap<String, AccountingApp> sessionToAppMap = accountingService.getSessionToAppMap();
      Collection<AccountingApp> values = sessionToAppMap.values();
      scene1.addScreen("9:00", values.iterator().next());

      System.out.println();
   }
}

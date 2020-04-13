package unikassel.bpmn2wf;

import org.fulib.servicegenerator.FulibScenarioDiagram;
import org.junit.Test;
import org.openqa.selenium.By;
import unikassel.bpmn2wf.BPMN.AddFlow;
import unikassel.bpmn2wf.BPMN.AddStep;
import unikassel.bpmn2wf.BPMN.BPMNApp;
import unikassel.bpmn2wf.BPMN.BPMNService;
import unikassel.bpmn2wf.WorkFlows.WorkFlowsService;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestBPMN2WorkFlow
{
   @Test
   public void testBXAndMultiEditing()
   {
      BPMNService bpmnService = new BPMNService().setMyPort(22040);
      bpmnService.start();

      WorkFlowsService workFlowsService = new WorkFlowsService().setMyPort(22050);
      workFlowsService.start();

      bpmnService.connectTo("BPMN", "http://localhost:22040","WorkFlow", "http://localhost:22050",
            AddStep.class.getSimpleName(),
            AddFlow.class.getSimpleName(),
            "<->",
            AddStep.class.getSimpleName(),
            AddFlow.class.getSimpleName());

      FulibScenarioDiagram scene1 = new FulibScenarioDiagram();
      scene1.setHtmlFileName("tmp/BPMN2WorkFlow.html");
      scene1.addServices("BPMN", "WorkFlow");

      open("http://localhost:22040/BPMN");
      $(By.xpath("//button[text()='addStep']")).click();

      $("#taskIdIn").$("input").setValue("t0");
      $("#taskTextIn").$("input").setValue("task 0");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("start");
      $("#targetIn").$("input").setValue("t0");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("pg1");
      $("#taskKindIn").$("input").setValue("gate");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t0");
      $("#targetIn").$("input").setValue("pg1");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("t2");
      $("#taskTextIn").$("input").setValue("task 2");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1");
      $("#targetIn").$("input").setValue("t2");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("t1");
      $("#taskTextIn").$("input").setValue("task 1");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1");
      $("#targetIn").$("input").setValue("t1");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("pg2");
      $("#taskKindIn").$("input").setValue("gate");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t2");
      $("#targetIn").$("input").setValue("pg2");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t1");
      $("#targetIn").$("input").setValue("pg2");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg2");
      $("#targetIn").$("input").setValue("end");
      $(By.xpath("//button[text()='add']")).click();

      BPMNApp bpmnApp = bpmnService.getSessionToAppMap().values().iterator().next();
      scene1.addScreen("BPMN", "9:00", bpmnApp);

      System.out.println();
   }
}

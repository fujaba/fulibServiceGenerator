package unikassel.bpmn2wf;

import org.fulib.FulibTools;
import org.fulib.servicegenerator.FulibScenarioDiagram;
import org.fulib.yaml.Yaml;
import org.junit.Test;
import org.openqa.selenium.By;
import unikassel.bpmn2wf.BPMN.*;
import unikassel.bpmn2wf.WorkFlows.WorkFlowsApp;
import unikassel.bpmn2wf.WorkFlows.WorkFlowsService;

import java.util.Collection;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestBPMN2WorkFlow
{
   @Test
   public void testBPMNEditing() throws InterruptedException
   {
      BPMNService bpmnService = new BPMNService().setMyPort(22042);
      bpmnService.start();

      open("http://localhost:22042/BPMN");
      Thread.sleep(500);

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("t0");
      $("#taskTextIn").$("input").setValue("check stock");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addGatePair']")).click();
      $("#gateIdIn").$("input").setValue("pg1");
      $("#gateKindIn").$("input").setValue("parallel");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t0");
      $("#targetIn").$("input").setValue("pg1_dot_start");
      $(By.xpath("//button[text()='add']")).click();

      FulibTools.objectDiagrams().dumpSVG("tmp/BPMNScreen1Model.svg",
            bpmnService.getModelEditor().taskMap.values());

      FulibTools.objectDiagrams().dumpSVG("tmp/BPMNScreen1Commands.svg",
            bpmnService.getModelEditor().getActiveCommands().values());

      System.out.println();

   }

      @Test
   public void testBXAndMultiEditing()
   {
      BPMNService bpmnService = new BPMNService().setMyPort(22040);
      bpmnService.start();
      CommandStream bpmnData4WorkFlows = bpmnService.getStream("BPMNData4WorkFlows");
      bpmnData4WorkFlows.addCommandsToBeStreamed(
            AddStep.class.getSimpleName(),
            AddParallel.class.getSimpleName(),
            AddFlow.class.getSimpleName()
      );

      WorkFlowsService workFlowsService = new WorkFlowsService().setMyPort(22050);
      workFlowsService.start();
      unikassel.bpmn2wf.WorkFlows.CommandStream workFlowData4BPMN = workFlowsService.getStream("WorkFlowData4BPMN");
      workFlowData4BPMN.addCommandsToBeStreamed(
            AddStep.class.getSimpleName(),
            AddParallel.class.getSimpleName(),
            AddFlow.class.getSimpleName()
      );

      bpmnData4WorkFlows.getTargetUrlList().add("http://localhost:22050/WorkFlowData4BPMN");
      workFlowData4BPMN.getTargetUrlList().add("http://localhost:22040/BPMNData4WorkFlows");

      FulibScenarioDiagram scene1 = new FulibScenarioDiagram();
      scene1.setHtmlFileName("tmp/BPMN2WorkFlow.html");
      scene1.addServices("BPMN", "WorkFlow");

      open("http://localhost:22040/BPMN");
      $(By.xpath("//button[text()='addStep']")).click();

      $("#taskIdIn").$("input").setValue("t0");
      $("#taskTextIn").$("input").setValue("check stock");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("start");
      $("#targetIn").$("input").setValue("t0");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addGatePair']")).click();
      $("#gateIdIn").$("input").setValue("pg1");
      $("#gateKindIn").$("input").setValue("parallel");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t0");
      $("#targetIn").$("input").setValue("pg1_dot_start");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("t2");
      $("#taskTextIn").$("input").setValue("charge");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1_dot_start");
      $("#targetIn").$("input").setValue("t2");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("t1");
      $("#taskTextIn").$("input").setValue("collect");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1_dot_start");
      $("#targetIn").$("input").setValue("t1");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t2");
      $("#targetIn").$("input").setValue("pg1_dot_end");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t1");
      $("#targetIn").$("input").setValue("pg1_dot_end");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1_dot_end");
      $("#targetIn").$("input").setValue("end");
      $(By.xpath("//button[text()='add']")).click();

      BPMNApp bpmnApp = bpmnService.getSessionToAppMap().values().iterator().next();
      scene1.addScreen("BPMN", "9:00", bpmnApp);

      open("http://localhost:22050/WorkFlows");
      WorkFlowsApp wfApp = workFlowsService.getSessionToAppMap().values().iterator().next();
      scene1.addScreen("WorkFlow", "9:00", wfApp);

      FulibTools.objectDiagrams().dumpSVG("tmp/BPMNModel.svg",
            bpmnService.getModelEditor().taskMap.get("start"));

      FulibTools.objectDiagrams().dumpSVG("tmp/WorkFlowModel.svg",
            workFlowsService.getModelEditor().root);

      Collection<unikassel.bpmn2wf.WorkFlows.ModelCommand> commands = workFlowsService.getModelEditor().getActiveCommands().values();
      FulibTools.objectDiagrams().dumpSVG("tmp/CommandModel.svg",
            commands);

      System.out.println();
   }
}

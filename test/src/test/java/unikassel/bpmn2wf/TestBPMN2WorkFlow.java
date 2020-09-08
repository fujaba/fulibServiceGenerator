package unikassel.bpmn2wf;

import org.fulib.FulibTools;
import org.fulib.services.FulibScenarioDiagram;
import org.junit.Test;
import org.openqa.selenium.By;
import unikassel.bpmn2wf.BPMN.*;
import unikassel.bpmn2wf.BPMN.AddFlow;
import unikassel.bpmn2wf.BPMN.AddParallel;
import unikassel.bpmn2wf.BPMN.AddStep;
import unikassel.bpmn2wf.BPMN.CommandStream;
import unikassel.bpmn2wf.WorkFlows.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBPMN2WorkFlow
{
   @Test
   public void testWorkFlowTextEditing()
   {
      WorkFlowsService workFlowsService = new WorkFlowsService();
      workFlowsService.start();
      WorkFlowsEditor editor = workFlowsService.getModelEditor();
      WorkFlowsApp myApp = new WorkFlowsApp().init(editor);

      try {
         byte[] bytes = Files.readAllBytes(Paths.get("tmp/WorkFlowCommandHistory.yaml"));
         String yaml = new String(bytes, StandardCharsets.UTF_8);

         // load commandstream and build model
         editor.loadYaml(yaml);

         // unparse to string
         myApp.diagram();
         String flowText = myApp.buf.toString();
         System.out.println(flowText);
         assertThat(flowText.startsWith("basic flow"), is(true));

         // edit flow string
         String changedText = flowText.replace("\"charge\"",
               "\"billing\" next t3\n" +
                     "    step t3 \"accounting\"");
         changedText = changedText.replace("t0", "prep");

         // parse changed text
         Parse parseCmd = new Parse();
         parseCmd.setText(changedText);
         parseCmd.run(null);
         unikassel.bpmn2wf.WorkFlows.Flow basicFlow = parseCmd.parseTreeListener.basicFlow;
         assertThat(basicFlow, not(equals(null)));

         Map<String, unikassel.bpmn2wf.WorkFlows.ModelCommand> newHistory = editor.parseModelToCommandSet(basicFlow);
         assertThat(newHistory.get("AddFlow-pg1_end_end"), notNullValue());

         editor.mergeIntoHistory(newHistory);
         Step t2 = editor.stepMap.get("t2");
         assertThat(t2.getText(), equalTo("billing"));
         assertThat(t2.getParent().getKind(), equalTo("parallel"));

         myApp.diagram();
         String newFlowText = myApp.buf.toString();
         System.out.println(newFlowText);
         assertThat(newFlowText.startsWith("basic flow"), is(true));

         System.out.println();
      }
      catch (IOException e) {
         return;
      }

   }

   @Test
   public void testWorkFlowParser()
   {
      String text = "" +
            "basic flow\n" +
            "    step t0 \"check stock\" next pg1\n" +
            "    parallel step pg1 subflows t2, t1 \n" +
            "end flow\n" +
            "parallel flow pg1 t2\n" +
            "    step t2 \"charge\" \n" +
            "end flow\n" +
            "parallel flow pg1 t1\n" +
            "    step t1 \"collect\" \n" +
            "end flow";

      // get model from text
      Parse parseCmd = new Parse();
      parseCmd.setText(text);
      parseCmd.run(null);
      unikassel.bpmn2wf.WorkFlows.Flow basicFlow = parseCmd.parseTreeListener.basicFlow;
      assertThat(basicFlow, not(equals(null)));
      FulibTools.objectDiagrams().dumpSVG("tmp/WorkFlowModelParsed.svg", basicFlow);


      System.out.println();



   }

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
      $("#targetIn").$("input").setValue("pg1_start");
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
      $("#targetIn").$("input").setValue("pg1_start");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("t2");
      $("#taskTextIn").$("input").setValue("charge");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1_start");
      $("#targetIn").$("input").setValue("t2");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addStep']")).click();
      $("#taskIdIn").$("input").setValue("t1");
      $("#taskTextIn").$("input").setValue("collect");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1_start");
      $("#targetIn").$("input").setValue("t1");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t2");
      $("#targetIn").$("input").setValue("pg1_end");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("t1");
      $("#targetIn").$("input").setValue("pg1_end");
      $(By.xpath("//button[text()='add']")).click();

      $(By.xpath("//button[text()='addFlow']")).click();
      $("#sourceIn").$("input").setValue("pg1_end");
      $("#targetIn").$("input").setValue("end");
      $(By.xpath("//button[text()='add']")).click();

      BPMNApp bpmnApp = bpmnService.getSessionToAppMap().values().iterator().next();
      scene1.addScreen("BPMN", "9:00", bpmnApp);

      open("http://localhost:22050/WorkFlows");
      WorkFlowsApp wfApp = workFlowsService.getSessionToAppMap().values().iterator().next();
      wfApp.getModelEditor().storeYamlCommands("tmp/WorkFlowCommandHistory.yaml");

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

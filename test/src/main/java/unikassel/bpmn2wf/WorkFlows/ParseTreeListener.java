package unikassel.bpmn2wf.WorkFlows;

import org.antlr.v4.runtime.Token;
import unikassel.bpmn2wf.WorkFlows.parser.WorkFlowGrammarBaseListener;
import unikassel.bpmn2wf.WorkFlows.parser.WorkFlowGrammarParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ParseTreeListener extends WorkFlowGrammarBaseListener
{
   public ArrayList<String> stepNames = new ArrayList<>();
   public LinkedHashMap<String, Step> stepMap = new LinkedHashMap<>();


   public Flow basicFlow = null;
   Flow currentFlow = null;

   @Override
   public void enterBasicFlow(WorkFlowGrammarParser.BasicFlowContext ctx)
   {
      basicFlow = new Flow().setKind("basic");
      currentFlow = basicFlow;
   }

   @Override
   public void exitBasicStep(WorkFlowGrammarParser.BasicStepContext ctx)
   {
      String stepId = ctx.stepId.getText();
      String text = ctx.text.getText();
      text = text.substring(1, text.length()-1);
      System.out.println("Found step " + stepId);
      stepNames.add(stepId);

      Step step = new Step().setId(stepId).setText(text);
      addStepToCurrentFlow(step);
   }


   @Override
   public void exitParallelStep(WorkFlowGrammarParser.ParallelStepContext ctx)
   {
      String stepId = ctx.stepId.getText();
      System.out.println("Found parallel step " + stepId);
      stepNames.add(stepId);
      Step step = new Step().setId(stepId).setKind("parallelStep");
      addStepToCurrentFlow(step);
   }

   @Override
   public void enterParallelFlow(WorkFlowGrammarParser.ParallelFlowContext ctx)
   {
      String flowId = ctx.firstStepId.getText();
      String parentId = ctx.parentId.getText();

      Step parent = stepMap.get(parentId);
      Flow flow = new Flow().setKind("parallel").setInvoker(parent);
      currentFlow = flow;
   }

   @Override
   public void exitParallelFlow(WorkFlowGrammarParser.ParallelFlowContext ctx)
   {
      String parentId = ctx.parentId.getText();
      String kidId = ctx.firstStepId.getText();
      String flowId = parentId + "_" + kidId;
      System.out.println("Found parallel flow " + flowId);
      stepNames.add(flowId);
   }


   private void addStepToCurrentFlow(Step step)
   {
      currentFlow.withSteps(step);
      currentFlow.setFinalFlow(step);
      step.setFinalFlag(true);
      if (currentFlow.getSteps().size() >= 2) {
         Step previousStep = currentFlow.getSteps().get(currentFlow.getSteps().size()-2);
         previousStep.withNext(step);
         previousStep.setFinalFlag(false);
      }

      stepMap.put(step.getId(), step);
   }


}

package unikassel.bpmn2wf.WorkFlows;

import org.antlr.v4.runtime.Token;
import unikassel.bpmn2wf.WorkFlows.parser.WorkFlowGrammarBaseListener;
import unikassel.bpmn2wf.WorkFlows.parser.WorkFlowGrammarParser;

import java.util.ArrayList;

public class ParseTreeListener extends WorkFlowGrammarBaseListener
{
   public ArrayList<String> stepNames = new ArrayList<>();

   @Override
   public void exitParallelFlow(WorkFlowGrammarParser.ParallelFlowContext ctx)
   {
      String parentId = ctx.parentId.getText();
      String kidId = ctx.firstStepId.getText();
      String flowId = parentId + "_" + kidId;
      System.out.println("Found parallel flow " + flowId);
      stepNames.add(flowId);
   }

   @Override
   public void exitBasicStep(WorkFlowGrammarParser.BasicStepContext ctx)
   {
      String stepId = ctx.stepId.getText();
      System.out.println("Found step " + stepId);
      stepNames.add(stepId);
   }

   @Override
   public void exitParallelStep(WorkFlowGrammarParser.ParallelStepContext ctx)
   {
      String stepId = ctx.stepId.getText();
      System.out.println("Found parallel step " + stepId);
      stepNames.add(stepId);

   }
}

package unikassel.bpmn2wf.WorkFlows;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import unikassel.bpmn2wf.WorkFlows.parser.WorkFlowGrammarLexer;
import unikassel.bpmn2wf.WorkFlows.parser.WorkFlowGrammarParser;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

public class Parse extends ModelCommand
{

   public ParseTreeListener parseTreeListener;

   @Override
   public Object run(WorkFlowsEditor editor)
   {
      CodePointCharStream stream = CharStreams.fromString(text);
      WorkFlowGrammarLexer lexer = new WorkFlowGrammarLexer(stream);
      CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
      WorkFlowGrammarParser parser = new WorkFlowGrammarParser(commonTokenStream);
      WorkFlowGrammarParser.FlowListContext flowListContext = parser.flowList();

      ParseTreeWalker treeWalker = new ParseTreeWalker();
      parseTreeListener = new ParseTreeListener();
      treeWalker.walk(parseTreeListener, flowListContext);

      return null;
   }

   public static final String PROPERTY_text = "text";

   private String text;

   public String getText()
   {
      return text;
   }

   public Parse setText(String value)
   {
      if (value == null ? this.text != null : ! value.equals(this.text))
      {
         String oldValue = this.text;
         this.text = value;
         firePropertyChange("text", oldValue, value);
      }
      return this;
   }

   public boolean preCheck(WorkFlowsEditor editor) { 
      if (this.getTime() == null) {
         this.setTime(editor.getTime());
      }
      RemoveCommand oldRemove = editor.getRemoveCommands().get("Parse-" + this.getId());
      if (oldRemove != null) {
         return false;
      }
      ModelCommand oldCommand = editor.getActiveCommands().get("Parse-" + this.getId());
      if (oldCommand != null && java.util.Objects.compare(oldCommand.getTime(), this.getTime(), (a,b) -> a.compareTo(b)) >= 0) {
         return false;
      }
      editor.getActiveCommands().put("Parse-" + this.getId(), this);
      return true;
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

      result.append(" ").append(this.getText());


      return result.substring(1);
   }

}

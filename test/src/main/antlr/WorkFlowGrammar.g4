
/** Taken from "The Definitive ANTLR 4 Reference" by Terence Parr */

// Derived from http://json.org
grammar WorkFlowGrammar;

flowList: flow+ ;

flow: basicFlow | parallelFlow;

basicFlow: 'basic' 'flow' stepList 'end' 'flow';

parallelFlow: 'parallel' 'flow' parentId=ID firstStepId=ID stepList 'end' 'flow';

stepList: step+;

step: basicStep | parallelStep;

basicStep: 'step' stepId=ID STRING ('next' ID)? ;

parallelStep: 'parallel' 'step' stepId=ID 'subflows' idList;

idList: ID (',' ID)*;

STRING
   : '"' (ESC | SAFECODEPOINT)* '"'
   ;


fragment ESC
   : '\\' (["\\/bfnrt] | UNICODE)
   ;
fragment UNICODE
   : 'u' HEX HEX HEX HEX
   ;
fragment HEX
   : [0-9a-fA-F]
   ;
fragment SAFECODEPOINT
   : ~ ["\\\u0000-\u001F]
   ;

ID: [a-zA-Z] [a-zA-Z0-9_]*;

NUMBER
   : '-'? INT ('.' [0-9] +)? EXP?
   ;


fragment INT
   : '0' | [1-9] [0-9]*
   ;

// no leading zeros

fragment EXP
   : [Ee] [+\-]? INT
   ;

// \- since - means "range" inside [...]

WS
   : [ \t\n\r] + -> skip
   ;

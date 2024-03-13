lexer grammar CalculatorLexer;

ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';

INT : [0-9.]+ ;
WS: [ \t\n\r\f]+ -> skip ;
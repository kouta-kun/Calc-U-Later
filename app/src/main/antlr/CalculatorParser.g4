parser grammar CalculatorParser;
options { tokenVocab=CalculatorLexer; }

expr : INT
    | expr MUL expr
    | expr DIV expr
    | expr ADD expr
    | expr SUB expr
    ;

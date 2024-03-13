package uy.koutarou.calc_u_later;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import uy.koutarou.calc_u_later.parsing.CalculatorLexer;
import uy.koutarou.calc_u_later.parsing.CalculatorParser;

public class ExpressionProcessor {
    public float execute(String input) {
        CharStream charStream = CharStreams.fromString(input);
        CalculatorLexer lexer = new CalculatorLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CalculatorParser calculatorParser = new CalculatorParser(tokenStream);
        CalculatorParser.ExprContext expr = calculatorParser.expr();
        return processContext(expr);
    }

    private static float processContext(CalculatorParser.ExprContext expr) {
        TerminalNode number = expr.INT();
        if(number != null) {
            return Float.parseFloat(number.getText());
        }

        CalculatorParser.ExprContext left = expr.expr(0);
        CalculatorParser.ExprContext right = expr.expr(1);

        float leftValue = processContext(left);
        float rightValue = processContext(right);
        if(expr.ADD() != null) {
            return leftValue + rightValue;
        } else if (expr.SUB() != null) {
            return leftValue - rightValue;
        } else if (expr.MUL() != null) {
            return leftValue * rightValue;
        } else if (expr.DIV() != null) {
            return leftValue / rightValue;
        } else {
            throw new RuntimeException("Unexpected expression" + expr.getText());
        }
    }
}

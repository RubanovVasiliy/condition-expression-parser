package org.example;

import antlr4.LogFilterBaseVisitor;
import antlr4.LogFilterParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashSet;
import java.util.Set;


public class ErrorCheckerVisitor extends LogFilterBaseVisitor<Void> {
    private final Set<Integer> indexes = new HashSet<>();
    private final Integer maxIndex;
    private final Integer minIndex;

    public ErrorCheckerVisitor(Integer minIndex, Integer maxIndex) {
        this.maxIndex = maxIndex;
        this.minIndex = minIndex;
    }

    @Override
    public Void visitAtom(LogFilterParser.AtomContext ctx) {
        if (ctx.expression() != null) {
            return visit(ctx.expression());
        } else {
            var compOp = ctx.compOp().getText();
            var isString = ctx.value().getText().contains("'");

            if (isString && (compOp.equals("<") || compOp.equals(">"))) {
                throw new NumberFormatException("< and > operations are not allowed for the string type");
            }

            var columnIndex = Integer.parseInt(ctx.column().INT().toString());

            if (columnIndex < minIndex || columnIndex > maxIndex) {
                throw new IndexOutOfBoundsException("Filter error: index " + columnIndex + " is not included in the range of acceptable indexes " + minIndex + " to " + maxIndex);
            }

            if (!indexes.contains(columnIndex)) {
                indexes.add(columnIndex);
            } else {
                throw new IndexOutOfBoundsException("Filter error: " + ctx.getText() + " has already been specified");
            }

            return null;
        }
    }
}

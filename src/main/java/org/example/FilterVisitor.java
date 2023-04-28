package org.example;

import antlr4.LogFilterBaseVisitor;
import antlr4.LogFilterParser;

import java.util.function.Predicate;

public class FilterVisitor extends LogFilterBaseVisitor<Predicate<String[]>> {
    @Override
    public Predicate<String[]> visitOrExpr(LogFilterParser.OrExprContext ctx) {
        var predicate = visit(ctx.andExpr(0));
        int count = ctx.getChildCount();
        for (int i = 1; i < count; i += 2) {
            var op = ctx.getChild(i).getText();
            var right = visit(ctx.andExpr((i + 1) / 2));
            if (op.equals("||")) {
                predicate = predicate.or(right);
            } else {
                predicate = predicate.and(right);
            }
        }
        return predicate;
    }

    @Override
    public Predicate<String[]> visitAndExpr(LogFilterParser.AndExprContext ctx) {
        var predicate = visit(ctx.atom(0));
        int count = ctx.getChildCount();
        for (int i = 1; i < count; i += 2) {
            var op = ctx.getChild(i).getText();
            var right = visit(ctx.atom((i + 1) / 2));
            if (op.equals("&")) {
                predicate = predicate.and(right);
            } else if (op.equals("||")) {
                predicate = predicate.or(right);
            }
        }
        return predicate;
    }

    @Override
    public Predicate<String[]> visitAtom(LogFilterParser.AtomContext ctx) {
        if (ctx.expression() != null) {
            return visit(ctx.expression());
        } else {
            var column = ctx.column().getText();
            var compOp = ctx.compOp().getText();
            var value = ctx.value().getText().replaceAll("'", "");

            return strArr -> {
                var columnIndex = Integer.parseInt(column.substring(7, column.length() - 1)) - 1;
                var columnValue = strArr[columnIndex].replaceAll("\"", "");
                switch (compOp) {
                    case "=":
                        return columnValue.equals(value);
                    case "<>":
                        return !columnValue.equals(value);
                    case "<":
                        return Double.parseDouble(columnValue) < Double.parseDouble(value);
                    case ">":
                        return Double.parseDouble(columnValue) > Double.parseDouble(value);
                    default:
                        return false;
                }
            };
        }
    }
}

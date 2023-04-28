package org.example;

import antlr4.LogFilterLexer;
import antlr4.LogFilterParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringsFilter {
    private Integer maxIndex = 14;
    private Integer minIndex = 1;

    public StringsFilter() {
    }

    public StringsFilter(Integer maxIndex, Integer minIndex) {
        this.maxIndex = maxIndex;
        this.minIndex = minIndex;
    }

    public List<String> filter(List<String> lines, String filter) {
        var errorListener = new ErrorListener();

        var lexer = new LogFilterLexer(CharStreams.fromString(filter));
        lexer.removeErrorListeners();

        var tokens = new CommonTokenStream(lexer);

        var parser = new LogFilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        var tree = parser.filter();

        if (parser.getNumberOfSyntaxErrors() != 0) {
            System.out.println("Invalid filter string");
            return new ArrayList<>();
        }

        try {
            new ErrorCheckerVisitor(minIndex, maxIndex).visit(tree);
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }

        var visitor = new FilterVisitor();
        var predicate = visitor.visit(tree);

        return lines.stream().filter(l -> predicate.test(l.split(","))).collect(Collectors.toList());
    }
}

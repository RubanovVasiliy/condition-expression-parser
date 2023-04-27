package org.example;

import antlr4.LogFilterLexer;
import antlr4.LogFilterParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


public class Main {
    public static void main(String[] args) {
        var filter = "column[0]>10 & (column[5]='GKA' || column[11]<>'America/Denver')";

        var input = CharStreams.fromString(filter);
        var lexer = new LogFilterLexer(input);
        var tokens = new CommonTokenStream(lexer);
        var parser = new LogFilterParser(tokens);

        var tree = parser.filter();

        var visitor = new LogFilterVisitor();
        var predicate = visitor.visit(tree);

        var lines = new String[]{
                "8854,\"Boulder Municipal Airport\",\"Boulder\",\"United States\",\"WBU\",\"KBDU\",40.0393981934,-105.225997925,5288,-7,\"A\",\"America/Denver\",\"airport\",\"OurAirports\"",
                "6248,\"Boulia Airport\",\"Boulia\",\"Australia\",\"BQL\",\"YBOU\",-22.913299560546875,139.89999389648438,542,10,\"O\",\"Australia/Brisbane\",\"airport\",\"OurAirports\""
        };

        for (var line : lines) {
            var values = line.split(",");
            if (predicate.test(values)) {
                System.out.println(line);
            }
        }
    }
}
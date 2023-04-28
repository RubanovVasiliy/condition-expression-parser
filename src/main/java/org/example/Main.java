package org.example;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        var filter = "column[1]>10 & (column[5]='GKA' || column[14]<>'OurAirports')";

        var lines = new ArrayList<String>();

        lines.add("8854,\"Boulder Municipal Airport\",\"Boulder\",\"United States\",\"WBU\",\"KBDU\",40.0393981934,-105.225997925,5288,-7,\"A\",\"America/Denver\",\"airport\",\"OurAirports\"");
        lines.add("6248,\"Boulia Airport\",\"Boulia\",\"Australia\",\"GKA\",\"YBOU\",-22.913299560546875,139.89999389648438,542,10,\"O\",\"Australia/Brisbane\",\"airport\",\"OurAirports\"");

        var filteredLines = new StringsFilter().filter(lines, filter);

        for (var line : filteredLines) {
            System.out.println(line);
        }
    }
}
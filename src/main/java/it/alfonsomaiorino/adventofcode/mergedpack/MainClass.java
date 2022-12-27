package it.alfonsomaiorino.adventofcode.mergedpack;

import it.alfonsomaiorino.adventofcode.mergedpack.service.PriorityCalculator;

import java.io.IOException;

public class MainClass {

    public static void main(String[] args) throws IOException {
        System.out.println("Total priority: " + PriorityCalculator.calculatePriorityForPack(PriorityCalculator.parseInputFile()));
    }

}

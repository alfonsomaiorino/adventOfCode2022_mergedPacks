package it.alfonsomaiorino.adventofcode.mergedpack.service;

import it.alfonsomaiorino.adventofcode.mergedpack.model.PriorityMap;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityCalculator {

    public static List<List<String>> parseInputFile() throws IOException {
        var lines = getLinesOfFile();
        var packs = new ArrayList<List<String>>();
        lines.forEach(line -> packs.add(List.of(line.substring(0, line.length() / 2), line.substring(line.length() / 2))));
        return packs;
    }

    public static List<Pair<String, Pair<String, String>>> parseInputFileForTriplet() throws IOException {
        var lines = getLinesOfFile();
        var triplets = new ArrayList<Pair<String, Pair<String, String>>>();
        var counter = new AtomicInteger();
        var tripletList = new ArrayList<String>();
        lines.forEach(line -> {
            createTripletsLineByLine(triplets, counter, line, tripletList);
        });
        return triplets;
    }

    private static void createTripletsLineByLine(ArrayList<Pair<String, Pair<String, String>>> triplets, AtomicInteger counter, String line, ArrayList<String> tripletList) {
        counter.getAndIncrement();
        tripletList.add(line);
        if(counter.get() % 3 == 0) {
            triplets.add(Pair.of(tripletList.get(0), Pair.of(tripletList.get(1), tripletList.get(2))));
            tripletList.clear();
        }
    }

    private static List<String> getLinesOfFile() throws IOException {
        var classLoader = Thread.currentThread().getContextClassLoader();
        var url = classLoader.getResource("merged_packs.txt");
        var path = Paths.get(Optional.ofNullable(url).map(u -> {
            try {
                return u.toURI();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }).orElseThrow());
        var lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines;
    }

    public static Integer calculatePriorityForPack(List<List<String>> packs) {
        var priorityResult = new AtomicInteger(0);
        packs.forEach(pack -> {
            var charListFirstPack = pack.get(0).chars().mapToObj(c -> (char) c).toList();
            var charListSecondPack = pack.get(1).chars().mapToObj(c -> (char) c).toList();
            var intersectionChar = charListFirstPack.stream()
                    .distinct()
                    .filter(charListSecondPack::contains)
                    .findAny()
                    .orElseThrow();
            priorityResult.addAndGet(PriorityMap.priorityMap.get(intersectionChar));
        });
        return priorityResult.get();
    }

    public static Integer calculatePriorityForTriplet(List<Pair<String, Pair<String, String>>> triplets) {
        var priorityResult = new AtomicInteger(0);
        triplets.forEach(triplet -> {
            var charListFirstElement = triplet.getLeft().chars().mapToObj(c -> (char) c).toList();
            var charListSecondElement = triplet.getRight().getLeft().chars().mapToObj(c -> (char) c).toList();
            var charListThirdElement = triplet.getRight().getRight().chars().mapToObj(c -> (char) c).toList();
            var intersectionChar = charListFirstElement.stream()
                    .distinct()
                    .filter(charListSecondElement::contains)
                    .filter(charListThirdElement::contains)
                    .findAny()
                    .orElseThrow();
            priorityResult.getAndAdd(PriorityMap.priorityMap.get(intersectionChar));
        });
        return priorityResult.get();
    }

}

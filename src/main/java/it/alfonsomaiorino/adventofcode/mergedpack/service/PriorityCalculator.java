package it.alfonsomaiorino.adventofcode.mergedpack.service;

import it.alfonsomaiorino.adventofcode.mergedpack.model.PriorityMap;

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

        var packs = new ArrayList<List<String>>();

        lines.forEach(line -> packs.add(List.of(line.substring(0, line.length() / 2), line.substring(line.length() / 2))));

        return packs;
    }

    public static Integer calculatePriorityForPack(List<List<String>> packs) {
        AtomicInteger priorityResult = new AtomicInteger(0);
        packs.forEach(pack -> {
            var charListFirstPack = pack.get(0).chars().mapToObj(c -> (char) c).toList();
            var charListSecondPack = pack.get(1).chars().mapToObj(c -> (char) c).toList();
            Character intersection = charListFirstPack.stream()
                    .distinct()
                    .filter(charListSecondPack::contains)
                    .findAny()
                    .orElseThrow();
            priorityResult.addAndGet(PriorityMap.priorityMap.get(intersection));
        });
        return priorityResult.get();
    }

}

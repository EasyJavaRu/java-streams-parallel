package ru.easyjava.java;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OrderedStreamMovies {
    /**
     * Regexp that extract year from IMDB's movies.list.
     */
    private static final Pattern YEAR_PATTERN =
            Pattern.compile(".*\\s+(\\d{4})$");

    private final List<String> data;

    public OrderedStreamMovies() throws IOException {
        this.data = IOUtils.readLines(SingleStreamMovies.class
                .getClassLoader()
                .getResourceAsStream("movies.list"));
    }

    public void printStreamMovies() {
        data
                .stream()
                .map(YEAR_PATTERN::matcher)
                .filter(Matcher::matches)
                .collect(
                        Collectors.groupingBy(m -> m.group(1),
                                Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + ":" + v));
    }

    public void sortStreamMovies() {
        data
                .stream()
                .map(YEAR_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1))
                .map(Integer::parseInt)
                .sorted()
                .collect(
                        Collectors.groupingBy(i -> i,
                                LinkedHashMap::new,
                                Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + ":" + v));
    }

    public Optional<Map.Entry<Integer, Long>> mostProductiveYear() {
        Comparator<Map.Entry<Integer, Long>> byValue = (entry1, entry2) -> entry1.getValue().compareTo(
                entry2.getValue());

        return data
                .stream()
                .map(YEAR_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1))
                .map(Integer::parseInt)
                .sorted()
                .collect(
                        Collectors.groupingBy(i -> i,
                                LinkedHashMap::new,
                                Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(byValue.reversed())
                .findFirst();
    }

    public void topFiveYears() {
        Comparator<Map.Entry<Integer, Long>> byValue = (entry1, entry2) -> entry1.getValue().compareTo(
                entry2.getValue());

        data
                .stream()
                .map(YEAR_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1))
                .map(Integer::parseInt)
                .sorted()
                .collect(
                        Collectors.groupingBy(i -> i,
                                LinkedHashMap::new,
                                Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(byValue.reversed())
                .limit(5)
                .forEach(e -> System.out.println(e.getKey()+":"+e.getValue()));
    }

    public void nextFiveYears() {
        Comparator<Map.Entry<Integer, Long>> byValue = (entry1, entry2) -> entry1.getValue().compareTo(
                entry2.getValue());

        data
                .stream()
                .map(YEAR_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1))
                .map(Integer::parseInt)
                .sorted()
                .collect(
                        Collectors.groupingBy(i -> i,
                                LinkedHashMap::new,
                                Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(byValue.reversed())
                .skip(5)
                .limit(5)
                .forEach(e -> System.out.println(e.getKey()+":"+e.getValue()));
    }
}

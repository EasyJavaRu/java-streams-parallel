package ru.easyjava.java;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Multi threaded movie counter.
 */
public final class ParallelStreamMovies {
    /**
     * Regexp that extract year from IMDB's movies.list.
     */
    private static final Pattern YEAR_PATTERN =
            Pattern.compile(".*\\s+(\\d{4})$");

    /**
     * Metrics registry.
     */
    static final MetricRegistry METRICS = new MetricRegistry();
    /**
     * Execution timer.
     */
    private static final Timer TIMER = METRICS.timer(
            name(SingleStreamMovies.class, "execution"));

    /**
     * Do not construct me.
     */
    private ParallelStreamMovies() { }

    /**
     * Entry point.
     * @param args Os arguments
     * @throws IOException when movies.list is unreadable
     */
    public static void main(final String[] args) throws IOException {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(METRICS)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        List<String> data = IOUtils.readLines(SingleStreamMovies.class
                .getClassLoader()
                .getResourceAsStream("movies.list"));


        final Timer.Context context = TIMER.time();
        data
                .parallelStream()
                .map(YEAR_PATTERN::matcher)
                .filter(Matcher::matches)
                .collect(
                        Collectors.groupingByConcurrent(m -> m.group(1),
                                Collectors.counting()));

        context.stop();

        SortedMap<String, Timer> timerWrapper = new TreeMap<>();
        timerWrapper.put("Execution", TIMER);
        reporter.report(
                new TreeMap<>(),
                new TreeMap<>(),
                new TreeMap<>(),
                new TreeMap<>(),
                timerWrapper);
    }
}

package ru.easyjava.java;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class OrderedStreamMoviesTest {
    private OrderedStreamMovies testedObject;

    public OrderedStreamMoviesTest() throws IOException {
        testedObject = new OrderedStreamMovies();
    }

    @Test
    public void testPrint() {
        testedObject.printStreamMovies();
    }

    @Test
    public void testSorted() {
        testedObject.sortStreamMovies();
    }

    @Test
    public void testProductiveYear() {
        Optional<Map.Entry<Integer, Long>> actual = testedObject.mostProductiveYear();
        assertTrue(actual.isPresent());
        System.out.println("Most productive year is: " + actual.get().getKey());
    }

    @Test
    public void testTopFive() {
        testedObject.topFiveYears();
    }

    @Test
    public void testNextFive() {
        testedObject.nextFiveYears();
    }
}
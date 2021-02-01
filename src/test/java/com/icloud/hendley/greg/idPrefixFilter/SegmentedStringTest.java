package com.icloud.hendley.greg.idPrefixFilter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SegmentedStringTest {

    SegmentedString segmentedString;

    @Test
    void testLengthNormal() {
        segmentedString = new SegmentedString("a.b.c.d");
        assertEquals(4, segmentedString.length());
    }

    @Test
    void testLengthEmptyString() {
        segmentedString = new SegmentedString("");
        assertEquals(1, segmentedString.length());
    }

    @Test
    void testLengthStringWithNoDelimiter() {
        segmentedString = new SegmentedString("justOneSegment");
        assertEquals(1, segmentedString.length());
    }

    @Test
    void testAdjacentDelimiters() {
        segmentedString = new SegmentedString("a--c", "-");
        String[] actual = segmentedString.getSegments();
        assertEquals("a", actual[0]);
        assertEquals("", actual[1]);
        assertEquals("c", actual[2]);
    }

    @Test
    void testGetSegmentsNormal() {
        segmentedString = new SegmentedString("a.b.c.d");
        String[] actual = segmentedString.getSegments();
        assertEquals("a", actual[0]);
        assertEquals("b", actual[1]);
        assertEquals("c", actual[2]);
        assertEquals("d", actual[3]);
    }

   @Test
    void testToStringEmpty() {
        segmentedString = new SegmentedString("a.b.c.d");
        String expected = "a.b.c.d";
        String actual = segmentedString.toString();
        assertEquals(expected, actual);
    }

    @Test
    void testToStringDot() {
        segmentedString = new SegmentedString("a.b.c.d");
        assertEquals("a.b.c.d", segmentedString.toString());
    }

    @Test
    void testToStringDash() {
        segmentedString = new SegmentedString("a-b-c-d", "-");
        assertEquals("a-b-c-d", segmentedString.toString());
    }

    @Test
    void testToStringWord() {
        segmentedString = new SegmentedString("1and2and3and4", "and");
        assertEquals("1and2and3and4", segmentedString.toString());
    }

    @Test
    void  testGetSegmentsEmptyString() {
        segmentedString = new SegmentedString("");
        String[] actual = segmentedString.getSegments();
        assertEquals("", actual[0]);
    }

    @Test
    void testGetSegmentsStringWithNoDelimiter() {
        segmentedString = new SegmentedString("justOneSegment");
        String[] actual = segmentedString.getSegments();
        assertEquals("justOneSegment", actual[0]);
    }

    @Test
    void testGetSegmentsAlternateDelimiter() {
        segmentedString = new SegmentedString("a-b-c", "-");
        String[] actual = segmentedString.getSegments();
        assertEquals("a", actual[0]);
        assertEquals("b", actual[1]);
        assertEquals("c", actual[2]);
    }

    @Test
    void testWordSegments() {
        segmentedString = new SegmentedString("one-two-three", "-");
        assertEquals(3, segmentedString.length());
        String[] segments = segmentedString.getSegments();
        assertEquals("one", segments[0]);
        assertEquals("two", segments[1]);
        assertEquals("three", segments[2]);
    }

    @Test
    void testGet() {
        segmentedString = new SegmentedString("a.b.c");
        assertEquals("a", segmentedString.get(0));
        assertEquals("b", segmentedString.get(1));
        assertEquals("c", segmentedString.get(2));
    }
}

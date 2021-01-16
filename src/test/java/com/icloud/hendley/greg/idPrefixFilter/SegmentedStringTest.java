package com.icloud.hendley.greg.idPrefixFilter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SegmentedStringTest {

    SegmentedString segmentedString;

    @Test
    void testDelimeterDefaultValue() {
        segmentedString = new SegmentedString("");
        assertEquals("\\.", segmentedString.getDelimeter());
    }

    @Test
    void testDelimeterPassedInValue() {
        segmentedString = new SegmentedString("", "-");
        assertEquals("-", segmentedString.getDelimeter());
    }

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
    void testLengthStringWithNoDelimeter() {
        segmentedString = new SegmentedString("justOneSegment");
        assertEquals(1, segmentedString.length());
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
    void  testGetSegmentsEmptyString() {
        segmentedString = new SegmentedString("");
        String[] actual = segmentedString.getSegments();
        assertEquals("", actual[0]);
    }

    @Test
    void testGetSegmentsStringWithNoDelimeter() {
        segmentedString = new SegmentedString("justOneSegment");
        String[] actual = segmentedString.getSegments();
        assertEquals("justOneSegment", actual[0]);
    }

    @Test
    void testGetSegmentsAlternateDelimeter() {
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

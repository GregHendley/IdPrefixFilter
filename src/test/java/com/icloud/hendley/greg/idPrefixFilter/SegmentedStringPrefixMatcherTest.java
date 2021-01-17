package com.icloud.hendley.greg.idPrefixFilter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SegmentedStringPrefixMatcherTest {

    SegmentedStringPrefixMatcher matcher;

    @BeforeEach
    void setup() {
        matcher = new SegmentedStringPrefixMatcher();
    }

    @AfterEach
    void teardown() {
        matcher = null;
    }

    @Test
    public void testGetPrefixesInitial() {
        List<SegmentedString> actual = matcher.getPrefixes();
        assertNotNull(actual, "Not null");
        assertTrue(actual.isEmpty(), "Empty list");
    }

    @Test
    public void testAddOnePrefix() {
        SegmentedString toAdd = new SegmentedString("a.b.c");
        matcher.add(toAdd);
        List<SegmentedString> prefixes = matcher.getPrefixes();
        List<SegmentedString> expected = new ArrayList<>();
        expected.add(toAdd);
        assertIterableEquals(expected, prefixes);
    }

    @Test
    public void testAddRedundantPrefix() {
        SegmentedString good = new SegmentedString("a.b.c");
        SegmentedString redundant1 = new SegmentedString("a.b.c.d");
        SegmentedString redundant2 = new SegmentedString("a.b.c.4");
        matcher.add(redundant1);
        matcher.add(good);
        matcher.add(redundant2);
        List<SegmentedString> expected = new ArrayList<>();
        expected.add(good);
        List<SegmentedString> actual = matcher.getPrefixes();
        assertIterableEquals(expected, actual);
    }

    @Test
    public void testMatchExact() {
        SegmentedString segmentedString = new SegmentedString("a.b.c");
        matcher.add(segmentedString);
        assertTrue(matcher.match(segmentedString));
    }

    @Test
    public void testMatchNormal() {
        SegmentedString prefix = new SegmentedString("a.b.c");
        matcher.add(prefix);
        SegmentedString segmentedString = new SegmentedString("a.b.c.d.e.f");
        assertTrue(matcher.match(segmentedString));
    }

    @Test void testMatchFailTooShort() {
        SegmentedString prefix = new SegmentedString("a.b.c");
        matcher.add(prefix);
        SegmentedString segmentedString = new SegmentedString("a.b");
        assertFalse(matcher.match(segmentedString));
    }

    @Test void testMatchFailSlightlyDifferent() {
        SegmentedString prefix = new SegmentedString("a.b.c");
        matcher.add(prefix);
        SegmentedString different = new SegmentedString("a.b.Z");
        assertFalse(matcher.match(different));
    }
}

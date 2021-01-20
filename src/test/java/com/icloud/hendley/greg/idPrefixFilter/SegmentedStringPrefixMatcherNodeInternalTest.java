package com.icloud.hendley.greg.idPrefixFilter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SegmentedStringPrefixMatcherNodeInternalTest {

    SegmentedStringPrefixMatcherNodeInternal internalNode;

    @BeforeEach
    void setup() {
        internalNode = new SegmentedStringPrefixMatcherNodeInternal();
    }

    @AfterEach
    void teardown() {
        internalNode = null;
    }

    @Test
    public void testMatchEmptyInitial() {
        SegmentedString input = new SegmentedString("");
        assertFalse(internalNode.match(input, 0));
    }

    @Test
    public void testMatchEmptyInitialIndexTooHigh() {
        SegmentedString input = new SegmentedString("");
        assertFalse(internalNode.match(input, 9));
    }

    @Test
    public void testMatchEmptyInitialIndexTooLow() {
        SegmentedString input = new SegmentedString("");
        assertFalse(internalNode.match(input, -1));
    }

    @Test
    public void testMatchNormalInputInitial() {
        SegmentedString input = new SegmentedString("a.b.c");
        assertFalse(internalNode.match(input, 0));
    }

    @Test
    public void testMatchNormalInputOnePrefixIndex0() {
        SegmentedString prefix = new SegmentedString("a.b");
        internalNode.add(prefix, 0);
        SegmentedString input = new SegmentedString("a.b.c");
        assertTrue(internalNode.match(input, 0));
    }

    @Test
    public void testMatchNormalInputOnePrefixIndex1() {
        SegmentedString prefix = new SegmentedString("a.b");
        internalNode.add(prefix, 1);
        SegmentedString input = new SegmentedString("a.b.c");
        assertTrue(internalNode.match(input, 1));
    }

    @Test
    public void testMatchNormalInputOnePrefixIndiciesMismatched() {
        SegmentedString prefix = new SegmentedString("a.b");
        internalNode.add(prefix, 0);
        SegmentedString input = new SegmentedString("a.b.c");
        assertFalse(internalNode.match(input, 1));
    }

    @Test
    public void testMatchNormalInputOnePrefixIndiciesReverseMismatched() {
        SegmentedString prefix = new SegmentedString("a.b");
        internalNode.add(prefix, 1);
        SegmentedString input = new SegmentedString("a.b.c");
        assertFalse(internalNode.match(input, 0));
    }

    @Test
    public void testMatchWithRedundantPrefixesExactCanonical() {
        addOneCanonicalAndTwoRedundantPrefixes();
        assertTrue(internalNode.match(new SegmentedString("a.b"), 0));
    }

    @Test
    public void testMatchWithRedundantPrefixesExactRedundant1() {
        addOneCanonicalAndTwoRedundantPrefixes();
        assertTrue(internalNode.match(new SegmentedString("a.b.c"), 0), "a.b.c");
    }

    @Test
    public void testMatchWithRedundantPrefixesExactRedundant2() {
        addOneCanonicalAndTwoRedundantPrefixes();
        assertTrue(internalNode.match(new SegmentedString("a.b.3"), 0), "a.b.c");
    }

    @Test
    public void testMatchWithRedundantPrefixesLongerThanCanonical() {
        addOneCanonicalAndTwoRedundantPrefixes();
        assertTrue(internalNode.match(new SegmentedString("a.b.longer"), 0), "a.b.c");
    }

    @Test
    public void testMatchWithRedundantPrefixesLongerThanAny() {
        addOneCanonicalAndTwoRedundantPrefixes();
        assertTrue(internalNode.match(new SegmentedString("a.b.c.d.e.f"), 0), "a.b.c");
    }

    @Test
    public void testMatchWithSeveralLongPrefixes() {
        internalNode.add(new SegmentedString("a.b.c.d.e.f.g.h"), 0);
        internalNode.add(new SegmentedString("a.b.c.d.e.f.g.1"), 0);
        internalNode.add(new SegmentedString("a.b.c.d.e.1.2.3"), 0);
        assertTrue(internalNode.match(new SegmentedString("a.b.c.d.e.f.g.1.2.3.4"), 0));
    }

    @Test
    public void testGetPrefixesAsListsOfStringsSimple() {
        SegmentedString prefix = new SegmentedString("a.b");
        internalNode.add(prefix, 0);
        List<String> prefixPrefix = new ArrayList<>();
        List<List<String>> actual = internalNode.getPrefixesAsListsOfStrings();
        assertNotNull(actual);
        assertEquals(1, actual.size(), "number of elements");
        List<String> actualElement = actual.get(0);
        List<String> expectedElement = Arrays.asList("a", "b");
        assertEquals(expectedElement, actualElement, "element");
    }

    @Test
    public void testGetPrefixesAsListsOfStringsRedundant() {
        addOneCanonicalAndTwoRedundantPrefixes();
        List<String> prefixPrefix = new ArrayList<>();
        List<List<String>> actual = internalNode.getPrefixesAsListsOfStrings();
        assertNotNull(actual);
        assertEquals(1, actual.size(), "number of elements");
        List<String> actualElement = actual.get(0);
        List<String> expectedElement = Arrays.asList("a", "b");
        assertEquals(expectedElement, actualElement, "element");

    }

    @Test
    public void testPrefixesPrefixedWithSimple() {
        SegmentedString prefix = new SegmentedString("a.b");
        internalNode.add(prefix, 0);
        List<String> prefixPrefix = new ArrayList<>();
        prefixPrefix.add("initial");
        List<List<String>> actual = internalNode.prefixesPrefixedWith(prefixPrefix);
        assertNotNull(actual);
        assertEquals(1, actual.size(), "number of elements");
        List<String> actualElement = actual.get(0);
        List<String> expectedElement = Arrays.asList("initial", "a", "b");
        assertEquals(expectedElement, actualElement, "element");
    }

    @Test
    public void testPrefixesPrefixedWithEmptyPrefixPrefix() {
        SegmentedString prefix = new SegmentedString("a.b");
        internalNode.add(prefix, 0);
        List<String> prefixPrefix = new ArrayList<>();
        List<List<String>> actual = internalNode.prefixesPrefixedWith(prefixPrefix);
        assertNotNull(actual);
        assertEquals(1, actual.size(), "number of elements");
        List<String> actualElement = actual.get(0);
        List<String> expectedElement = Arrays.asList("a", "b");
        assertEquals(expectedElement, actualElement, "element");
    }

    @Test
    public void testPrefixesPrefixedWithRedundant() {
        addOneCanonicalAndTwoRedundantPrefixes();
        List<String> prefixPrefix = new ArrayList<>();
        prefixPrefix.add("initial");
        List<List<String>> actual = internalNode.prefixesPrefixedWith(prefixPrefix);
        assertNotNull(actual);
        assertEquals(1, actual.size(), "number of elements");
        List<String> actualElement = actual.get(0);
        List<String> expectedElement = Arrays.asList("initial", "a", "b");
        assertEquals(expectedElement, actualElement, "element");
    }

    private void addOneCanonicalAndTwoRedundantPrefixes() {
        SegmentedString canonicalPrefix = new SegmentedString("a.b");
        SegmentedString redundantPrefix1 = new SegmentedString("a.b.c");
        SegmentedString redundantPrefix2 = new SegmentedString(("a.b.3"));
        internalNode.add(redundantPrefix1, 0);
        internalNode.add(canonicalPrefix, 0);
        internalNode.add(redundantPrefix2, 0);
    }


}

package com.icloud.hendley.greg.idPrefixFilter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SegmentedStringPrefixMatcherNodeLeafTest {

    SegmentedStringPrefixMatcherNodeLeaf leafNode;

    @BeforeEach
    void setup() {
        leafNode = SegmentedStringPrefixMatcherNodeLeaf.singleton();
    }

    @AfterEach
    void teardown() {
        leafNode = null;
    }

    @Test
    public void testMatchEmptyWithNoAdds() {
        SegmentedString input;
        input = new SegmentedString("");
        assertTrue(leafNode.match(input,0));
    }

    @Test
    public void testMatchWithNoAdds() {
        SegmentedString input;
        input = new SegmentedString("a.b.c.d");
        assertTrue(leafNode.match(input, 2));
    }

    @Test
    public void testAdd() {
        SegmentedString prefix;
        prefix = new SegmentedString("a.b.c");
        leafNode.add(prefix, 1);
        assertTrue(true);
        //no exceptions thrown with the add()
    }

    @Test
    public void testMatchWithMatchingPrefix() {
        SegmentedString prefix;
        prefix = new SegmentedString("a.b.c");
        leafNode.add(prefix, 1);
        SegmentedString input;
        input = new SegmentedString("a.b.c.d");
        assertTrue(leafNode.match(input, prefix.length()-1));
    }

    @Test
    public void testMatchWithMismatchedPrefix() {
        SegmentedString prefix;
        prefix = new SegmentedString("x.y.z");
        leafNode.add(prefix, 1);
        SegmentedString input;
        input = new SegmentedString("a.b.c.d");
        assertTrue(leafNode.match(input, prefix.length()-1));
    }

    @Test
    public void testPrefixesPrefixedWithEmpty() {
        List<List<String>> actual;
        actual = leafNode.prefixesPrefixedWith(new ArrayList<>());
        assertNotNull(actual, "Not null");
        assertEquals(1, actual.size(), "size");
        List<String> actualListOfStrings = actual.get(0);
        assertNotNull(actualListOfStrings, "Element not null");
        assertEquals(0, actualListOfStrings.size(), "Element size");
    }

    @Test
    public void testPrefixesPrefixedWithAList() {
        List<String> prefixPrefix = Arrays.asList("a", "b", "c");
        List<List<String>> actual = leafNode.prefixesPrefixedWith(prefixPrefix);
        assertNotNull(actual, "not null");
        assertEquals(1, actual.size(), "size");
        List<String> actualListOfStrings = actual.get(0);
        assertEquals(prefixPrefix, actualListOfStrings, "element");
    }

}

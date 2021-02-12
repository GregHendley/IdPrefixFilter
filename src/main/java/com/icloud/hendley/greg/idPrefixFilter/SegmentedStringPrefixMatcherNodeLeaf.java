package com.icloud.hendley.greg.idPrefixFilter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * As an implementor of SegmentedStringPrefixMatcherNode this class
 * represents both a compact representation of a set of segmented strings
 * and recursive algorithms for adding segmented strings, matching these
 * segmented strings as prefixes to input segmented strings, and for
 * answering back a form of the stored segmented strings.
 *
 * As a leaf in the compact representation the singleton instance
 * represents the end of a segmented string prefix.
 *
 * In the recursive algorithms the singleton instance implements the
 * leaf/terminal behavior of the algorithm.
 */
public class SegmentedStringPrefixMatcherNodeLeaf implements SegmentedStringPrefixMatcherNode{
    private static SegmentedStringPrefixMatcherNodeLeaf singleton;

    /**
     * Answer the single instance of SegmentedStringPrefixMatcherNodeLeaf
     * @return the single instance of SegmentedStringPrefixMatcherNodeLeaf
     */
    public static SegmentedStringPrefixMatcherNodeLeaf singleton() {
        if (singleton == null) {
            singleton = new SegmentedStringPrefixMatcherNodeLeaf();
        }
        return singleton;
    }

    /**
     * The only instance that should be accessed is the
     * singleton, which should be requested through
     * the static method singleton().
     */
    private SegmentedStringPrefixMatcherNodeLeaf() {
    }

    /**
     * The add method is called when a redundant prefix
     * is being added. Since this is the end of an
     * existing prefix that makes the new prefix redundant
     * nothing is done and the add of the redundant
     * prefix is ended.
     * @param prefix redundant prefix for which this add is attempted.
     * @param index the index into the prefix this node would represent.
     */
    @Override
    public void add(SegmentedString prefix, int index) {
        //quietly do nothing
    }

    /**
     * The method match always returns true since this object
     * represents the end of a prefix. If this object is being
     * asked for a match we know the match has already occurred.
     * @param input The segmented string being matched.
     * @param index The index in the segmented string.
     * @return true always
     */
    @Override
    public boolean match(SegmentedString input, int index) {
        return true;
    }

    /**
     * Answer the segments for the prefixes this object
     * represents. Answer the segments as lists of strings
     *
     * As a leaf node answer the initial empty list.
     *
     * @return list segments for this object's prefixes
     */
    @Override
    public List<List<String>> getPrefixesAsListsOfStrings() {
        List<List<String>> answer = new ArrayList<>();
        answer.add(new LinkedList<>());
        return answer;
    }

    /**
     * Since this prefix has no nodes and no prefix segments
     * to add, simply return the argument in a list.
     * @param prefixPrefix a list of segments (Strings) to use as a prefix
     * @return the argument in a list
     */
    public List<List<String>> prefixesPrefixedWith(List<String> prefixPrefix) {
        List<List<String>> answer = new ArrayList<>();
        answer.add(prefixPrefix);
        return answer;
    }
}

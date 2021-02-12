package com.icloud.hendley.greg.idPrefixFilter;

import java.util.List;

/**
 * Implementors of SegmentedStringPrefixMatcherNode represent
 * a compact representation of a set of segmented strings
 * and recursive algorithms for adding segmented strings,
 * matching these segmented strings as prefixes to input
 * segmented strings, and for answering back a form of the stored
 * segmented strings.
 */
interface SegmentedStringPrefixMatcherNode {

    /**
     * Add the prefix at this node beginning with index
     * @param prefix the segmented string to be added
     * @param index the index into the segmented string at which it should be added.
     */
    void add(SegmentedString prefix, int index);

    /**
     * Match the input segmented string against this and child nodes.
     * A match is found if this and each subsequent descendent has
     * a match for each subsequent index until a leaf is encountered.
     * @param input the segmented string to be matched
     * @param index the index into the segmented string where the matching
     *              should begin.
     * @return true if matching succeeds until a leaf is encountered.
     */
    boolean match(SegmentedString input, int index);

    /**
     * Answer the segments for the prefixes this object
     * represents. Answer the segments as lists of strings
     * @return list segments for this object's prefixes
     */
    List<List<String>> getPrefixesAsListsOfStrings();

    /**
     * Answer the segments for the prefixes this object represents.
     * Answer the segments as lists of strings.
     *
     * This is the original method for getting segments.
     * The method getPrefixesAsListsOfStrings() replaces this.
     *
     * @param prefixPrefix the prefix of the parent as lists of strings.
     * @return lists of segments for this object's prefixes, prefixed by prefixPrefix
     */
    List<List<String>> prefixesPrefixedWith(List<String> prefixPrefix);

    }

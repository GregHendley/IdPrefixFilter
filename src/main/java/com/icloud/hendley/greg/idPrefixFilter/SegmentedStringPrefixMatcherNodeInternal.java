package com.icloud.hendley.greg.idPrefixFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * As an implementor of SegmentedStringPrefixMatcherNode this class
 * represents both a compact representation of a set of segmented strings
 * and recursive algorithms for adding segmented strings, matching these
 * segmented strings as prefixes to input segmented strings, and for
 * answering back a form of the stored segmented strings.
 *
 * As a node in the compact representation the singleton instance
 * represents the initial segments for all prefixes by this object
 * and its children. This is especially true if this is the root node.
 *
 * In the recursive algorithms instances implement the repeating behavior
 * of the algorithm.
 *
 * Implementation details:
 * An internal node knows it's possible initial prefix segments
 * each initial prefix segment references a node that knows
 * possible subsequent prefix segments. This ends with a leaf node that
 * knows there are no possible subsequent sequence segments.
 * Given the segmented strings
 *   a.d
 *   a.e.g
 *   a.e.h
 *   a.f
 *   b
 *   c
 *
 * The structure would be
 *   {a:{d:leaf,
 *       e:{g:leaf,
 *          h:leaf},
 *       f:leaf}
 *    b:leaf,
 *    c:leaf
 *   }
 *   where {} indicates an internal node
 *   and leaf indicates a leaf node
 */
public class SegmentedStringPrefixMatcherNodeInternal implements SegmentedStringPrefixMatcherNode {

    private final TreeMap<String, SegmentedStringPrefixMatcherNode> nodes;

    public SegmentedStringPrefixMatcherNodeInternal() {
        nodes = new TreeMap<>();
    }

    /**
     * Add the prefix at this node beginning with index
     * @param prefix the segmented string to be added
     * @param index the index into the segmented string at which it should be added.
     */
    public void add(SegmentedString prefix, int index) {
        int nextIndex = index + 1;
        boolean atEnd = nextIndex >= prefix.length();
        if (atEnd) {
            nodes.put(prefix.get(index), leafNode());
        }
        else {
            SegmentedStringPrefixMatcherNode nextNode;
            nextNode = getOrCreateInternalNode(prefix.get(index));
            nextNode.add(prefix, nextIndex);
        }
    }

    /**
     * Match the input segmented string against this and child nodes.
     * A match is found if this and each subsequent descendent has
     * a match for each subsequent index until a leaf is encountered.
     * @param input the segmented string to be matched
     * @param index the index into the segmented string where the matching
     *              should begin.
     * @return true if matching succeeds until a leaf is encountered.
     */
    public boolean match(SegmentedString input, int index) {
        boolean answer;
        int nextIndex = index + 1;
        if (index < 0 || nextIndex > input.length()) {
            answer = false;
        }
        else {
            SegmentedStringPrefixMatcherNode nextNode;
            nextNode = nodes.get(input.get(index));
            if (nextNode == null) {
                answer = false;
            }
            else {
                answer = nextNode.match(input, nextIndex);
            }
        }
        return answer;
    }

    /**
     * Answer the segments for the prefixes this object
     * represents. Answer the segments as lists of strings
     *
     * @return list segments for this object's prefixes
     */
    @Override
    public List<List<String>> getPrefixesAsListsOfStrings() {
        List<List<String>> answer = new ArrayList<>();
        for (Map.Entry<String , SegmentedStringPrefixMatcherNode> entry : nodes.entrySet()) {
            for (List<String> prefixAsList : entry.getValue().getPrefixesAsListsOfStrings()) {
                prefixAsList.add(0, entry.getKey());
                answer.add(prefixAsList);
            }
        }
        return answer;
    }

    /**
     * Answer the segments for the prefixes this object represents.
     * Answer the segments as lists of strings.
     * @param prefixPrefix the prefix of the parent as lists of strings.
     * @return lists of segments for this object's prefixes, prefixed by prefixPrefix
     */
    public List<List<String>> prefixesPrefixedWith(List<String> prefixPrefix) {
        List<List<String>> answer = new ArrayList<>();
        for (Map.Entry<String, SegmentedStringPrefixMatcherNode> entry : nodes.entrySet()) {
            String localPrefixSegment = entry.getKey();
            SegmentedStringPrefixMatcherNode childNode = entry.getValue();
            ArrayList<String> extendedPrefixPrefix = new ArrayList<>(prefixPrefix);
            extendedPrefixPrefix.add(localPrefixSegment);
            answer.addAll( childNode.prefixesPrefixedWith(extendedPrefixPrefix) );
        }
        return answer;
    }

    /**
     * Return the object used as the leaf node.
     * The leaf node indicates the end of a prefix to match.
     * This is a convenience method that makes code
     * easier to read and understand.
     * @return the singleton instance of SegmentedStringPrefixMatcherNodeLeaf
     */
    private SegmentedStringPrefixMatcherNodeLeaf leafNode() {
        return SegmentedStringPrefixMatcherNodeLeaf.singleton();
    }

    /**
     * If nodes does not contain the key "segment" then create
     * an internal node with key "segment".
     * In any case return the node (internal or leaf) at "segment".
     *
     * @param segment the key for a node that may contain subsequent segments
     * @return the node that may contain segments that come after "segment"
     */
    private SegmentedStringPrefixMatcherNode getOrCreateInternalNode(String segment) {
        SegmentedStringPrefixMatcherNode answer;
        if (nodes.containsKey(segment)) {
            answer = nodes.get(segment);
        }
        else {
            answer = new SegmentedStringPrefixMatcherNodeInternal();
            nodes.put(segment, answer);
        }
        return answer;
    }
}

package com.icloud.hendley.greg.idPrefixFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SegmentedStringPrefixMatcherNodeInternal implements SegmentedStringPrefixMatcherNode {

    private TreeMap<String, SegmentedStringPrefixMatcherNode> nodes;

    public SegmentedStringPrefixMatcherNodeInternal() {
        nodes = new TreeMap<>();
    }

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
    
    public List<List<String>> prefixesPrefixedWith(List<String> prefixPrefix) {
        List<List<String>> answer = new ArrayList<>();
        for (Map.Entry<String, SegmentedStringPrefixMatcherNode> entry : nodes.entrySet()
             ) {
            String localPrefixSegment = entry.getKey();
            SegmentedStringPrefixMatcherNode childNode = entry.getValue();
            ArrayList<String> extendedPrefixPrefix = new ArrayList<>(prefixPrefix);
            extendedPrefixPrefix.add(localPrefixSegment);
            answer.addAll( childNode.prefixesPrefixedWith(extendedPrefixPrefix) );
        }
        return answer;
    }

    private SegmentedStringPrefixMatcherNodeLeaf leafNode() {
        return SegmentedStringPrefixMatcherNodeLeaf.singleton();
    }

    private SegmentedStringPrefixMatcherNode getOrCreateInternalNode(String s) {
        SegmentedStringPrefixMatcherNode answer;
        if (nodes.containsKey(s)) {
            answer = nodes.get(s);
        }
        else {
            answer = new SegmentedStringPrefixMatcherNodeInternal();
            nodes.put(s, answer);
        }
        return answer;
    }
}

package com.icloud.hendley.greg.idPrefixFilter;

import java.util.ArrayList;
import java.util.List;

public class SegmentedStringPrefixMatcherNodeLeaf implements SegmentedStringPrefixMatcherNode{
    private static SegmentedStringPrefixMatcherNodeLeaf singleton;

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

    @Override
    public void add(SegmentedString prefix, int index) {
        //quietly do nothing
    }

    @Override
    public boolean match(SegmentedString input, int index) {
        return true;
    }

    public List<List<String>> prefixesPrefixedWith(List<String> prefixPrefix) {
        List<List<String>> answer = new ArrayList<>();
        answer.add(prefixPrefix);
        return answer;
    }
}

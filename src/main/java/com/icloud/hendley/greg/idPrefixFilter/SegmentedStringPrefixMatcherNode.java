package com.icloud.hendley.greg.idPrefixFilter;

import java.util.List;
 interface SegmentedStringPrefixMatcherNode {

    void add(SegmentedString prefix, int index);

    boolean match(SegmentedString input, int index);

    List<List<String>> prefixesPrefixedWith(List<String> prefixPrefix);

    }

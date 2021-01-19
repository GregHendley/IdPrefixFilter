package com.icloud.hendley.greg.idPrefixFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * SegmentedStringPrefixMatcher holds a collection
 * of prefixes of instances of SegmentedString
 * and answers if an input SegmentedString
 * matches any in the collection.
 *
 * To use, create an instance and add prefixes to match
 * as segmented strings. These are your patterns (without wild cards).
 * Then, you can match against other segmented strings.
 *
 * You can also ask for the prefixes from an instance.
 * A canonical version is returned, without redundant prefixes.
 * For example:
 *    given {"a" , "b", "c"}
 *    and {"a" , "b"}
 *    only {"a", "b"} will be returned since anything that
 *    matches {"a" , "b" , "c"} will also match {"a" , "b" }
 */
public class SegmentedStringPrefixMatcher {
    SegmentedStringPrefixMatcherNodeInternal root;

    public SegmentedStringPrefixMatcher() {
        root = new SegmentedStringPrefixMatcherNodeInternal();
    }

    /**
     * Add the prefix as one to match against
     * @param prefix a SegmentedString to match as a prefix.
     */
    public void add(SegmentedString prefix) {
        root.add(prefix, 0);
    }

    /**
     * Answer true if the toMatch SegmentedString matches
     * any of this object's prefixes.
     *
     * @param toMatch the segmented string in question
     * @return boolean, true if the segmented string to match
     * matches any of this object's prefixes.
     */
    public boolean match(SegmentedString toMatch) {
        return root.match(toMatch, 0);
    }

    /**
     * Answer a canonical list of SegmentedString
     * against which this object matches.
     *
     * @return list of SegmentedString prefixes
     */
    public List<SegmentedString> getPrefixes() {
        List<SegmentedString> answer = new ArrayList<>();
        List<List<String>> prefixesAsListsOfStrings;
        prefixesAsListsOfStrings = root.prefixesPrefixedWith(new ArrayList<>());
        String[] emptyStringArray = new String[]{};
        for (List<String> prefixAsListOfStrings : prefixesAsListsOfStrings) {
            String[] prefixAsArrayOfStrings = prefixAsListOfStrings.toArray(emptyStringArray);
            answer.add(new SegmentedString(prefixAsArrayOfStrings));
        }
        return answer;
    }

}

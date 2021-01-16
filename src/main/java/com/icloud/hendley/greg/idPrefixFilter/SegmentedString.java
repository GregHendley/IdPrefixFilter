package com.icloud.hendley.greg.idPrefixFilter;

import java.util.Arrays;

/**
 * The class SegmentedString represents a delimeted string such as
 *   1.2.3.4
 *   one*two*three*four
 *   100->200->300->4
 * The delimeter is usually a single character
 * but can be any string.
 * This class makes it convenient to access segments
 * and still answer the segmented string as a string.
 * An enhancement would be to add comparisons.
 */
public class SegmentedString {
    private String delimeter = "\\.";
    private String[] segments;

    /**
     * Constructs an array list with the segments in the array
     * @param segments array of segment strings
     */
    public SegmentedString(String[] segments) {
        this.segments = segments;
    }

    public SegmentedString(String string) {
        segments = string.split(delimeter);
    }

    public SegmentedString(String string, String delimeter) {
        this.delimeter = delimeter;
        this.segments = string.split(this.delimeter);
    }

    public String toString() {
        return String.join((CharSequence) Arrays.asList(segments), delimeter);
    }

    public String get(int index) {
        return segments[index];
    }

    public int length() {
        return segments.length;
    }

    public String getDelimeter() {
        return delimeter;
    }

    public String[] getSegments() {
        return segments;
    }
}

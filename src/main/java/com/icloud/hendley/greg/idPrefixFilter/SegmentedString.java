package com.icloud.hendley.greg.idPrefixFilter;

import java.util.Arrays;
import java.util.Objects;

/**
 * The class SegmentedString represents a delimeted string such as
 *   1.2.3.4
 *   one-two-three-four
 *   100and200and300and400
 * The delimeter is usually a single character
 * but can be any string.
 * This class makes it convenient to access segments
 * and still answer the segmented string as a string.
 * An enhancement would be to add comparisons.
 */
public class SegmentedString {
    private static final String dot = "\\.";
    private static final String dotForPrinting = ".";
    private String delimeter = dot;
    private String delimeterForPrinting = dotForPrinting;
    private final String[] segments;

    /**
     * Constructs a segmented string with the segments in the array
     * @param segments array of segment strings
     */
    public SegmentedString(String[] segments) {
        this.segments = segments;
    }

    /**
     * Constructs a segmented string from string assuming dots(.) separate segments
     * @param string a string representation of the segmented string to be created.
     */
    public SegmentedString(String string) {
        segments = string.split(delimeter);
    }

    /**
     * Constructs a segmented string from string using delimiter to separate segments.
     * @param string The string representation of a segmented string
     * @param delimiter the delimiter separating segments of the string
     */
    public SegmentedString(String string, String delimiter) {
        this.delimeter = delimiter;
        delimeterForPrinting = delimiter.equals(dot) ? dotForPrinting : delimiter;
        this.segments = string.split(this.delimeter);
    }

    /**
     * Return a string representation of the segmented string using delimiter
     * to separate the segments.
     * @return string representation of the segmented string.
     */
    public String toString() {
        return String.join(delimeterForPrinting, Arrays.asList(getSegments()));
    }

    /**
     * Return the segment at index
     * @param index index of the desired segment
     * @return the segment at index
     */
    public String get(int index) {
        return segments[index];
    }

    /**
     * Return the number of segments.
     * @return the number of segments.
     */
    public int length() {
        return segments.length;
    }

    /**
     * Return an array of the segments
     * @return an array of the segments.
     */
    public String[] getSegments() {
        return segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SegmentedString that = (SegmentedString) o;
        return Objects.equals(delimeter, that.delimeter) && Arrays.equals(segments, that.segments);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(delimeter);
        result = 31 * result + Arrays.hashCode(segments);
        return result;
    }
}

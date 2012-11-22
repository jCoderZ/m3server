package org.jcoderz.m3server.protocol.http;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A RangeSet represents a set of ranges, requested by a client.
 *
 * @see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.16
 * @see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35
 *
 * @author mrumpf
 */
public class RangeSet {

    private List<Range> ranges = new ArrayList<>();
    private long size;

    /**
     * Constructor-
     *
     * @param size
     * @param rangeset
     */
    public RangeSet(long size, String rangeset) {
        this.size = size;
        // skip the "bytes=" prefix
        int idx = rangeset.indexOf('=');
        String r = rangeset;
        if (idx != -1) {
            r = rangeset.substring(idx + 1);
        }
        StringTokenizer strtok = new StringTokenizer(r, ",");
        while (strtok.hasMoreTokens()) {
            String tok = strtok.nextToken();
            Range re = new Range(size, tok);
            ranges.add(re);
        }
    }

    /**
     * Returns the number of ranges.
     *
     * @return the range count
     */
    public int getRangeCount() {
        return ranges.size();
    }

    /**
     * Returns the range of the given index.
     *
     * @param index the index of the range
     * @return the range identified by the index
     */
    public Range getRange(int index) {
        return ranges.get(index);
    }

    /**
     * Represents a range START-END.
     */
    public static class Range {

        private long first;
        private long last;

        /**
         * Constructor.
         *
         * @param size the total size
         * @param value the string that represents the range
         */
        public Range(long size, String value) {
            StringTokenizer strtok = new StringTokenizer(value, "-");
            if (strtok.countTokens() == 2) {
                first = parseToken(strtok.nextToken());
                last = parseToken(strtok.nextToken());
                if (last >= size) {
                    last = size - 1;
                }
            } else if (strtok.countTokens() == 1) {
                if (value.startsWith("-")) {
                    first = size - parseToken(strtok.nextToken()) - 1;
                    last = size - 1;
                } else {
                    first = parseToken(strtok.nextToken());
                    last = size - 1;
                }
            } else {
                throw new RangeRuntimeException("first and last must not be zero");
            }
            if (last <= first) {
                throw new RangeRuntimeException("first must not be greater than last");
            }
        }

        /**
         * Returns the first position.
         *
         * @return the first position
         */
        public long getFirstPos() {
            return first;
        }

        /**
         * Returns the last position.
         *
         * @return the last position
         */
        public long getLastPos() {
            return last;
        }

        private Long parseToken(String tok) throws NumberFormatException {
            long result = 0L;
            if (tok != null && !tok.isEmpty()) {
                result = Long.valueOf(tok);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getName());
            sb.append('@');
            sb.append(Integer.toHexString(hashCode()));
            sb.append("[first=");
            sb.append(first);
            sb.append(", last=");
            sb.append(last);
            sb.append(']');
            return sb.toString();
        }
    }
}

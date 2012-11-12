package org.jcoderz.m3server.protocol.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mrumpf
 */
public class Range {

    private static final String RANGE_PATTERN = "bytes=([0-9]?-[0-9]?)(,[0-9]?-[0-9]?)*";
    private String value;
    private Matcher m;

    public Range(String value) {
        this.value = value;
        Pattern p = Pattern.compile(RANGE_PATTERN);
        m = p.matcher(value);
    }

    public String getGroup(int idx) {
        return m.group(idx);
    }

    public int getGroupCount() {
        return m.groupCount();
    }
}

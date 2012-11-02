package org.jcoderz.m3server.util;

import java.util.concurrent.TimeUnit;

/**
 * Utilities for dealing with time values.
 *
 * @author mrumpf
 */
public class TimeUtil {

    private TimeUtil() {
        // do not allow instances
    }

    /**
     * Converts the milli-seconds long into a string.
     *
     * @param millis the milli-seconds to convert
     * @return a string representation of the long value
     */
    public static String convertMillis(long millis) {
        String result;
        if (TimeUnit.MILLISECONDS.toHours(millis) != 0L) {
            result = String.format("%d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        } else {
            result = String.format("%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }
        return result;
    }
}

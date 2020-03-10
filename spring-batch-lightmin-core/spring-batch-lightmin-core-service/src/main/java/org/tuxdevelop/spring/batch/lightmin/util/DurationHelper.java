package org.tuxdevelop.spring.batch.lightmin.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Marcel Becker
 * @since 0.1
 * <p>
 * Utility class to generate human readable durations
 * </p>
 */
@Slf4j
public final class DurationHelper {

    private DurationHelper() {
    }

    private static final long MILLIS_UPPER_BOUND = 1000;
    private static final long SECONDS_UPPER_BOUND = 60000;
    private static final long MINUTES_UPPER_BOUND = 3600000;

    private static final String MILLIS_FORMAT = "SSS";
    private static final String SECONDS_MILLIS_FORMAT = "ss:SSS";
    private static final String MINUTES_SECONDS_MILLIS_FORMAT = "mm:ss:SSS";
    private static final String HOURS_MINUTES_SECONDS_MILLIS_FORMAT = "hh:mm:ss:SSS";

    private static final String MILLIS_UNIT = " ms";
    private static final String SECONDS_UNIT = " sec";
    private static final String MINUTES_UNIT = " min";
    private static final String HOURES_UNIT = " h";

    /**
     * Creates a human readable String of duration between a start date and an end date
     *
     * @param startTime beginning of the duration interval
     * @param endTime   end of the duration interval
     * @return a {@link String} representation of the duration
     */
    public static String createDurationValue(Date startTime, Date endTime) {
        final Date current = new Date();
        if (startTime == null) {
            startTime = current;
            log.info("startTime was null, set to current date");
        }
        if (endTime == null) {
            endTime = current;
            log.info("endTime was null, set to current date");
        }
        final Long duration = endTime.getTime() - startTime.getTime() < 0 ? 0 : endTime.getTime() - startTime.getTime();

        return format(new Date(duration));
    }

    private static String format(final Date date) {
        final long duration = date.getTime();
        final String unit;
        final SimpleDateFormat simpleDateFormat;
        if (duration < MILLIS_UPPER_BOUND) {
            simpleDateFormat = new SimpleDateFormat(MILLIS_FORMAT);
            unit = MILLIS_UNIT;
        } else if (duration < SECONDS_UPPER_BOUND) {
            simpleDateFormat = new SimpleDateFormat(SECONDS_MILLIS_FORMAT);
            unit = SECONDS_UNIT;
        } else if (duration < MINUTES_UPPER_BOUND) {
            simpleDateFormat = new SimpleDateFormat(MINUTES_SECONDS_MILLIS_FORMAT);
            unit = MINUTES_UNIT;
        } else {
            simpleDateFormat = new SimpleDateFormat(HOURS_MINUTES_SECONDS_MILLIS_FORMAT);
            unit = HOURES_UNIT;
        }
        return simpleDateFormat.format(date) + unit;
    }
}

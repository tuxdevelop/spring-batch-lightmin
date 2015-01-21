package org.tuxdevelop.spring.batch.lightmin.util;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DurationHelper {

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
		final Double duration = new Double(endTime.getTime() - startTime.getTime());
		final String durationString;
		if (duration > 1000) {
			durationString = (duration / 1000) + " seconds";
		} else {
			durationString = duration + " millis";
		}
		return durationString;
	}

}

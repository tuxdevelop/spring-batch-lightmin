package org.tuxdevelop.spring.batch.lightmin.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class LightminMetricUtils {

    private static final String LIGHTMIN_BASE = "lightmin_";
    private static final String LIGHTMIN_CLIENT = "client_";
    private static final String LIGHTMIN_SERVER = "server_";
    private static final String LIGHTMIN_STEP_DATA_BASE = "step_execution_data_item_";

    @Getter
    public enum LightminMetrics {

        LIGHTMIN_JOB_STATUS("job_status"),

        LIGHTMIN_STEP_DATA_WRITE(LIGHTMIN_STEP_DATA_BASE + "write"),

        LIGHTMIN_STEP_DATA_COMMIT(LIGHTMIN_STEP_DATA_BASE + "commit"),

        LIGHTMIN_STEP_DATA_READ(LIGHTMIN_STEP_DATA_BASE + "read"),

        LIGHTMIN_STEP_DATA_ROLLBACK(LIGHTMIN_STEP_DATA_BASE + "rollback");


        String lightminMetricName;

        LightminMetrics(final String lightminMetricName) {
            this.lightminMetricName = lightminMetricName;
        }
    }

    private LightminMetricUtils() {
    }

    public static String getMetricName(LightminMetricSource source, LightminMetrics lightminMetricName) {
        if (!Arrays.asList(LightminMetrics.values()).contains(lightminMetricName)) {
            log.info(lightminMetricName.getLightminMetricName() + " is no METRIC known by Lightmin context with this name.");
        } else {
            switch (source) {
                case CLIENT:
                    return LIGHTMIN_BASE + LIGHTMIN_CLIENT + lightminMetricName.getLightminMetricName();
                case SERVER:
                    return LIGHTMIN_BASE + LIGHTMIN_SERVER + lightminMetricName.getLightminMetricName();
                default:
                    log.info(source.name() + " is no SOURCE known by Lightmin context with this name.");
            }
        }
        return null;
    }
}

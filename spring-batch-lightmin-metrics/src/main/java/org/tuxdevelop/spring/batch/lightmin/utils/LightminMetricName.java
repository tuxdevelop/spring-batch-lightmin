package org.tuxdevelop.spring.batch.lightmin.utils;

public class LightminMetricName {

    private static final String LIGHTMIN_BASE = "lightmin_";
    private static final String LIGHTMIN_STEP_DATA_BASE = LIGHTMIN_BASE + "step_execution_data_item_";

    public static final String LIGHTMIN_JOB_STATUS = LIGHTMIN_BASE + "job_status";

    public static final String LIGHTMIN_STEP_DATA_WRITE = LIGHTMIN_STEP_DATA_BASE + "write";
    public static final String LIGHTMIN_STEP_DATA_COMMIT = LIGHTMIN_STEP_DATA_BASE + "commit";
    public static final String LIGHTMIN_STEP_DATA_READ = LIGHTMIN_STEP_DATA_BASE + "read";
    public static final String LIGHTMIN_STEP_DATA_ROLLBACK = LIGHTMIN_STEP_DATA_BASE + "rollback";

    private LightminMetricName(){}
}

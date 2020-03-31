package org.tuxdevelop.spring.batch.lightmin.utils;

import lombok.Getter;
import org.springframework.batch.core.ExitStatus;

import java.util.Arrays;

public enum LightminExitStatus {

    UNKNOWN(ExitStatus.UNKNOWN.getExitCode(), 0),
    EXECUTING(ExitStatus.EXECUTING.getExitCode(), 1),
    COMPLETED(ExitStatus.COMPLETED.getExitCode(), 2),
    NOOP(ExitStatus.NOOP.getExitCode(), 3),
    FAILED(ExitStatus.FAILED.getExitCode(), 4),
    STOPPED(ExitStatus.STOPPED.getExitCode(), 5);

    @Getter
    private final String exitCode;
    @Getter
    private final int metricExitId;

    LightminExitStatus(final String exitCode, final int metricExitId) {
        this.exitCode = exitCode;
        this.metricExitId = metricExitId;
    }

    public static int getLightminMetricExitIdByExitStatus(final String exitCode) {
        return Arrays.stream(values()).filter(les -> les.getExitCode().equals(exitCode))
                .map(LightminExitStatus::getMetricExitId)
                .findFirst()
                .orElse(UNKNOWN.getMetricExitId());
    }

}

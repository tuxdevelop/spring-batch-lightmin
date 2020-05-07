package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import java.io.Serializable;

public final class ExecutionStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionStatus() {
    }

    public static final Integer NEW = 1;
    public static final Integer RUNNING = 10;
    public static final Integer FINISHED = 20;
    public static final Integer FAILED = 30;
    public static final Integer LOST = 40;
}

package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

public final class ExecutionStatus {

    private ExecutionStatus() {
    }

    public static final Integer NEW = 1;
    public static final Integer RUNNING = 10;
    public static final Integer FINISHED = 20;
    public static final Integer FAILED = 30;
    public static final Integer LOST = 40;
}

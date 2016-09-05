package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;


/**
 * @author Marcel Becker
 * @Since 0.3
 */
public enum BatchStatus {

    COMPLETED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    FAILED,
    ABANDONED,
    UNKNOWN;

}

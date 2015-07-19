package org.tuxdevelop.spring.batch.lightmin.admin.domain;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

@Slf4j
public abstract class AbstractConfiguration {

    protected void throwExceptionAndLogError(final String message) {
        log.error(message);
        throw new SpringBatchLightminApplicationException(message);
    }
}

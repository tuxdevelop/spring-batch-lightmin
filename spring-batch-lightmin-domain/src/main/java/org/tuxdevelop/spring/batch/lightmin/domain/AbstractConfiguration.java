package org.tuxdevelop.spring.batch.lightmin.domain;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
abstract class AbstractConfiguration {

    void throwExceptionAndLogError(final String message) {
        log.error(message);
        throw new SpringBatchLightminApplicationException(message);
    }
}

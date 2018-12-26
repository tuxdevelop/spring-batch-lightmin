package org.tuxdevelop.spring.batch.lightmin.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public final class ResponseUtil {

    private ResponseUtil() {
    }

    public static void checkHttpOk(final ResponseEntity<?> responseEntity) {
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + responseEntity.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        } else {
            log.debug("Response {} ok", responseEntity);
        }
    }
}

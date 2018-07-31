package org.tuxdevelop.spring.batch.lightmin.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public final class RequestUtil {

    private RequestUtil() {
    }

    public static <T> HttpEntity<T> createApplicationJsonEntity(final T body) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        return new HttpEntity<>(body, HttpHeaders.readOnlyHttpHeaders(headers));
    }
}

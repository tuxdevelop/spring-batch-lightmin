package org.tuxdevelop.spring.batch.lightmin.util;

import com.fasterxml.jackson.core.Base64Variants;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class BasicAuthHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final String encodedAuth;

    public BasicAuthHttpRequestInterceptor(final String username,
                                           final String password) {
        final String authentication = username + ":" + password;
        encodedAuth = "Basic " + Base64Variants.MIME_NO_LINEFEEDS.encode(authentication.getBytes(StandardCharsets.US_ASCII));
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request,
                                        final byte[] body,
                                        final ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", encodedAuth);
        return execution.execute(request, body);
    }

}

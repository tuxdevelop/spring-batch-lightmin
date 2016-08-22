package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "spring.batch.lightmin.server")
public class LightminServerProperties {

    @Getter
    private String contextPath = "";

    public void setContextPath(final String pathPrefix) {
        if (!pathPrefix.startsWith("/") || pathPrefix.endsWith("/")) {
            throw new IllegalArgumentException(
                    "ContextPath must start with '/' and not end with '/'");
        }
        this.contextPath = pathPrefix;
    }
}

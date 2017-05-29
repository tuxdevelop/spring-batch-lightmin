package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@ConfigurationProperties(value = "spring.batch.lightmin.server")
public class LightminServerProperties {

    @Getter
    private String contextPath = "";

    @Getter
    @Setter
    private Long heartbeatPeriod = 10000L;

    @Getter
    @Setter
    private String clientUserName;
    @Getter
    @Setter
    private String clientPassword;
    @Getter
    @Setter
    private Integer errorEventRepositorySize = 200;

    public void setContextPath(final String pathPrefix) {
        if (!pathPrefix.startsWith("/") || pathPrefix.endsWith("/")) {
            throw new IllegalArgumentException(
                    "ContextPath must start with '/' and not end with '/'");
        }
        this.contextPath = pathPrefix;
    }
}

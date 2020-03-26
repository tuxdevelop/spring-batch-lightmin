package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@ConfigurationProperties(value = "spring.batch.lightmin.server")
public class LightminServerCoreProperties {

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
    private Integer eventRepositorySize = 1000;

    @Getter
    @Setter
    private Boolean metricsEnabled = Boolean.FALSE;

    @Getter
    @Setter
    private Boolean discoveryEnabled = Boolean.FALSE;

    @Getter
    @Setter
    private Boolean useXForwardedHeaders = Boolean.FALSE;

    public void setContextPath(final String pathPrefix) {
        if (!pathPrefix.startsWith("/") || pathPrefix.endsWith("/")) {
            throw new IllegalArgumentException(
                    "ContextPath must start with '/' and not end with '/'");
        }
        this.contextPath = pathPrefix;
    }
}

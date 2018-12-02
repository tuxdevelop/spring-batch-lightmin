package org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.client.discovery")
public class LightminClientDiscoveryProperties {

    private String serverDiscoveryName = "lightmin-server";
    private String serverContextPath;
}

package org.tuxdevelop.spring.batch.lightmin.server.fe.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.server.fe")
public class LightminServerFeConfigurationProperties {

    private String label;
    private String imgPath;

}

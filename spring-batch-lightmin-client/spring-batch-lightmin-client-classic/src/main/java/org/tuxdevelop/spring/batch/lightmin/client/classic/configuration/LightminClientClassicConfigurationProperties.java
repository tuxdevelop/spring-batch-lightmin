package org.tuxdevelop.spring.batch.lightmin.client.classic.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.client.classic")
public class LightminClientClassicConfigurationProperties {

    private boolean autoDeregistration = Boolean.TRUE;
    private boolean autoRegistration = Boolean.TRUE;
    private boolean registerOnce = Boolean.FALSE;
    private Long period = 10000L;

    @NestedConfigurationProperty
    private LightminClientServerProperties server = new LightminClientServerProperties();

    @Data
    public static class LightminClientServerProperties {

        private String[] url;
        private String apiApplicationsPath = "api/applications";

        public String[] getLightminUrl() {
            final String[] adminUrls;
            if (this.url != null) {
                adminUrls = this.url.clone();
            } else {
                adminUrls = new String[]{};
            }
            return adminUrls;
        }
    }

}

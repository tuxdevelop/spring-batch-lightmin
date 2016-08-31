package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.server")
public class LightminProperties {

    private String[] url;
    private String apiPath = "api/applications";
    private Long period = 10000L;
    private String username;
    private String password;
    private boolean autoDeregistration;
    private boolean autoRegistration = true;
    private boolean registerOnce = true;

    public String[] getLightminUrl() {
        final String[] adminUrls = url.clone();
        for (int i = 0; i < adminUrls.length; i++) {
            adminUrls[i] += "/" + apiPath;
        }
        return adminUrls;
    }
}

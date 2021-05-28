package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.RemoteAdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.RemoteJobServerService;

@Configuration
public class BaseRemoteStandaloneConfiguration extends BaseStandaloneConfiguration{
    @Bean
    public AdminServerService adminServerService(@Qualifier("clientRestTemplate") final RestTemplate restTemplate) {
        return new RemoteAdminServerService(restTemplate);
    }

    @Bean
    public JobServerService jobServerService(@Qualifier("clientRestTemplate") final RestTemplate restTemplate) {
        return new RemoteJobServerService(restTemplate);
    }
}

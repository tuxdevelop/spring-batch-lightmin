package org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class ConsulMetaDataExtender implements MetaDataExtender {

    private final ConsulDiscoveryProperties consulDiscoveryProperties;

    public ConsulMetaDataExtender(final ConsulDiscoveryProperties consulDiscoveryProperties) {
        this.consulDiscoveryProperties = consulDiscoveryProperties;
    }

    @Override
    public void extendMetaData() {
        final String consulTag = MetaDataExtender.LIGHTMIN_CLIENT_META_DATA_KEY + "=" + LIGHTMIN_CLIENT_META_DATA_VALUE;
        if (this.consulDiscoveryProperties.getTags() != null) {
            log.debug("consul meta tag list already initialized");
        } else {
            final List<String> metaTags = new ArrayList<>();
            this.consulDiscoveryProperties.setTags(metaTags);
        }
        this.consulDiscoveryProperties.getTags().add(consulTag);
    }
}

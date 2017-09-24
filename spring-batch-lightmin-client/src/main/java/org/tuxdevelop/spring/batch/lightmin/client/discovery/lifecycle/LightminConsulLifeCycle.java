package org.tuxdevelop.spring.batch.lightmin.client.discovery.lifecycle;

import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;

public class LightminConsulLifeCycle extends ConsulAutoServiceRegistration {

    private final MetaDataExtender metaDataExtender;

    public LightminConsulLifeCycle(final ConsulServiceRegistry serviceRegistry,
                                   final ConsulDiscoveryProperties properties,
                                   final ConsulAutoRegistration registration,
                                   final MetaDataExtender metaDataExtender) {

        super(serviceRegistry, properties, registration);
        this.metaDataExtender = metaDataExtender;
    }

    @Override
    protected void register() {
        this.metaDataExtender.extendMetaData();
        super.register();
    }
}

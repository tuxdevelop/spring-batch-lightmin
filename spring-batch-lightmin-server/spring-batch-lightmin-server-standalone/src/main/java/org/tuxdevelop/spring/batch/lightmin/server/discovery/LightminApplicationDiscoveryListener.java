package org.tuxdevelop.spring.batch.lightmin.server.discovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.discovery.event.ParentHeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.discovery.DiscoveryBase;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.List;
import java.util.Map;

@Slf4j
public class LightminApplicationDiscoveryListener {

    private final DiscoveryClient discoveryClient;
    private final DiscoveryRegistrationBean discoveryRegistrationBean;
    private final HeartbeatMonitor heartbeatMonitor;

    public LightminApplicationDiscoveryListener(final DiscoveryClient discoveryClient,
                                                final DiscoveryRegistrationBean discoveryRegistrationBean,
                                                final HeartbeatMonitor heartbeatMonitor) {
        this.discoveryClient = discoveryClient;
        this.discoveryRegistrationBean = discoveryRegistrationBean;
        this.heartbeatMonitor = heartbeatMonitor;
    }

    @EventListener
    public void onInstanceRegistered(final InstanceRegisteredEvent<?> event) {
        this.discover();
    }

    @EventListener
    public void onApplicationReady(final ApplicationReadyEvent event) {
        this.discover();
    }

    @EventListener
    public void onParentHeartbeat(final ParentHeartbeatEvent event) {
        this.discoverIfNeeded(event.getValue());
    }

    @EventListener
    public void onHeartbeat(final HeartbeatEvent event) {
        this.discoverIfNeeded(event.getValue());
    }

    public void discover() {
        final List<String> serviceIds = this.discoveryClient.getServices();
        for (final String serviceId : serviceIds) {
            final List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(serviceId);
            this.register(serviceInstances);
        }
    }

    private void discoverIfNeeded(final Object value) {
        if (this.heartbeatMonitor.update(value)) {
            this.discover();
        }
    }

    private void register(final List<ServiceInstance> serviceInstances) {
        serviceInstances
                .parallelStream()
                .forEach(
                        this::register
                );
    }

    private void register(final ServiceInstance serviceInstance) {
        if (this.checkIsLightminInstance(serviceInstance)) {
            try {
                this.discoveryRegistrationBean.register(serviceInstance);
            } catch (final SpringBatchLightminApplicationException ex) {
                log.error("Error while register {}", serviceInstance, ex);
            }
        } else {
            log.info("Skipping {}, no Spring Batch Lightmin Instance");
        }
    }

    private Boolean checkIsLightminInstance(final ServiceInstance serviceInstance) {
        final Boolean result;
        final Map<String, String> metaData = serviceInstance.getMetadata();
        if (metaData.containsKey(DiscoveryBase.LIGHTMIN_CLIENT_META_DATA_KEY)) {
            final String value = metaData.get(DiscoveryBase.LIGHTMIN_CLIENT_META_DATA_KEY);
            if (DiscoveryBase.LIGHTMIN_CLIENT_META_DATA_VALUE.equals(value)) {
                result = Boolean.TRUE;
            } else {
                result = Boolean.FALSE;
            }
        } else {
            result = Boolean.FALSE;
        }
        return result;
    }
}

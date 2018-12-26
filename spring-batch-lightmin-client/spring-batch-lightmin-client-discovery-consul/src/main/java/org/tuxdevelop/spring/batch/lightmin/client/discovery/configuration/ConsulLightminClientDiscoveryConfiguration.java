package org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation.EnableLightminClientDiscoveryCore;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.ConsulMetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;

import java.util.List;

@Configuration
@ConditionalOnConsulEnabled
@EnableLightminClientDiscoveryCore
@ConditionalOnClass(ConsulDiscoveryClient.class)
public class ConsulLightminClientDiscoveryConfiguration {

    @Bean
    public MetaDataExtender metaDataExtender(final ConsulDiscoveryProperties consulDiscoveryProperties) {
        return new ConsulMetaDataExtender(consulDiscoveryProperties);
    }

    @Bean
    @ConditionalOnMissingBean(ConsulAutoRegistration.class)
    public ConsulAutoRegistration consulAutoRegistration(
            final MetaDataExtender metaDataExtender,
            final AutoServiceRegistrationProperties autoServiceRegistrationProperties,
            final ConsulDiscoveryProperties consulDiscoveryProperties,
            final ApplicationContext applicationContext,
            final List<ConsulRegistrationCustomizer> consulRegistrationCustomizers,
            final HeartbeatProperties heartbeatProperties) {

        metaDataExtender.extendMetaData();
        return ConsulAutoRegistration.registration(
                autoServiceRegistrationProperties,
                consulDiscoveryProperties,
                applicationContext,
                consulRegistrationCustomizers,
                heartbeatProperties);
    }
}

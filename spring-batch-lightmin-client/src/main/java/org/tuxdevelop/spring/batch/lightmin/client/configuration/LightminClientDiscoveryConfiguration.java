package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.lifecycle.LightminConsulLifeCycle;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.lifecycle.LightminEurekaClientConfigBean;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.ConsulMetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.EurekaMetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.NoOperationMetaDataExtender;

@Configuration
@ConditionalOnSingleCandidate(value = DiscoveryClient.class)
@ConditionalOnProperty(prefix = "spring.batch.lightmin.client", value = "discovery-enabled", havingValue = "true")
public class LightminClientDiscoveryConfiguration {

    @Configuration
    @ConditionalOnBean(value = {EurekaClient.class})
    public class EurekaLightminClientDiscoveryConfiguration {
        @Bean
        public MetaDataExtender metaDataExtender() {
            return new EurekaMetaDataExtender();
        }

        @Bean
        public EurekaClientConfig eurekaClientConfig(final MetaDataExtender metaDataExtender) {
            return new LightminEurekaClientConfigBean(metaDataExtender);
        }
    }

    @Configuration
    @ConditionalOnBean(value = {ConsulDiscoveryClient.class})
    public class ConsulLightminClientDiscoveryConfiguration {
        @Bean
        public MetaDataExtender metaDataExtender(final ConsulDiscoveryProperties consulDiscoveryProperties) {
            return new ConsulMetaDataExtender(consulDiscoveryProperties);
        }

        @Bean
        @ConditionalOnMissingBean(value = {LightminConsulLifeCycle.class})
        public LightminConsulLifeCycle consulLifeCycle(final ConsulServiceRegistry consulServiceRegistry,
                                                       final ConsulDiscoveryProperties consulDiscoveryProperties,
                                                       final ConsulAutoRegistration consulAutoRegistration,
                                                       final MetaDataExtender metaDataExtender) {
            return new LightminConsulLifeCycle(
                    consulServiceRegistry,
                    consulDiscoveryProperties,
                    consulAutoRegistration,
                    metaDataExtender);
        }

    }

    @Bean
    @ConditionalOnMissingBean(MetaDataExtender.class)
    public MetaDataExtender metaDataExtender() {
        return new NoOperationMetaDataExtender();
    }


}

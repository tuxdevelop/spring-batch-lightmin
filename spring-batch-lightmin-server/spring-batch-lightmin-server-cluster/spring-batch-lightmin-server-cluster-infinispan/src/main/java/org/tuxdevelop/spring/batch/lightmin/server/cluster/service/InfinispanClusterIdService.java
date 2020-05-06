package org.tuxdevelop.spring.batch.lightmin.server.cluster.service;


import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import static org.tuxdevelop.spring.batch.lightmin.server.cluster.configuration.InfinispanServerClusterConfiguration.REPOSITORY_ID_CACHE_NAME;

@Slf4j
public class InfinispanClusterIdService implements InitializingBean {

    public static final Long initialValue = 1L;

    private final Cache<String, Long> idCache;

    public InfinispanClusterIdService(@Qualifier(REPOSITORY_ID_CACHE_NAME) final org.springframework.cache.Cache cache) {
        this.idCache = (Cache<String, Long>) cache.getNativeCache();
    }

    public Long getNextId(final String key) {
        final Long id;
        final Long incId;
        id = this.idCache.getOrDefault(key, initialValue);
        incId = id + 1;
        this.idCache.put(key, incId);
        return id;
    }

    @Override
    public void afterPropertiesSet() {
        if (this.idCache == null) {
            throw new SpringBatchLightminConfigurationException("No cache with the name " + REPOSITORY_ID_CACHE_NAME + " is configured");
        } else {
            log.trace("Cache initialized");
        }

    }
}

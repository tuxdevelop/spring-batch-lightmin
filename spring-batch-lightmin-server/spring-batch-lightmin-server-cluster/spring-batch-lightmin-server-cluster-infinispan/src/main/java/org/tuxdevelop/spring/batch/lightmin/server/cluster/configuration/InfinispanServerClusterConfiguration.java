package org.tuxdevelop.spring.batch.lightmin.server.cluster.configuration;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.embedded.provider.SpringEmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.lock.InfinispanLightminServerLockManager;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.lock.LightminServerLockManager;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.repository.*;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.service.InfinispanClusterIdService;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import static org.tuxdevelop.spring.batch.lightmin.server.cluster.lock.InfinispanLightminServerLockManager.VERIFICATION_CACHE_NAME;

@Slf4j
@Configuration
@EnableConfigurationProperties(value = {InfinispanServerClusterConfigurationProperties.class})
public class InfinispanServerClusterConfiguration extends CommonServerClusterConfiguration {

    public static final String INFINISPAN_DEFAULT_CACHE_MANAGER_NAME = "lightminInfinispanCacheManager";
    public static final String REPOSITORY_ID_CACHE_NAME = "lightminInfinispanRepositoryIdCache";
    public static final String SCHEDULER_EXECUTION_REPOSITORY_CACHE_NAME = "lightminInfinispanServerSchedulerExecutionRepositoryCache";
    public static final String SCHEDULER_CONFIGURATION_REPOSITORY_CACHE_NAME = "lightminInfinispanServerSchedulerConfigurationRepositoryCache";
    public static final String INFINISPAN_LIGHTMIN_APPLICATION_REPOSITORY_CACHE_NAME = "infinispanLightminApplicationRepositoryCache";
    public static final String INFINISPAN_JOB_EXECUTION_EVENT_REPOSITORY_CACHE_NAME = "infinispanJobExecutionEventRepositoryCache";
    public static final String INFINISPAN_JOURNAL_REPOSITORY_CACHE_NAME = "infinispanJournalRepositoryCache";

    /*
     * CACHE MANAGER
     */

    @Bean(name = INFINISPAN_DEFAULT_CACHE_MANAGER_NAME)
    @ConditionalOnMissingBean(SpringEmbeddedCacheManager.class)
    public SpringEmbeddedCacheManager infinispanCacheManager(final GlobalConfiguration globalConfiguration) {
        final EmbeddedCacheManager manager = new DefaultCacheManager(globalConfiguration);
        return new SpringEmbeddedCacheManager(manager);
    }

    /*
     * INFINISPAN CACHES
     */

    @Bean
    @DependsOn(value = {VERIFICATION_CACHE_NAME})
    public LightminServerLockManager lightminServerLockManager(final SpringEmbeddedCacheManager springEmbeddedCacheManager) {
        return new InfinispanLightminServerLockManager(springEmbeddedCacheManager.getNativeCacheManager());
    }

    @Bean(name = VERIFICATION_CACHE_NAME)
    public org.springframework.cache.Cache lightminLockVerificationCache(final SpringEmbeddedCacheManager springEmbeddedCacheManager) {

        final org.infinispan.configuration.cache.Configuration configuration = this.getConfiguration();

        return this.getSpringCache(springEmbeddedCacheManager, configuration, VERIFICATION_CACHE_NAME);
    }

    @Bean(name = REPOSITORY_ID_CACHE_NAME)
    public org.springframework.cache.Cache lightminRepositoryIdCache(final SpringEmbeddedCacheManager springEmbeddedCacheManager) {
        final org.infinispan.configuration.cache.Configuration configuration = this.getConfiguration();
        return this.getSpringCache(springEmbeddedCacheManager, configuration, REPOSITORY_ID_CACHE_NAME);
    }

    /*
     * INFINISPAN REPOSITORIES
     */

    @Bean
    public LightminApplicationRepository lightminApplicationRepository(final SpringEmbeddedCacheManager embeddedCacheManager) {
        final org.infinispan.configuration.cache.Configuration configuration = this.getLightminApplicationRepositoryConfiguration();
        final Cache<?, ?> cache = this.getCache(embeddedCacheManager, configuration, INFINISPAN_LIGHTMIN_APPLICATION_REPOSITORY_CACHE_NAME);
        return new InfinispanLightminApplicationRepository((Cache<String, LightminClientApplication>) cache);
    }

    @Bean
    public JobExecutionEventRepository jobExecutionEventRepository(final SpringEmbeddedCacheManager embeddedCacheManager,
                                                                   final LightminServerCoreProperties properties) {
        final org.infinispan.configuration.cache.Configuration configuration =
                this.getJobExecutionEventRepositoryConfiguration(properties.getEventRepositorySize());
        final Cache<?, ?> cache = this.getCache(embeddedCacheManager, configuration, INFINISPAN_JOB_EXECUTION_EVENT_REPOSITORY_CACHE_NAME);
        return new InfinispanJobExecutionEventRepository((Cache<Long, JobExecutionEventInfo>) cache);
    }

    @Bean
    public JournalRepository journalRepository(final SpringEmbeddedCacheManager embeddedCacheManager,
                                               final InfinispanClusterIdService infinispanClusterIdService) {
        final org.infinispan.configuration.cache.Configuration configuration = this.getJournalRepositoryConfiguration();
        final Cache<?, ?> cache = this.getCache(embeddedCacheManager, configuration, INFINISPAN_JOURNAL_REPOSITORY_CACHE_NAME);
        return new InfinispanJournalRepository((Cache<Long, Journal>) cache, infinispanClusterIdService);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.batch.lightmin.server.cluster.infinispan.repository",
            name = "init-scheduler-execution-repository",
            havingValue = "true")
    public SchedulerExecutionRepository schedulerExecutionRepository(final SpringEmbeddedCacheManager embeddedCacheManager,
                                                                     final InfinispanClusterIdService infinispanClusterIdService) {
        final org.infinispan.configuration.cache.Configuration configuration = this.getConfiguration();
        final Cache<?, ?> cache = this.getCache(embeddedCacheManager, configuration, SCHEDULER_EXECUTION_REPOSITORY_CACHE_NAME);
        return new InfinispanSchedulerExecutionRepository((Cache<Long, SchedulerExecution>) cache, infinispanClusterIdService);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.batch.lightmin.server.cluster.infinispan.repository",
            name = "init-scheduler-configuration-repository",
            havingValue = "true")
    public SchedulerConfigurationRepository schedulerConfigurationRepository(final SpringEmbeddedCacheManager embeddedCacheManager,
                                                                             final InfinispanClusterIdService infinispanClusterIdService) {
        final org.infinispan.configuration.cache.Configuration configuration = this.getConfiguration();
        final Cache<?, ?> cache = this.getCache(embeddedCacheManager, configuration, SCHEDULER_CONFIGURATION_REPOSITORY_CACHE_NAME);
        return new InfinispanSchedulerConfigurationRepository((Cache<Long, SchedulerConfiguration>) cache, infinispanClusterIdService);
    }

    /*
     * SERVICES
     */

    @Bean
    public InfinispanClusterIdService infinispanClusterIdService(@Qualifier(REPOSITORY_ID_CACHE_NAME) final org.springframework.cache.Cache cache) {
        return new InfinispanClusterIdService(cache);
    }

    /*
     * HELPERS AND CONFIGURATIONS
     */

    protected org.infinispan.configuration.cache.Configuration getJobExecutionEventRepositoryConfiguration(final long limit) {
        return this.getConfiguration(limit);
    }

    protected org.infinispan.configuration.cache.Configuration getLightminApplicationRepositoryConfiguration() {
        return this.getConfiguration();
    }

    protected org.infinispan.configuration.cache.Configuration getJournalRepositoryConfiguration() {
        return this.getConfiguration(20L);
    }

    protected org.infinispan.configuration.cache.Configuration getConfiguration(final Long limit) {
        return new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .stateTransfer()
                .awaitInitialTransfer(Boolean.FALSE)
                .statistics()
                .enable()
                .memory()
                .maxCount(limit)
                .build();
    }

    protected org.infinispan.configuration.cache.Configuration getConfiguration() {
        return new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .stateTransfer()
                .awaitInitialTransfer(Boolean.FALSE)
                .statistics()
                .enable()
                .build();
    }

    private Cache<?, ?> getCache(
            final SpringEmbeddedCacheManager springEmbeddedCacheManager,
            final org.infinispan.configuration.cache.Configuration configuration,
            final String cacheName) {
        springEmbeddedCacheManager.getNativeCacheManager().defineConfiguration(cacheName, configuration);

        if (springEmbeddedCacheManager.getNativeCacheManager().isRunning(cacheName)) {
            log.info("Cache {} is already running, nothing to start", cacheName);
        } else {
            springEmbeddedCacheManager.getNativeCacheManager().startCaches(cacheName);
        }
        return springEmbeddedCacheManager.getNativeCacheManager().getCache(cacheName);
    }

    private org.springframework.cache.Cache getSpringCache(
            final SpringEmbeddedCacheManager springEmbeddedCacheManager,
            final org.infinispan.configuration.cache.Configuration configuration,
            final String cacheName) {
        springEmbeddedCacheManager.getNativeCacheManager().defineConfiguration(cacheName, configuration);

        if (springEmbeddedCacheManager.getNativeCacheManager().isRunning(cacheName)) {
            log.info("Cache {} is already running, nothing to start", cacheName);
        } else {
            springEmbeddedCacheManager.getNativeCacheManager().startCaches(cacheName);
        }
        return springEmbeddedCacheManager.getCache(cacheName);
    }


}

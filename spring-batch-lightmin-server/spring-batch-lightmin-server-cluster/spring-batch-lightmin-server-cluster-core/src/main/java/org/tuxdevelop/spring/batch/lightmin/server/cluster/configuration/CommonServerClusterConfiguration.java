package org.tuxdevelop.spring.batch.lightmin.server.cluster.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.actuator.LockEndpoint;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.lock.LightminServerLockManager;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.lock.ServerLockAspect;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.BaseRemoteStandaloneConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.BaseStandaloneConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerStandaloneConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.annotation.EnableServerSchedulerCore;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.ClusterSchedulerConfiguration;

@EnableAspectJAutoProxy
@EnableServerSchedulerCore
@Import(ClusterSchedulerConfiguration.class)
public class CommonServerClusterConfiguration extends BaseRemoteStandaloneConfiguration {

    @Bean
    public ServerLockAspect serverLockAspect(final LightminServerLockManager lightminServerLockManager) {
        return new ServerLockAspect(lightminServerLockManager);
    }

    @Bean
    public LockEndpoint lockEndpoint(final LightminServerLockManager lightminServerLockManager) {
        return new LockEndpoint(lightminServerLockManager);
    }

}

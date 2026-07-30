package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
public class JpaSpringBatchLightminBatchConfigurer extends BasicSpringBatchLightminBatchConfigurer {

    private final EntityManagerFactory entityManagerFactory;

    public JpaSpringBatchLightminBatchConfigurer(final EntityManagerFactory entityManagerFactory) {
        super();
        this.entityManagerFactory = entityManagerFactory;
    }

    public JpaSpringBatchLightminBatchConfigurer(final DataSource dataSource,
                                                 final String tablePrefix,
                                                 final EntityManagerFactory entityManagerFactory) {
        super(dataSource, tablePrefix);
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    protected String determineIsolationLevel() {
        log.warn("JPA does not support custom isolation levels, so locks may not be taken when launching jobs");
        return "ISOLATION_DEFAULT";
    }

    @Override
    protected PlatformTransactionManager createTransactionManager() {
        return new JpaTransactionManager(this.entityManagerFactory);
    }
}

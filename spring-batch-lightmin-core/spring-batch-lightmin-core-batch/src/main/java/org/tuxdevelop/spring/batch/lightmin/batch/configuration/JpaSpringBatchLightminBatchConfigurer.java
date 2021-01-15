package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
public class JpaSpringBatchLightminBatchConfigurer extends BasicSpringBatchLightminBatchConfigurer{

    private final EntityManagerFactory entityManagerFactory;

    public JpaSpringBatchLightminBatchConfigurer(final TransactionManagerCustomizers transactionManagerCustomizers,
                                                 final EntityManagerFactory entityManagerFactory) {
        super(transactionManagerCustomizers);
        this.entityManagerFactory = entityManagerFactory;
    }

    public JpaSpringBatchLightminBatchConfigurer(final TransactionManagerCustomizers transactionManagerCustomizers,
                                                 final DataSource dataSource,
                                                 final String tablePrefix,
                                                 final EntityManagerFactory entityManagerFactory) {
        super(transactionManagerCustomizers, dataSource, tablePrefix);
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    protected String determineIsolationLevel() {
        log.warn("JPA does not support custom isolation levels, so locks may not be taken when launching Jobs");
        return "ISOLATION_DEFAULT";
    }

    @Override
    protected PlatformTransactionManager createTransactionManager() {
        return new JpaTransactionManager(this.entityManagerFactory);
    }
}

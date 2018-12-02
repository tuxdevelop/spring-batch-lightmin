package org.tuxdevelop.spring.batch.lightmin.domain;

import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

import static org.assertj.core.api.Fail.fail;

public class JobSchedulerConfigurationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobSchedulerConfiguration.class);
        this.testEquals(JobSchedulerConfiguration.class);
    }

    @Test
    public void validateForPeriodTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(1000L);
        jobSchedulerConfiguration.setInitialDelay(1000L);
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        try {
            jobSchedulerConfiguration.validate();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateForCronTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.CRON);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        try {
            jobSchedulerConfiguration.validate();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validateSchedulerNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setJobSchedulerType(null);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        jobSchedulerConfiguration.validate();
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validateTaskExecutorNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.CRON);
        jobSchedulerConfiguration.setTaskExecutorType(null);
        jobSchedulerConfiguration.validate();
    }

    @Test
    public void validateCronTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        try {
            jobSchedulerConfiguration.validateCron();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validateCronExpressionNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression(null);
        jobSchedulerConfiguration.validateCron();
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validateCronExpressionNotValidTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("invalid");
        jobSchedulerConfiguration.validateCron();
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validateCronExpressionFixedDelaySetTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setFixedDelay(1000L);
        jobSchedulerConfiguration.validateCron();
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validateCronExpressionInitialDelaySetTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setInitialDelay(1000L);
        jobSchedulerConfiguration.validateCron();
    }

    @Test
    public void validatePeriodTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(1000L);
        jobSchedulerConfiguration.setInitialDelay(1000L);
        try {
            jobSchedulerConfiguration.validatePeriod();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validatePeriodFixedDelayNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.validateCron();
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validatePeriodFixedDelayNegativTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(-1000L);
        jobSchedulerConfiguration.validateCron();
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validatePeriodInitialDelayNegativTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(-1000L);
        jobSchedulerConfiguration.setInitialDelay(-1000L);
        jobSchedulerConfiguration.validateCron();
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void validatePeriodCronExpressionSetTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(1000L);
        jobSchedulerConfiguration.setInitialDelay(1000L);
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.validateCron();
    }
}

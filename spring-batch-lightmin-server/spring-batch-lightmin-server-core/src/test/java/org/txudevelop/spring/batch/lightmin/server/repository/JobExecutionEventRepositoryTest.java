package org.txudevelop.spring.batch.lightmin.server.repository;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;

import java.util.List;

public abstract class JobExecutionEventRepositoryTest {

    @Test
    public void testSave() {
        final JobExecutionEventInfo jobExecutionEventInfo =
                ServerDomainHelper.createJobExecutionEventInfo();
        final JobExecutionEventInfo saved = getJobExecutionEventRepository().save(jobExecutionEventInfo);
        final int totalCount = this.getJobExecutionEventRepository().getTotalCount();
        final List<JobExecutionEventInfo> all = this.getJobExecutionEventRepository().findAll(0, totalCount);
        BDDAssertions.then(totalCount).isEqualTo(1);
        BDDAssertions.then(all).hasSize(1);
        BDDAssertions.then(all).contains(saved);
    }

    @Test
    public void testFindAll() {
        final int count = 10;
        for (int i = 0; i < count; i++) {
            final JobExecutionEventInfo jobExecutionEventInfo =
                    ServerDomainHelper.createJobExecutionEventInfo();
            getJobExecutionEventRepository().save(jobExecutionEventInfo);
        }
        final int totalCount = this.getJobExecutionEventRepository().getTotalCount();
        final int fetchCount = 5;
        final List<JobExecutionEventInfo> all0to5 = this.getJobExecutionEventRepository().findAll(0, fetchCount);
        final List<JobExecutionEventInfo> all5to10 = this.getJobExecutionEventRepository().findAll(fetchCount, fetchCount);
        BDDAssertions.then(totalCount).isEqualTo(count);
        BDDAssertions.then(all0to5).hasSize(fetchCount);
        BDDAssertions.then(all5to10).hasSize(fetchCount);
        BDDAssertions.then(all5to10).doesNotContain(all0to5.toArray(new JobExecutionEventInfo[5]));
    }

    @Test
    public void testFinalByExitStatus() {
        final int countCompleted = 10;
        final ExitStatus completed = new ExitStatus("COMPLETED");
        final ExitStatus failed = new ExitStatus("FAILED");
        for (int i = 0; i < countCompleted; i++) {
            final JobExecutionEventInfo jobExecutionEventInfo =
                    ServerDomainHelper.createJobExecutionEventInfo(completed);
            getJobExecutionEventRepository().save(jobExecutionEventInfo);
        }
        final int countFailed = 10;
        for (int i = 0; i < countFailed; i++) {
            final JobExecutionEventInfo jobExecutionEventInfo =
                    ServerDomainHelper.createJobExecutionEventInfo(failed);
            getJobExecutionEventRepository().save(jobExecutionEventInfo);
        }

        final List<JobExecutionEventInfo> allCompleted =
                this.getJobExecutionEventRepository().finalByExitStatus(completed, 0, countCompleted);

        final List<JobExecutionEventInfo> allfailed =
                this.getJobExecutionEventRepository().finalByExitStatus(failed, 0, countFailed);

        BDDAssertions.then(allCompleted).hasSize(countCompleted);
        BDDAssertions.then(allfailed).hasSize(countFailed);

        allCompleted.forEach(
                info -> BDDAssertions.then(info.getExitStatus()).isEqualTo(completed)
        );

        allfailed.forEach(
                info -> BDDAssertions.then(info.getExitStatus()).isEqualTo(failed)
        );

    }

    @Test
    public void testLimit() {
        final int limit = getLightminServerCoreProperties().getEventRepositorySize();

        final int count = 2 * limit;

        for (int i = 0; i < count; i++) {
            final JobExecutionEventInfo jobExecutionEventInfo =
                    ServerDomainHelper.createJobExecutionEventInfo();
            getJobExecutionEventRepository().save(jobExecutionEventInfo);
        }

        final List<JobExecutionEventInfo> all =
                this.getJobExecutionEventRepository().findAll(0, count);
        final int totalCount = this.getJobExecutionEventRepository().getTotalCount();
        BDDAssertions.then(all).hasSize(limit);
        BDDAssertions.then(totalCount).isEqualTo(limit);
    }

    @Before
    public void init() {
        this.getJobExecutionEventRepository().clear();
    }

    protected abstract JobExecutionEventRepository getJobExecutionEventRepository();

    protected abstract LightminServerCoreProperties getLightminServerCoreProperties();
}

package org.txudevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public final class ServerDomainHelper {

    private ServerDomainHelper() {
    }

    public static LightminClientApplication createLightminClientApplication(final String name) {

        final LightminClientApplication application = new LightminClientApplication();
        application.setId(UUID.randomUUID().toString());
        application.setName(name);
        application.setServiceUrl("http://test.com:8180");
        application.setManagementUrl("http://test.com:8180");
        application.setHealthUrl("http://test.com:8180/health");
        application.setLightminClientInformation(new LightminClientInformation());
        application.setLightminClientApplicationStatus(LightminClientApplicationStatus.ofUp());

        return application;
    }

    public static JobExecutionEventInfo createJobExecutionEventInfo(final ExitStatus exitStatus) {
        return createJobExecutionEventInfo("testApp", "testJob", exitStatus);
    }

    public static JobExecutionEventInfo createJobExecutionEventInfo() {
        return createJobExecutionEventInfo("testApp", "testJob", new ExitStatus("COMPLETED"));
    }

    public static JobExecutionEventInfo createJobExecutionEventInfo(final String appName, final String jobName, final ExitStatus exitStatus) {
        final JobExecutionEventInfo info = new JobExecutionEventInfo();
        info.setApplicationName(appName);
        info.setEndDate(new Date());
        info.setExitStatus(exitStatus);
        info.setJobExecutionId(new Random().nextLong());
        info.setJobName(jobName);
        info.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        return info;
    }

    public static Journal createJournal() {
        return createJournal("testApp");
    }

    public static Journal createJournal(final String appName) {
        final Journal journal = new Journal();
        journal.setApplicationName(appName);
        journal.setHost("my.test.com");
        journal.setNewStatus("UP");
        journal.setOldStatus("DOWN");
        journal.setTimestamp(new Date());
        return journal;
    }
}

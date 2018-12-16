package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.CommonJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.ListenerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.SchedulerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.JobListenerModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.JobSchedulerModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerTypeModel;

import java.util.*;

public final class ServiceTestHelper {

    private ServiceTestHelper() {

    }

    public static List<JobExecutionEventInfo> createJobExecutionEvents(final int count, final String applicationName) {
        final List<JobExecutionEventInfo> jobExecutionEventInfos = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            final JobExecutionEventInfo jobExecutionEventInfo = createJobExecutionEvent((long) i, applicationName);
            jobExecutionEventInfos.add(jobExecutionEventInfo);
        }
        return jobExecutionEventInfos;
    }

    public static JobExecutionEventInfo createJobExecutionEvent(final Long jobExecutionId, final String applicationName) {
        final JobExecutionEventInfo jobExecutionEventInfo = new JobExecutionEventInfo();
        jobExecutionEventInfo.setApplicationName(applicationName);
        jobExecutionEventInfo.setEndDate(new Date());
        jobExecutionEventInfo.setStartDate(new Date());
        jobExecutionEventInfo.setExitStatus(new ExitStatus("COMPLETED"));
        jobExecutionEventInfo.setJobExecutionId(jobExecutionId);
        jobExecutionEventInfo.setJobName("test_job");
        return jobExecutionEventInfo;
    }

    public static LightminClientApplication createLightminClientApplication() {
        final LightminClientApplication lightminClientApplication = new LightminClientApplication();
        lightminClientApplication.setLightminClientInformation(createLightminClientInformation());
        return lightminClientApplication;
    }

    public static LightminClientInformation createLightminClientInformation() {
        final LightminClientInformation lightminClientInformation = new LightminClientInformation();
        lightminClientInformation.setRegisteredJobs(Arrays.asList("test_job_1", "test_job_2"));
        return lightminClientInformation;
    }

    public static SchedulerJobConfigurationModel createSchedulerJobConfigurationModel(final String jobName) {

        final SchedulerJobConfigurationModel schedulerJobConfigurationModel = new SchedulerJobConfigurationModel();
        schedulerJobConfigurationModel.setConfig(createSchedulerModel());
        mapConfig(schedulerJobConfigurationModel, jobName);
        return schedulerJobConfigurationModel;

    }

    public static JobSchedulerModel createSchedulerModel() {
        final JobSchedulerModel jobSchedulerModel = new JobSchedulerModel();
        jobSchedulerModel.setFixedDelay(1L);
        jobSchedulerModel.setInitialDelay(1L);
        jobSchedulerModel.setStatusRead(new SchedulerStatusModel(SchedulerStatusModel.SchedulerStatus.RUNNING));
        jobSchedulerModel.setStatus(jobSchedulerModel.getStatusRead().getValue());
        jobSchedulerModel.setTaskExecutorRead(new TaskExecutorTypeModel(TaskExecutorTypeModel.map(TaskExecutorType.SYNCHRONOUS)));
        jobSchedulerModel.setTaskExecutor(jobSchedulerModel.getTaskExecutorRead().getValue());
        jobSchedulerModel.setTypeRead(new SchedulerTypeModel(SchedulerTypeModel.JobSchedulerType.PERIOD));
        jobSchedulerModel.setType(jobSchedulerModel.getTypeRead().getValue());
        return jobSchedulerModel;
    }

    public static ListenerJobConfigurationModel createListenerJobConfigurationModel(final String jobName) {

        final ListenerJobConfigurationModel listenerJobConfigurationModel = new ListenerJobConfigurationModel();
        listenerJobConfigurationModel.setConfig(createListenerModel());
        mapConfig(listenerJobConfigurationModel, jobName);
        return listenerJobConfigurationModel;

    }

    public static void mapConfig(final CommonJobConfigurationModel jobConfigurationModel, final String jobName) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("hello", "world");
        jobConfigurationModel.setIncrementerRead(new JobIncremeterTypeModel(JobIncremeterTypeModel.map(JobIncrementer.DATE)));
        jobConfigurationModel.setIncrementer(jobConfigurationModel.getIncrementerRead().getValue());
        jobConfigurationModel.setJobName(jobName);
        jobConfigurationModel.setParameters("hello(String)=world");
        jobConfigurationModel.setParametersRead(parameters);
        jobConfigurationModel.setId(1L);
    }

    public static JobListenerModel createListenerModel() {
        final JobListenerModel jobListenerModel = new JobListenerModel();
        jobListenerModel.setFilePattern("*.txt");
        jobListenerModel.setPollerPeriod(1L);
        jobListenerModel.setSourceFolder("/test");
        jobListenerModel.setStatusRead(new ListenerStatusModel(ListenerStatusModel.map(ListenerStatus.ACTIVE)));
        jobListenerModel.setStatus(jobListenerModel.getStatusRead().getValue());
        jobListenerModel.setTaskExecutorRead(new TaskExecutorTypeModel(TaskExecutorTypeModel.map(TaskExecutorType.SYNCHRONOUS)));
        jobListenerModel.setTaskExecutor(jobListenerModel.getTaskExecutorRead().getValue());
        jobListenerModel.setTypeRead(new ListenerTypeModel(ListenerTypeModel.JobListenerType.LOCAL_FOLDER_LISTENER));
        jobListenerModel.setType(jobListenerModel.getTypeRead().getValue());
        return jobListenerModel;
    }

    public static List<Journal> createJournals(final int count) {
        final List<Journal> journals = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final Journal journal = createJournal((long) i);
            journals.add(journal);
        }
        return journals;
    }

    public static Journal createJournal(final Long id) {
        final Journal journal = new Journal();
        journal.setApplicationName("testApp");
        journal.setHost("localhost");
        journal.setId(id);
        journal.setOldStatus("DOWN");
        journal.setNewStatus("UP");
        journal.setTimestamp(new Date());
        return journal;
    }
}
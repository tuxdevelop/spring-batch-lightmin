package org.tuxdevelop.spring.batch.lightmin.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.IgnoreHiddenFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.transformer.AbstractFilePayloadTransformer;
import org.tuxdevelop.spring.batch.lightmin.domain.ListenerConstructorWrapper;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import java.io.File;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class FolderListener extends AbstractListener implements Listener {

    /**
     * public for resusability in {@link org.springframework.batch.core.Job}
     */
    public static final String FILE_SOURCE_PARAMETER_NAME = "fileSource";

    private CompositeFileListFilter<File> fileFileListFilter;
    private JobLaunchingMessageHandler jobLaunchingMessageHandler;
    private AbstractFilePayloadTransformer<JobLaunchRequest> transformer;

    public FolderListener(final ListenerConstructorWrapper listenerConstructorWrapper) {
        this.jobConfiguration = listenerConstructorWrapper.getJobConfiguration();
        this.job = listenerConstructorWrapper.getJob();
        this.jobParameters = listenerConstructorWrapper.getJobParameters();
        this.jobLauncher = listenerConstructorWrapper.getJobLauncher();
        this.jobIncrementer = listenerConstructorWrapper.getJobIncrementer();
        this.jobListenerConfiguration = this.jobConfiguration.getJobListenerConfiguration();
        this.listenerStatus = listenerConstructorWrapper.getJobConfiguration().getJobListenerConfiguration().getListenerStatus();
        this.assertConstructor();
        try {
            this.attachJobIncrementer();
            this.initFileListFilter();
            this.initTransformer();
            this.initJobLaunchingMessageHandler();
            this.initIntegrationFlow();
        } catch (final Exception e) {
            throw new SpringBatchLightminConfigurationException(e.getMessage());
        }
    }

    private void initIntegrationFlow() {
        this.integrationFlow = IntegrationFlows
                .from(Files.inboundAdapter(new File(this.jobListenerConfiguration.getSourceFolder()))
                        .filter(this.fileFileListFilter)
                        .scanEachPoll(Boolean.TRUE)
                        .get(), e -> {
                    e.poller(Pollers.fixedRate(this.jobListenerConfiguration.getPollerPeriod()).maxMessagesPerPoll(1000));
                    e.autoStartup(Boolean.TRUE);
                })
                .transform(this.transformer,
                        e -> e.autoStartup(Boolean.TRUE))
                .handle(this.jobLaunchingMessageHandler)
                .channel(MessageChannels.direct())
                .handle(new JobExecutionFinishedMessageHandler())
                .get();

    }

    private void initFileListFilter() throws Exception {
        this.fileFileListFilter = new CompositeFileListFilter<>();
        this.fileFileListFilter.addFilter(new AcceptOnceFileListFilter<>());
        this.fileFileListFilter.addFilter(new IgnoreHiddenFileListFilter());

        this.fileFileListFilter.addFilter(new SimplePatternFileListFilter(this.jobListenerConfiguration.getFilePattern()));
    }

    private void initTransformer() {
        this.transformer = new AbstractFilePayloadTransformer<JobLaunchRequest>() {
            @Override
            protected JobLaunchRequest transformFile(final File file) throws Exception {
                FolderListener.this.attachFileSourceToJobParameters(file);
                return new JobLaunchRequest(FolderListener.this.job, FolderListener.this.jobParameters);
            }
        };
    }

    private void initJobLaunchingMessageHandler() {
        this.jobLaunchingMessageHandler = new JobLaunchingMessageHandler(this.jobLauncher);
    }

    private void attachFileSourceToJobParameters(final File file) {
        final JobParametersBuilder jobParametersBuilder;
        if (this.jobParameters == null) {
            jobParametersBuilder = new JobParametersBuilder();
        } else {
            jobParametersBuilder = new JobParametersBuilder(this.jobParameters);
        }
        final String sourceFile = file.getAbsolutePath();
        jobParametersBuilder.addString(FILE_SOURCE_PARAMETER_NAME, sourceFile);
        this.jobParameters = jobParametersBuilder.toJobParameters();
    }

}

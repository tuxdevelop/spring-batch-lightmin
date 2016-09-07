package org.tuxdevelop.spring.batch.lightmin.admin.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.integration.dsl.GenericEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.file.Files;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.IgnoreHiddenFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.transformer.AbstractFilePayloadTransformer;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.ListenerConstructorWrapper;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import java.io.File;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class FolderListener extends AbstractListener implements Listener {

    private static final String FILE_SOURCE_PARAMETER_NAME = "fileSource";

    private CompositeFileListFilter<File> fileFileListFilter;
    private JobLaunchingMessageHandler jobLaunchingMessageHandler;
    private AbstractFilePayloadTransformer<JobLaunchRequest> transformer;

    public FolderListener(final ListenerConstructorWrapper listenerConstructorWrapper) {
        this.jobConfiguration = listenerConstructorWrapper.getJobConfiguration();
        this.job = listenerConstructorWrapper.getJob();
        this.jobParameters = listenerConstructorWrapper.getJobParameters();
        this.jobLauncher = listenerConstructorWrapper.getJobLauncher();
        this.jobIncrementer = listenerConstructorWrapper.getJobIncrementer();
        this.jobListenerConfiguration = jobConfiguration.getJobListenerConfiguration();
        this.listenerStatus = listenerConstructorWrapper.getJobConfiguration().getJobListenerConfiguration().getListenerStatus();
        assertConstructor();
        try {
            attachJobIncrementer();
            initFileListFilter();
            initTransformer();
            initJobLaunchingMessageHandler();
            initIntegrationFlow();
        } catch (final Exception e) {
            throw new SpringBatchLightminConfigurationException(e.getMessage());
        }
    }

    private void initIntegrationFlow() {
        integrationFlow = IntegrationFlows
                .from(Files.inboundAdapter(new File(jobListenerConfiguration.getSourceFolder()))
                        .filter(fileFileListFilter)
                        .scanEachPoll(Boolean.TRUE)
                        .get(), new Consumer<SourcePollingChannelAdapterSpec>() {
                    @Override
                    public void accept(final SourcePollingChannelAdapterSpec e) {
                        e.poller(Pollers.fixedRate(jobListenerConfiguration.getPollerPeriod()).maxMessagesPerPoll(1000));
                        e.autoStartup(Boolean.TRUE);
                    }
                })
                .transform(transformer,
                        new Consumer<GenericEndpointSpec<MessageTransformingHandler>>() {
                            @Override
                            public void accept(final GenericEndpointSpec<MessageTransformingHandler> e) {
                                e.autoStartup(Boolean.TRUE);
                            }
                        })
                .handle(jobLaunchingMessageHandler)
                .channel(MessageChannels.direct())
                .handle(new MessageHandler() {
                    @Override
                    public void handleMessage(final Message<?> message) throws MessagingException {
                        final JobExecution jobExecution = (JobExecution) message.getPayload();
                        log.info("Executed: {} ", jobExecution);
                    }
                })
                .get();

    }

    private void initFileListFilter() throws Exception {
        this.fileFileListFilter = new CompositeFileListFilter<>();
        fileFileListFilter.addFilter(new AcceptOnceFileListFilter<File>());
        fileFileListFilter.addFilter(new IgnoreHiddenFileListFilter());

        fileFileListFilter.addFilter(new SimplePatternFileListFilter(jobListenerConfiguration.getFilePattern()));
    }

    private void initTransformer() {
        transformer = new AbstractFilePayloadTransformer<JobLaunchRequest>() {
            @Override
            protected JobLaunchRequest transformFile(final File file) throws Exception {
                attachFileSourceToJobParameters(file);
                return new JobLaunchRequest(job, jobParameters);
            }
        };
    }

    private void initJobLaunchingMessageHandler() {
        jobLaunchingMessageHandler = new JobLaunchingMessageHandler(jobLauncher);
    }

    private void attachFileSourceToJobParameters(final File file) {
        final JobParametersBuilder jobParametersBuilder;
        if (jobParameters == null) {
            jobParametersBuilder = new JobParametersBuilder();
        } else {
            jobParametersBuilder = new JobParametersBuilder(jobParameters);
        }
        final String sourceFile = file.getAbsolutePath();
        jobParametersBuilder.addString(FILE_SOURCE_PARAMETER_NAME, sourceFile);
        jobParameters = jobParametersBuilder.toJobParameters();
    }

}

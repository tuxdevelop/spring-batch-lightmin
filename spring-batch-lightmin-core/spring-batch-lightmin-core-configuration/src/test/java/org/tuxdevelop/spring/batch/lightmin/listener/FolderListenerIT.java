package org.tuxdevelop.spring.batch.lightmin.listener;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.domain.JobListenerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.ListenerConstructorWrapper;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;
import org.tuxdevelop.test.configuration.ITConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class FolderListenerIT {

    @Autowired
    private BeanRegistrar beanRegistrar;
    @Autowired
    private Job simpleJob;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testFolderListener() {
        final String directory = "src/test/resources/input";
        final File file = new File(directory);
        assertThat(file.isDirectory()).isTrue();
        log.info("File Path: {}", file.getAbsoluteFile());
        final File[] files = file.listFiles();
        for (final File readFile : files) {
            log.info("Read File: {}", readFile);
        }
        final String beanName = "simpleIntgerationFlowBean";
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        final JobListenerConfiguration jobListenerConfiguration = new JobListenerConfiguration();
        jobListenerConfiguration.setSourceFolder(directory);
        jobListenerConfiguration.setFilePattern("*.txt");
        jobListenerConfiguration.setPollerPeriod(10L);
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);

        final ListenerConstructorWrapper listenerConstructorWrapper = new ListenerConstructorWrapper();
        listenerConstructorWrapper.setJobIncrementer(JobIncrementer.DATE);
        listenerConstructorWrapper.setJob(this.simpleJob);
        listenerConstructorWrapper.setJobConfiguration(jobConfiguration);
        listenerConstructorWrapper.setJobLauncher(this.jobLauncher);
        listenerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        final Set<Object> constructorValues = new HashSet<>();
        constructorValues.add(listenerConstructorWrapper);
        this.beanRegistrar.registerBean(FolderListener.class, beanName, constructorValues, null, null, null, null);
        final FolderListener folderListener = this.applicationContext.getBean(beanName, FolderListener.class);
        folderListener.start();
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        final List<JobInstance> jobinstances = this.jobExplorer.findJobInstancesByJobName("simpleJob", 0, 10);
        assertThat(jobinstances).hasSize(1);
    }

}

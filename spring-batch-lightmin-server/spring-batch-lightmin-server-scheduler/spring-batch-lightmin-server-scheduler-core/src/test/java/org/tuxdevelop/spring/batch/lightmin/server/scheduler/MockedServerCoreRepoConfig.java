package org.tuxdevelop.spring.batch.lightmin.server.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class MockedServerCoreRepoConfig {

    @Bean
    LightminApplicationRepository lightminApplicationRepository() {
        return mock(LightminApplicationRepository.class);
    }

    @Bean
    JobExecutionEventRepository jobExecutionEventRepository() {
        return mock(JobExecutionEventRepository.class);
    }

    @Bean
    JournalRepository journalRepository() {
        return mock(JournalRepository.class);
    }
}

package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.TaskExecutorType;

/**
 * @since 0.5
 * @author Marcel Becker
 */
public interface JobLauncherService {

    /**
     * Interface for a services which has to create a {@link JobLauncher}
     *
     * @param taskExecutorType type of the {@link JobLauncher}
     * @param jobRepository the {@link JobRepository} the {@link JobLauncher} is connected to
     * @return the {@link JobLauncher}
     */
    JobLauncher createLobLauncher(TaskExecutorType taskExecutorType, JobRepository jobRepository);

}

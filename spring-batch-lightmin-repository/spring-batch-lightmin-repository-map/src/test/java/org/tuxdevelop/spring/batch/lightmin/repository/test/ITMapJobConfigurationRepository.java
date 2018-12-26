package org.tuxdevelop.spring.batch.lightmin.repository.test;

import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.repository.MapJobConfigurationRepository;

import java.util.Collection;

import static org.junit.Assert.fail;

public class ITMapJobConfigurationRepository extends MapJobConfigurationRepository implements ITJobConfigurationRepository {

    @Override
    public void clean(final String applicationName) {
        final Collection<JobConfiguration> allJobConfigurations = super.getAllJobConfigurations(applicationName);
        for (final JobConfiguration jobConfiguration : allJobConfigurations) {
            try {
                super.delete(jobConfiguration, applicationName);
            } catch (final NoSuchJobConfigurationException e) {
                fail(e.getMessage());
            }
        }
    }
}

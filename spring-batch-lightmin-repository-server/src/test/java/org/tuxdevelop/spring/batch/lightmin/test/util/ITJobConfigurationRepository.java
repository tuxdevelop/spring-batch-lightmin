package org.tuxdevelop.spring.batch.lightmin.test.util;

import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;

public interface ITJobConfigurationRepository extends JobConfigurationRepository {

    void clean(final String applicationName);
}

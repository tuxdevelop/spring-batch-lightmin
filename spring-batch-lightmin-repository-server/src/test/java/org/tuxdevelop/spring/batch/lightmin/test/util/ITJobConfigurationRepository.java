package org.tuxdevelop.spring.batch.lightmin.test.util;

import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;

public interface ITJobConfigurationRepository extends JobConfigurationRepository {

    void clean(final String applicationName);
}

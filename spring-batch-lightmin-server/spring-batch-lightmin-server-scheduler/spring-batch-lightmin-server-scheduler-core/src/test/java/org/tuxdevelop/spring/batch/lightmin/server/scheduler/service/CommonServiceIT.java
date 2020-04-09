package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.junit.Before;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;

public abstract class CommonServiceIT {

    protected abstract CleanUpRepository cleanUpRepository();

    @Before
    public void init() {
        this.cleanUpRepository().cleanUp();
    }
}

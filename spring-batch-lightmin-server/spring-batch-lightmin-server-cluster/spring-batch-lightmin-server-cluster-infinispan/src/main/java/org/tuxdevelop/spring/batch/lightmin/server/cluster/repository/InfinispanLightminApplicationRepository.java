package org.tuxdevelop.spring.batch.lightmin.server.cluster.repository;

import org.infinispan.Cache;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.KVLightminApplicationRepository;

public class InfinispanLightminApplicationRepository extends KVLightminApplicationRepository<Cache<String, LightminClientApplication>> {
    
    public InfinispanLightminApplicationRepository(final Cache<String, LightminClientApplication> applications) {
        super(applications);
    }
}

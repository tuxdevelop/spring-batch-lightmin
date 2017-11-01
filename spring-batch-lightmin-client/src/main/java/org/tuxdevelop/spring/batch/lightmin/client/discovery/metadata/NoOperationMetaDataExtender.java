package org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class NoOperationMetaDataExtender implements MetaDataExtender {

    @Override
    public void extendMetaData() {
        log.warn("NoOperation Spring Batch Lightmin MetaDataExtender is active, no Meta Data will extended! Service Discovery on Server Side will not find the instance.");
    }
}

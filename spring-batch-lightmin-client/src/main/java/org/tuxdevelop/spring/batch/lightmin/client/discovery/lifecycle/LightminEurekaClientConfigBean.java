package org.tuxdevelop.spring.batch.lightmin.client.discovery.lifecycle;

import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class LightminEurekaClientConfigBean extends EurekaClientConfigBean {

    public LightminEurekaClientConfigBean(final MetaDataExtender metaDataExtender) {
        metaDataExtender.extendMetaData();
    }


}

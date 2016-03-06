package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {ITSchedulerConfiguration.class, ITJobConfiguration.class, ITPersistenceConfiguration.class})
public class ITConfiguration {

}

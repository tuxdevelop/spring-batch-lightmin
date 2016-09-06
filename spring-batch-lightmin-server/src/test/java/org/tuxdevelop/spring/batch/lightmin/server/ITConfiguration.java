package org.tuxdevelop.spring.batch.lightmin.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ITJobConfiguration.class)
public class ITConfiguration {
}

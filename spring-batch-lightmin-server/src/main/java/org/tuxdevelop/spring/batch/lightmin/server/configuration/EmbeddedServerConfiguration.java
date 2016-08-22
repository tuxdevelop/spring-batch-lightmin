package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {CommonServerConfiguration.class})
public class EmbeddedServerConfiguration {
}

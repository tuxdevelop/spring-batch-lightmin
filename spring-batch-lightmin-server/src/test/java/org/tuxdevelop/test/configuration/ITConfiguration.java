package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ITJobConfiguration.class)
public class ITConfiguration {
}

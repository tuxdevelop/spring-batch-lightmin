package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringBatchLightminWebConfiguration {

	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addViewControllers(
					final ViewControllerRegistry viewControllerRegistry) {
				viewControllerRegistry.addViewController("/").setViewName(
						"index");
				viewControllerRegistry.addViewController("/index").setViewName(
						"index");
				viewControllerRegistry.addViewController("/jobs").setViewName(
						"jobs");
			}
		};
	}

}

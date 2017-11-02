package com.github.ibiber.jutrack.application;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ibiber.common.http.RestServiceQuery;

@Configuration
public class JuTrackProcessorContextConfiguration {
	@Bean
	public RestServiceQuery restServiceQuery(RestTemplateBuilder builder) {
		return new RestServiceQuery(builder.build());
	}
}

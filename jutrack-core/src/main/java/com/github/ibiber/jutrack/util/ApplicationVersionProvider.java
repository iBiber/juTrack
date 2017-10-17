package com.github.ibiber.jutrack.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource("classpath:/juTrackBuild.properties")
@Component
public class ApplicationVersionProvider {
	@Autowired
	private Environment env;

	public String getApplicationVersion() {
		return env.getProperty("application.version");
	}
}

package com.github.ibiber.jutrack.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.external.DefaultValueProviderService;

@Component
public class DefaultValueProvider implements DefaultValueProviderService {
	private static final String JIRA_ROOT_URL = "ui.jira.root.url.default";

	private final Environment env;

	@Autowired
	public DefaultValueProvider(Environment env) {
		this.env = env;
	}

	@Override
	public String getJiraRootUrlPropertyName() {
		return JIRA_ROOT_URL;
	}

	@Override
	public String getJiraRootUrl() {
		String jiraRootUrl = env.getProperty(JIRA_ROOT_URL);
		if (jiraRootUrl == null) {
			return "";
		}
		return jiraRootUrl;
	}

	@Override
	public String getUserName() {
		String defaultUser = env.getProperty("ui.username.default");
		if (defaultUser == null) {
			return "";
		}
		return defaultUser;
	}

	@Override
	public int dayRange() {
		return env.getProperty("ui.startDate.default.day.range", Integer.class, 1);
	}
}

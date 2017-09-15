package com.github.ibiber.jutrack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DefaultValueProvider {
	private static final String JIRA_ROOT_URL = "ui.jira.root.url.default";

	private final Environment env;

	@Autowired
	public DefaultValueProvider(Environment env) {
		this.env = env;
	}

	public String getJiraRootUrlPropertyName() {
		return JIRA_ROOT_URL;
	}

	public String getJiraRootUrl() {
		String jiraRootUrl = env.getProperty(JIRA_ROOT_URL);
		if (jiraRootUrl == null) {
			return "";
		}
		return jiraRootUrl;
	}

	public String getUserName() {
		String defaultUser = env.getProperty("ui.username.default");
		if (defaultUser == null) {
			return "";
		}
		return defaultUser;
	}

	public int dayRange() {
		return env.getProperty("ui.startDate.default.day.range", Integer.class, 1);
	}
}

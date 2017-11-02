package com.github.ibiber.jutrack.external;

public interface DefaultValueProviderService {
	String getJiraRootUrlPropertyName();

	String getJiraRootUrl();

	String getUserName();

	int dayRange();
}
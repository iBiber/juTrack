package com.github.ibiber.jutrack;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.Credentials;
import com.github.ibiber.jutrack.data.jira.JiraIssuesQueryResults;
import com.github.ibiber.jutrack.util.JiraQuery;

@Component
public class JiraIssuesQueryExecutor {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraIssuesQueryExecutor.class);

	private JiraQuery jiraQuery;

	@Autowired
	public JiraIssuesQueryExecutor(JiraQuery jiraQuery) {
		this.jiraQuery = jiraQuery;
	}

	public JiraIssuesQueryResults getIssues(String jiraUrl, Credentials credentials, LocalDate startDate,
	        LocalDate endDate) {
		String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String url = jiraUrl + "/rest/api/2/search";
		String urlParameter = "jql=assignee was " + credentials.userName // Filter for user
		        + " AND status changed during (\"" + startDateStr + "\",\"" + endDateStr + "\")" // Filter for time range
		        + "&fields=key,summary" // reduce the issue result to the fields "key" and "summary"
		        + "&expand=changelog"; // collect the change log of each issue
		JiraIssuesQueryResults queryResults = jiraQuery.query(credentials, JiraIssuesQueryResults.class, url,
		        urlParameter);

		if (queryResults.total > queryResults.maxResults) {
			LOGGER.warn("The query returns to many results " + queryResults.total + " and only the first "
			        + queryResults.maxResults + " results are considered.");
		}
		LOGGER.info("Processing " + queryResults.issues.size() + " issues");

		return queryResults;
	}
}

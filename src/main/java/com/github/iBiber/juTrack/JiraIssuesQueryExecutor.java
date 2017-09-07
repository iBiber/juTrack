package com.github.iBiber.juTrack;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.iBiber.juTrack.data.Credentials;
import com.github.iBiber.juTrack.data.jira.JiraIssuesQueryResults;
import com.github.iBiber.juTrack.util.JiraQuery;

public class JiraIssuesQueryExecutor {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraIssuesQueryExecutor.class);

	private JiraIssuesQueryResults queryResults;
	private final String jiraUrl;

	public JiraIssuesQueryExecutor(String jiraUrl) {
		this.jiraUrl = jiraUrl;
	}

	public JiraIssuesQueryResults getIssues(Credentials credentials, LocalDate startDate, LocalDate endDate) {
		try {
			String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			String url = jiraUrl + "/rest/api/2/search";
			String urlParameter = "jql=assignee was " + credentials.userName // Filter for user
			        + " AND status changed during (\"" + startDateStr + "\",\"" + endDateStr + "\")" // Filter for time range
			        + "&fields=key,summary" // reduce the issue result to the fields "key" and "summary"
			        + "&expand=changelog"; // collect the change log of each issue
			queryResults = new JiraQuery().query(credentials, JiraIssuesQueryResults.class, url, urlParameter);

			if (queryResults.total > queryResults.maxResults) {
				LOGGER.warn("The query returns to many results " + queryResults.total + " and only the first "
				        + queryResults.maxResults + " results are considered.");
			}
			LOGGER.info("Processing " + queryResults.issues.size() + " issues");

			return queryResults;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public JiraIssuesQueryResults getQueryResults() {
		return queryResults;
	}
}

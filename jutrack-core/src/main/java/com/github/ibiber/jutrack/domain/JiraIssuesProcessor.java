package com.github.ibiber.jutrack.domain;

import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.domain.data.jira.History;
import com.github.ibiber.jutrack.domain.data.jira.HistoryItem;
import com.github.ibiber.jutrack.domain.data.jira.Issue;
import com.github.ibiber.jutrack.external.GetIssueResultItemPresenter;
import com.github.ibiber.jutrack.external.JiraIssuesProcessorService;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem;

@Component
public class JiraIssuesProcessor implements JiraIssuesProcessorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraIssuesProcessor.class);

	private JiraIssuesQueryExecutor queryExecutor;
	private IssuesFilter<JiraQueryResultItem> filter;

	@Autowired
	public JiraIssuesProcessor(JiraIssuesQueryExecutor queryExecutor, IssuesFilter<JiraQueryResultItem> filter) {
		this.queryExecutor = queryExecutor;
		this.filter = filter;
	}

	@Override
	public void getIssues(JiraQueryParmeter parameter, GetIssueResultItemPresenter presenter) {
		LOGGER.info("Get issues: " + parameter);
		String userName = parameter.credentials.userName;

		// Query Jira
		List<Issue> issues = queryExecutor.getIssues(parameter.jiraRootUrl, parameter.credentials, parameter.startDate,
		        parameter.endDate).issues;

		// Filter and transform query result
		List<JiraQueryResultItem> resultList = filter.execute(userName, issues, parameter.startDate, parameter.endDate,
		        this::mapStateChangeItem);

		// Print result
		Stream<JiraQueryResultItem> sorted = resultList.stream().sorted((o1, o2) -> o1.created.compareTo(o2.created));
		presenter.presentResults(parameter, sorted);
	}

	private JiraQueryResultItem mapStateChangeItem(Issue issue, History history, HistoryItem historyItem,
	        String itemType) {
		return new JiraQueryResultItem(history.getDateTime(), issue.key, issue.getSummary(),
		        itemType + ": " + historyItem.toString);
	}
}

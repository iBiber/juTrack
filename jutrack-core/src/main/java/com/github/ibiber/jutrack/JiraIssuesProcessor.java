package com.github.ibiber.jutrack;

import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.data.jira.History;
import com.github.ibiber.jutrack.data.jira.HistoryItem;
import com.github.ibiber.jutrack.data.jira.Issue;

@Component
public class JiraIssuesProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraIssuesProcessor.class);

	private JiraIssuesQueryExecutor queryExecutor;
	private IssuesFilter<GetIssueResultItem> filter;
	private GetIssueResultItemPresenter presenter;

	@Autowired
	public JiraIssuesProcessor(JiraIssuesQueryExecutor queryExecutor, IssuesFilter<GetIssueResultItem> filter,
	        GetIssueResultItemPresenter presenter) {
		this.queryExecutor = queryExecutor;
		this.filter = filter;
		this.presenter = presenter;
	}

	public void getIssues(JiraQueryParmeter parameter) {
		LOGGER.info("Get issues: " + parameter);
		String userName = parameter.credentials.userName;

		// Query Jira
		List<Issue> issues = queryExecutor.getIssues(parameter.jiraRootUrl, parameter.credentials, parameter.startDate,
		        parameter.endDate).issues;

		// Filter and transform query result
		List<GetIssueResultItem> resultList = filter.execute(userName, issues, parameter.startDate, parameter.endDate,
		        this::mapStateChangeItem);

		// Print result
		Stream<GetIssueResultItem> sorted = resultList.stream().sorted((o1, o2) -> o1.created.compareTo(o2.created));
		presenter.presentResults(parameter, sorted);
	}

	private GetIssueResultItem mapStateChangeItem(Issue issue, History history, HistoryItem historyItem,
	        String itemType) {
		return new GetIssueResultItem(history.getDateTime(), issue.key, issue.getSummary(),
		        itemType + ": " + historyItem.toString);
	}
}

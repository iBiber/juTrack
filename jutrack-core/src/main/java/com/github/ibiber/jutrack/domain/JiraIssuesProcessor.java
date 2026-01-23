package com.github.ibiber.jutrack.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.domain.data.jira.History;
import com.github.ibiber.jutrack.domain.data.jira.HistoryItem;
import com.github.ibiber.jutrack.domain.data.jira.Issue;
import com.github.ibiber.jutrack.domain.data.jira.WorklogEntry;
import com.github.ibiber.jutrack.external.GetIssueResultItemPresenter;
import com.github.ibiber.jutrack.external.JiraIssuesProcessorService;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem.EntryType;

@Component
public class JiraIssuesProcessor implements JiraIssuesProcessorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraIssuesProcessor.class);

	private JiraIssuesQueryExecutor queryExecutor;
	private IssuesFilter<JiraQueryResultItem> filter;
        private JiraWorklogQueryExecutor worklogQueryExecutor;

	@Autowired
	public JiraIssuesProcessor(JiraIssuesQueryExecutor queryExecutor, IssuesFilter<JiraQueryResultItem> filter,
	        JiraWorklogQueryExecutor worklogQueryExecutor) {
		this.queryExecutor = queryExecutor;
		this.filter = filter;
		this.worklogQueryExecutor = worklogQueryExecutor;
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
		        this::mapHistoryItem);
		issues.forEach(issue -> appendWorklogs(resultList, parameter, issue));

		// Print result
		Stream<JiraQueryResultItem> sorted = resultList.stream().sorted((o1, o2) -> o1.created.compareTo(o2.created));
		presenter.presentResults(parameter, sorted);
	}

	private JiraQueryResultItem mapHistoryItem(Issue issue, History history, HistoryItem historyItem,
	        String itemType) {
		if ("new state".equals(itemType)) {
			String from = valueOrPlaceholder(historyItem.fromString);
			String to = valueOrPlaceholder(historyItem.toString);
			String label = String.format("Status: %s → %s", from, to);
			return JiraQueryResultItem.status(history.getDateTime(), issue.key, issue.getSummary(), label, from, to);
		}

		EntryType type = "attachment".equals(itemType) ? EntryType.ATTACHMENT : EntryType.COMMENT;
		return JiraQueryResultItem.audit(history.getDateTime(), issue.key, issue.getSummary(),
		        itemType + ": " + historyItem.toString, type);
	}

	private void appendWorklogs(List<JiraQueryResultItem> resultList, JiraQueryParmeter parameter, Issue issue) {
		worklogQueryExecutor.getWorklogs(parameter.jiraRootUrl, parameter.credentials, issue.key).worklogs.stream()
		        .filter(entry -> entry.author != null && parameter.credentials.userName.equals(entry.author.name))
		        .filter(entry -> isWithinRange(entry, parameter)).map(entry -> mapWorklogItem(issue, entry))
		        .forEach(resultList::add);
	}

	private boolean isWithinRange(WorklogEntry entry, JiraQueryParmeter parameter) {
		LocalDateTime started = entry.getStartedDateTime();
		return !started.isBefore(parameter.startDate.atStartOfDay())
		        && !started.isAfter(parameter.endDate.atTime(LocalTime.MAX));
	}

	private JiraQueryResultItem mapWorklogItem(Issue issue, WorklogEntry entry) {
		String hoursLabel = formatHours(entry.getLoggedHours());
		String label = "Worklog: " + hoursLabel;
		return JiraQueryResultItem.worklog(entry.getStartedDateTime(), issue.key, issue.getSummary(), label,
		        entry.getLoggedHours());
	}

	private String formatHours(double hours) {
		return hours >= 10 ? String.format("%.0fh", hours) : String.format("%.2fh", hours);
	}

	private String valueOrPlaceholder(String value) {
		return value == null || value.isBlank() ? "n/a" : value;
	}
}

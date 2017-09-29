package com.github.ibiber.jutrack;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.jira.History;
import com.github.ibiber.jutrack.data.jira.HistoryItem;
import com.github.ibiber.jutrack.data.jira.Issue;

/**
 * Filters the given {@link Issue} list for the given <code>userName</code>.
 */
@Component
public class IssuesFilter<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(IssuesFilter.class);

	public interface IssueTransformer<T> {
		public T transformHistoryItem(Issue issue, History history, HistoryItem hItem, String hItemType);
	}

	public List<T> execute(String userName, List<Issue> issues, LocalDate startDate, LocalDate endDate,
	        IssueTransformer<T> transformer) {
		LOGGER.info("Transform change log items");
		List<T> resultList = new ArrayList<>();

		issues.forEach(
		        issue -> issue.changelog.histories.stream().filter(history -> filterUserAndDate(userName, startDate, endDate, history))
		                .forEach(history -> processUserHistory(resultList, issue, history, transformer)));

		LOGGER.info("Transformed " + resultList.size() + " change log items");
		return resultList;
	}

	protected boolean filterUserAndDate(String userName, LocalDate startDate, LocalDate endDate, History history) {
		LocalDateTime dateTime = history.getDateTime();
		return userName.equals(history.author.name) //
		        && dateTime.isAfter(startDate.atStartOfDay()) //
		        && dateTime.isBefore(endDate.atTime(LocalTime.MAX));
	}

	protected void processUserHistory(List<T> resultList, Issue issue, History history,
	        IssueTransformer<T> transformer) {
		history.items.stream().filter(item -> "status".equals(item.field))
		        .map(item -> transformer.transformHistoryItem(issue, history, item, "new state"))
		        .forEach(resultItem -> resultList.add(resultItem));

		history.items.stream().filter(item -> "Attachment".equals(item.field))
		        .map(item -> transformer.transformHistoryItem(issue, history, item, "attachment"))
		        .forEach(resultItem -> resultList.add(resultItem));

		history.items.stream().filter(item -> "Comment".equals(item.field))
		        .map(item -> transformer.transformHistoryItem(issue, history, item, "comment"))
		        .forEach(resultItem -> resultList.add(resultItem));
	}
}

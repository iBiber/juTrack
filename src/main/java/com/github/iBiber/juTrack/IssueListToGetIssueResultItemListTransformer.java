package com.github.iBiber.juTrack;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.iBiber.juTrack.data.GetIssueResultItem;
import com.github.iBiber.juTrack.data.jira.History;
import com.github.iBiber.juTrack.data.jira.HistoryItem;
import com.github.iBiber.juTrack.data.jira.Issue;

/**
 * Filters the given {@link Issue} list for the given <code>userName</code> and the resulting list is transformed to a
 * list of {@link GetIssueResultItem}.
 */
@Component
public class IssueListToGetIssueResultItemListTransformer {
	private static final Logger LOGGER = LoggerFactory.getLogger(IssueListToGetIssueResultItemListTransformer.class);

	public List<GetIssueResultItem> execute(String userName, List<Issue> issues) {
		LOGGER.info("Transform change log items");
		List<GetIssueResultItem> resultList = new ArrayList<>();

		issues.forEach(
		        issue -> issue.changelog.histories.stream().filter(history -> userName.equals(history.author.name))
		                .forEach(history -> processUserHistory(resultList, issue, history)));

		LOGGER.info("Transformed " + resultList.size() + " change log items");
		return resultList;
	}

	private void processUserHistory(List<GetIssueResultItem> resultList, Issue issue, History history) {
		history.items.stream().filter(item -> "status".equals(item.field))
		        .map(item -> mapStateChangeItem(issue, history, item, "new state"))
		        .forEach(resultItem -> resultList.add(resultItem));

		history.items.stream().filter(item -> "Attachment".equals(item.field))
		        .map(item -> mapStateChangeItem(issue, history, item, "attachment"))
		        .forEach(resultItem -> resultList.add(resultItem));

		history.items.stream().filter(item -> "Comment".equals(item.field))
		        .map(item -> mapStateChangeItem(issue, history, item, "comment"))
		        .forEach(resultItem -> resultList.add(resultItem));
	}

	private GetIssueResultItem mapStateChangeItem(Issue issue, History history, HistoryItem historyItem,
	        String itemType) {
		return new GetIssueResultItem(history.created, issue.key, issue.getSummary(),
		        itemType + ": " + historyItem.toString);
	}
}

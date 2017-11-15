package com.github.ibiber.jutrack.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.domain.data.jira.History;
import com.github.ibiber.jutrack.domain.data.jira.Issue;
import com.github.ibiber.jutrack.domain.data.jira.WorklogItem;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;

/**
 * Filters the given {@link Issue} list for the given <code>userName</code> for worklogs and query them from jira.
 */
@Component
public class WorklogCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorklogCollector.class);

    @Autowired
    private WorklogQueryExecutor queryExecutor;

    private class WorklogQueryParameter {
        final String issue;
        final String worklogId;

        public WorklogQueryParameter(String issue, String worklogId) {
            this.issue = issue;
            this.worklogId = worklogId;
        }
    }

    public List<WorklogItem> execute(JiraQueryParmeter parameter, List<Issue> issues, LocalDate startDate,
            LocalDate endDate) {
        LOGGER.info("Transform change log items");
        List<WorklogQueryParameter> resultList = new ArrayList<>();

        // Grab worklog IDs from changelog
        issues.forEach(issue -> issue.changelog.histories.stream()
                .filter(history -> filterUserAndDate(parameter.credentials.userName, startDate, endDate, history))
                .forEach(history -> processUserHistory(resultList, issue, history)));

        LOGGER.info("Found " + resultList.size() + " worklog IDs");

        // Collect worklog information
        List<WorklogItem> result = resultList.stream()
                .map(queryParameter -> queryExecutor.getWorklog(parameter.jiraRootUrl, parameter.credentials,
                        queryParameter.issue, queryParameter.worklogId))
                .collect(Collectors.toList());

        LOGGER.info("Transformed " + result.size() + " work log items");
        return result;
    }

    protected boolean filterUserAndDate(String userName, LocalDate startDate, LocalDate endDate, History history) {
        LocalDateTime dateTime = history.getDateTime();
        return userName.equals(history.author.name) //
                && dateTime.isAfter(startDate.atStartOfDay()) //
                && dateTime.isBefore(endDate.atTime(LocalTime.MAX));
    }

    protected void processUserHistory(List<WorklogQueryParameter> resultList, Issue issue, History history) {
        history.items.stream().filter(item -> "WorklogId".equals(item.field))
                .map(item -> new WorklogQueryParameter(issue.key, item.from))
                .forEach(resultItem -> resultList.add(resultItem));
    }
}

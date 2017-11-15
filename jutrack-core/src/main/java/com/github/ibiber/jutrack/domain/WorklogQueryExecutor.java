package com.github.ibiber.jutrack.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.ibiber.common.http.RestServiceQuery;
import com.github.ibiber.common.http.UrlParameterBuilder;
import com.github.ibiber.jutrack.domain.data.jira.WorklogItem;
import com.github.ibiber.jutrack.external.data.Credentials;

@Component
public class WorklogQueryExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorklogQueryExecutor.class);

    private RestServiceQuery restServiceQuery;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class WorklogQueryResult {
        public final long timeSpentSeconds;
        public final String dateStarted;

        /** Constructor needed by framework */
        public WorklogQueryResult() {
            this(0, null);
        }

        public WorklogQueryResult(long timeSpentSeconds, String dateStarted) {
            this.timeSpentSeconds = timeSpentSeconds;
            this.dateStarted = dateStarted;
        }
    }

    @Autowired
    public WorklogQueryExecutor(RestServiceQuery restServiceQuery) {
        this.restServiceQuery = restServiceQuery;
    }

    public WorklogItem getWorklog(String jiraUrl, Credentials credentials, String issue, String worklogId) {

        String url = jiraUrl + "/rest/tempo-timesheets/3/worklogs/" + worklogId;

        WorklogQueryResult queryResults = restServiceQuery.httpGetQueryBasicAuthorization(WorklogQueryResult.class, url,
                new UrlParameterBuilder(), credentials.userName, credentials.password);

        WorklogItem worklogItem = new WorklogItem(issue, worklogId, queryResults.timeSpentSeconds,
                queryResults.dateStarted);
        LOGGER.info(worklogItem.toString());
        return worklogItem;
    }
}

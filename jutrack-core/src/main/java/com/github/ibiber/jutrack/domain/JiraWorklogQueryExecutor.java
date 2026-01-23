package com.github.ibiber.jutrack.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.ibiber.common.http.RestServiceQuery;
import com.github.ibiber.jutrack.domain.data.jira.WorklogResponse;
import com.github.ibiber.jutrack.external.data.Credentials;

@Component
public class JiraWorklogQueryExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraWorklogQueryExecutor.class);

    private final RestServiceQuery restServiceQuery;

    public JiraWorklogQueryExecutor(RestServiceQuery restServiceQuery) {
        this.restServiceQuery = restServiceQuery;
    }

    public WorklogResponse getWorklogs(String jiraUrl, Credentials credentials, String issueKey) {
        String url = jiraUrl + "/rest/api/2/issue/" + issueKey + "/worklog";
        LOGGER.info("Fetching worklogs for {}", issueKey);
        WorklogResponse response = restServiceQuery.httpGetBasicAuthorization(WorklogResponse.class, url,
                credentials.userName, credentials.password);
        return response == null ? new WorklogResponse() : response;
    }
}

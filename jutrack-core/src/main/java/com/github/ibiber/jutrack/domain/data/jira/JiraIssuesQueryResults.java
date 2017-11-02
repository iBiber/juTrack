package com.github.ibiber.jutrack.domain.data.jira;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssuesQueryResults {
    public final int startAt;
    public final int maxResults;
    public final int total;
    public final List<Issue> issues;

    /** Constructor needed by framework */
    JiraIssuesQueryResults() {
        this(-1, -1, -1, null);
    }

    public JiraIssuesQueryResults(int startAt, int maxResults, int total, List<Issue> issues) {
        super();
        this.startAt = startAt;
        this.maxResults = maxResults;
        this.total = total;
        this.issues = issues;
    }

    @Override
    public String toString() {
        return "JiraIssues [startAt=" + startAt + ", maxResults=" + maxResults + ", total=" + total + ", issues="
                + issues + "]";
    }
}

package com.github.ibiber.jutrack.web;

import java.util.List;

public class IssueTimelineRowView {
    private final String issueKey;
    private final String issueUrl;
    private final String summary;
    private final List<String> statuses;

    public IssueTimelineRowView(String issueKey, String issueUrl, String summary, List<String> statuses) {
        this.issueKey = issueKey;
        this.issueUrl = issueUrl;
        this.summary = summary;
        this.statuses = List.copyOf(statuses);
    }

    public String getIssueKey() {
        return issueKey;
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getStatuses() {
        return statuses;
    }
}

package com.github.ibiber.jutrack.web;

import java.util.List;

public class IssueTimelineRowView {
    private final String issueKey;
    private final String issueUrl;
    private final String summary;
    private final List<TimelineCellView> cells;

    public IssueTimelineRowView(String issueKey, String issueUrl, String summary, List<TimelineCellView> cells) {
        this.issueKey = issueKey;
        this.issueUrl = issueUrl;
        this.summary = summary;
        this.cells = List.copyOf(cells);
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

    public List<TimelineCellView> getCells() {
        return cells;
    }
}

package com.github.ibiber.jutrack.web;

import java.time.LocalDateTime;

public class ResultEntryView {
    private final LocalDateTime created;
    private final String createdDisplay;
    private final String issueKey;
    private final String issueUrl;
    private final String summary;
    private final String changeAction;

    ResultEntryView(LocalDateTime created, String createdDisplay, String issueKey, String issueUrl, String summary,
            String changeAction) {
        this.created = created;
        this.createdDisplay = createdDisplay;
        this.issueKey = issueKey;
        this.issueUrl = issueUrl;
        this.summary = summary;
        this.changeAction = changeAction;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getCreatedDisplay() {
        return createdDisplay;
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

    public String getChangeAction() {
        return changeAction;
    }

    public String asPlainTextRow() {
        return createdDisplay + "\t" + issueKey + "\t" + changeAction + "\t" + summary + "\t" + issueUrl;
    }
}

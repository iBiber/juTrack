package com.github.ibiber.jutrack.web;

import java.time.LocalDateTime;

import com.github.ibiber.jutrack.external.data.JiraQueryResultItem.EntryType;

public class ResultEntryView {
    private final LocalDateTime created;
    private final String createdDisplay;
    private final String issueKey;
    private final String issueUrl;
    private final String summary;
    private final String changeAction;
    private final EntryType type;
    private final String statusFrom;
    private final String statusTo;
    private final double worklogHours;

    ResultEntryView(LocalDateTime created, String createdDisplay, String issueKey, String issueUrl, String summary,
            String changeAction, EntryType type, String statusFrom, String statusTo, double worklogHours) {
        this.created = created;
        this.createdDisplay = createdDisplay;
        this.issueKey = issueKey;
        this.issueUrl = issueUrl;
        this.summary = summary;
        this.changeAction = changeAction;
        this.type = type;
        this.statusFrom = statusFrom;
        this.statusTo = statusTo;
        this.worklogHours = worklogHours;
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

    public EntryType getType() {
        return type;
    }

    public boolean isStatusChange() {
        return type == EntryType.STATUS;
    }

    public boolean isWorklog() {
        return type == EntryType.WORKLOG;
    }

    public String getStatusFrom() {
        return statusFrom;
    }

    public String getStatusTo() {
        return statusTo;
    }

    public double getWorklogHours() {
        return worklogHours;
    }

    public String formattedTransition() {
        if (!isStatusChange()) {
            return "";
        }
        String from = statusFrom == null ? "n/a" : statusFrom;
        String to = statusTo == null ? "n/a" : statusTo;
        return from + " → " + to;
    }

    public String asPlainTextRow() {
        StringBuilder builder = new StringBuilder()
                .append(createdDisplay).append("\t")
                .append(issueKey).append("\t")
                .append(changeAction).append("\t")
                .append(summary).append("\t")
                .append(issueUrl);
        if (isWorklog()) {
            builder.append("\t").append(String.format("%.2fh", worklogHours));
        }
        return builder.toString();
    }
}

package com.github.ibiber.jutrack.domain.data.jira;

public class WorklogItem {
    final String issue;
    final String worklogId;
    final long timeSpentSeconds;
    final String dateStarted;

    public WorklogItem(String issue, String worklogId, long timeSpentSeconds, String dateStarted) {
        super();
        this.issue = issue;
        this.worklogId = worklogId;
        this.timeSpentSeconds = timeSpentSeconds;
        this.dateStarted = dateStarted;
    }

    @Override
    public String toString() {
        return "WorklogItem [issue=" + issue + ", worklogId=" + worklogId + ", timeSpentSeconds=" + timeSpentSeconds
                + ", dateStarted=" + dateStarted + "]";
    }

}

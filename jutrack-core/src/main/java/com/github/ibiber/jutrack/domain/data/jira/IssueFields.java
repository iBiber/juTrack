package com.github.ibiber.jutrack.domain.data.jira;

public class IssueFields {
    public final String summary;

    /** Constructor needed by framework */
    IssueFields() {
        this(null);
    }

    public IssueFields(String summary) {
        this.summary = summary;
    }
}
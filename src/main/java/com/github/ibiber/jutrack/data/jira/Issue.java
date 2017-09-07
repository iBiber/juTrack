package com.github.ibiber.jutrack.data.jira;

public class Issue {
    public final String self;
    public final String key;
    public final IssueFields fields;
    public final Changelog changelog;

    /** Constructor needed by framework */
    Issue() {
        this(null, null, null, null);
    }

    public Issue(String self, String key, IssueFields fields, Changelog changelog) {
        super();
        this.self = self;
        this.key = key;
        this.fields = fields;
        this.changelog = changelog;
    }

    public String getSummary() {
        return fields.summary;
    }

    @Override
    public String toString() {
        return "JiraIssuesQueryResult [self=" + self + ", key=" + key + ", summary=" + fields.summary + ", changelog="
                + changelog + "]";
    }
}

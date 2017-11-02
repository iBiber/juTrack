package com.github.ibiber.jutrack.domain.data.jira;

public class HistoryItem {
    public final String field;
    public final String fieldtype;
    public final String from;
    public final String fromString;
    public final String to;
    public final String toString;

    /** Constructor needed by framework */
    HistoryItem() {
        this(null, null, null, null, null, null);
    }

    public HistoryItem(String field, String fieldtype, String from, String fromString, String to, String toString) {
        super();
        this.field = field;
        this.fieldtype = fieldtype;
        this.from = from;
        this.fromString = fromString;
        this.to = to;
        this.toString = toString;
    }

    @Override
    public String toString() {
        return "ChangelogHistoryItem [field=" + field + ", fieldtype=" + fieldtype + ", from=" + from + ", fromString="
                + fromString + ", to=" + to + ", toString=" + toString + "]";
    }

}

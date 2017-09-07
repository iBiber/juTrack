package com.github.iBiber.juTrack.data.jira;

import java.util.List;

public class Changelog {
    public final int startAt;
    public final int maxResults;
    public final int total;
    public final List<History> histories;

    /** Constructor needed by framework */
    Changelog() {
        this(-1, -1, -1, null);
    }

    public Changelog(int startAt, int maxResults, int total, List<History> histories) {
        super();
        this.startAt = startAt;
        this.maxResults = maxResults;
        this.total = total;
        this.histories = histories;
    }

    @Override
    public String toString() {
        return "Changelog [startAt=" + startAt + ", maxResults=" + maxResults + ", total=" + total + ", histories="
                + histories + "]";
    }

}

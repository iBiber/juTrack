package com.github.ibiber.jutrack.domain.data.jira;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorklogResponse {
    public final int startAt;
    public final int maxResults;
    public final int total;
    public final List<WorklogEntry> worklogs;

    public WorklogResponse() {
        this(0, 0, 0, Collections.emptyList());
    }

    public WorklogResponse(int startAt, int maxResults, int total, List<WorklogEntry> worklogs) {
        this.startAt = startAt;
        this.maxResults = maxResults;
        this.total = total;
        this.worklogs = worklogs == null ? Collections.emptyList() : List.copyOf(worklogs);
    }
}

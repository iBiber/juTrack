package com.github.ibiber.jutrack.domain.data.jira;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorklogEntry {
    public final String id;
    public final Author author;
    public final String started;
    public final int timeSpentSeconds;

    WorklogEntry() {
        this(null, null, null, 0);
    }

    public WorklogEntry(String id, Author author, String started, int timeSpentSeconds) {
        this.id = id;
        this.author = author;
        this.started = started;
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public LocalDateTime getStartedDateTime() {
        return LocalDateTime.parse(started, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
    }

    public double getLoggedHours() {
        return timeSpentSeconds / 3600d;
    }
}

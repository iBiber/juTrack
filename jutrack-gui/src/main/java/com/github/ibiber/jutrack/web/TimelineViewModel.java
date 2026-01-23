package com.github.ibiber.jutrack.web;

import java.util.Collections;
import java.util.List;

public class TimelineViewModel {
    private final List<TimelineDayView> days;
    private final List<IssueTimelineRowView> rows;

    public TimelineViewModel(List<TimelineDayView> days, List<IssueTimelineRowView> rows) {
        this.days = List.copyOf(days);
        this.rows = List.copyOf(rows);
    }

    public static TimelineViewModel empty() {
        return new TimelineViewModel(Collections.emptyList(), Collections.emptyList());
    }

    public List<TimelineDayView> getDays() {
        return days;
    }

    public List<IssueTimelineRowView> getRows() {
        return rows;
    }

    public boolean hasRows() {
        return !rows.isEmpty();
    }
}

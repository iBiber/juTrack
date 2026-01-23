package com.github.ibiber.jutrack.web;

import java.util.Collections;
import java.util.List;

public class ResultViewModel {
    private final List<ResultEntryView> entries;
    private final String textView;
    private final TimelineViewModel timeline;

    public ResultViewModel(List<ResultEntryView> entries, String textView, TimelineViewModel timeline) {
        this.entries = Collections.unmodifiableList(entries);
        this.textView = textView;
        this.timeline = timeline;
    }

    public static ResultViewModel empty() {
        return new ResultViewModel(Collections.emptyList(), "", TimelineViewModel.empty());
    }

    public List<ResultEntryView> getEntries() {
        return entries;
    }

    public String getTextView() {
        return textView;
    }

    public TimelineViewModel getTimeline() {
        return timeline;
    }

    public boolean hasResults() {
        return !entries.isEmpty();
    }

    public int getTotalEntries() {
        return entries.size();
    }
}

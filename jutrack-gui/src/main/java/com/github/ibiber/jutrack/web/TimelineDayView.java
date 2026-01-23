package com.github.ibiber.jutrack.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimelineDayView {
    private static final DateTimeFormatter LABEL_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    private final LocalDate date;
    private final String label;

    public TimelineDayView(LocalDate date) {
        this.date = date;
        this.label = LABEL_FORMATTER.format(date);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLabel() {
        return label;
    }
}

package com.github.ibiber.jutrack.web;

import java.text.DecimalFormat;
import java.util.List;

public class TimelineCellView {
    private static final DecimalFormat HOURS_FORMAT = new DecimalFormat("0.##h");

    private final List<String> statusChanges;
    private final double loggedHours;

    public TimelineCellView(List<String> statusChanges, double loggedHours) {
        this.statusChanges = List.copyOf(statusChanges);
        this.loggedHours = loggedHours;
    }

    public List<String> getStatusChanges() {
        return statusChanges;
    }

    public boolean hasStatuses() {
        return !statusChanges.isEmpty();
    }

    public double getLoggedHours() {
        return loggedHours;
    }

    public boolean hasWorklog() {
        return loggedHours > 0;
    }

    public String getHoursDisplay() {
        return hasWorklog() ? HOURS_FORMAT.format(loggedHours) : "";
    }
}

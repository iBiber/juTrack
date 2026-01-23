package com.github.ibiber.jutrack.web;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.github.ibiber.jutrack.external.DefaultValueProviderService;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JiraQueryForm {

    @NotBlank(message = "Jira root URL is required")
    private String jiraRootUrl;

    @NotBlank(message = "User name is required")
    private String userName;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    public static JiraQueryForm withDefaults(DefaultValueProviderService defaults, Clock clock) {
        JiraQueryForm form = new JiraQueryForm();
        form.jiraRootUrl = nullToEmpty(defaults.getJiraRootUrl());
        form.userName = nullToEmpty(defaults.getUserName());
        form.password = "";

        LocalDate today = LocalDate.now(clock);
        int dayRange = Math.max(1, defaults.dayRange());
        form.endDate = today;
        form.startDate = today.minusDays(dayRange);
        return form;
    }

    public JiraQueryParmeter toParameter() {
        return new JiraQueryParmeter(jiraRootUrl.trim(), userName.trim(), password, startDate, endDate);
    }

    @AssertTrue(message = "End date must not be before start date")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public String getJiraRootUrl() {
        return jiraRootUrl;
    }

    public void setJiraRootUrl(String jiraRootUrl) {
        this.jiraRootUrl = jiraRootUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

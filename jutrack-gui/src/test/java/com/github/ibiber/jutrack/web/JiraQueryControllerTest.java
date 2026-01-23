package com.github.ibiber.jutrack.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.github.ibiber.jutrack.external.DefaultValueProviderService;

@WebMvcTest(JiraQueryController.class)
class JiraQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefaultValueProviderService defaultValueProviderService;

    @MockBean
    private QueryResultService queryResultService;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        when(defaultValueProviderService.getJiraRootUrl()).thenReturn("https://jira.example.com");
        when(defaultValueProviderService.getUserName()).thenReturn("user@example.com");
        when(defaultValueProviderService.dayRange()).thenReturn(3);

        when(clock.instant()).thenReturn(Instant.parse("2026-01-10T10:15:30Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    void shouldRenderFormWithDefaults() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("juTrack")))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    void shouldReturnValidationErrors() throws Exception {
        mockMvc.perform(post("/query")
                .param("jiraRootUrl", "")
                .param("userName", "")
                .param("password", "")
                .param("startDate", "2026-01-11")
                .param("endDate", "2026-01-10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("form", "jiraRootUrl", "userName", "password"))
                .andExpect(model().attributeHasFieldErrors("form"))
                .andExpect(content().string(containsString("End date must not be before start date")));
    }

    @Test
    void shouldRenderResultsWhenSubmissionIsValid() throws Exception {
        ResultEntryView entry = new ResultEntryView(LocalDateTime.of(2026, 1, 10, 12, 0),
                "10 Jan 2026, 12:00", "ABC-123", "https://jira.example.com/browse/ABC-123", "Demo summary",
                "Transition to In Progress");
        when(queryResultService.fetch(any())).thenReturn(new ResultViewModel(Collections.singletonList(entry),
                entry.asPlainTextRow()));

        mockMvc.perform(post("/query")
                .param("jiraRootUrl", "https://jira.example.com")
                .param("userName", "user")
                .param("password", "secret")
                .param("startDate", "2026-01-01")
                .param("endDate", "2026-01-05"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("resultView"))
                .andExpect(content().string(containsString("ABC-123")));
    }
}

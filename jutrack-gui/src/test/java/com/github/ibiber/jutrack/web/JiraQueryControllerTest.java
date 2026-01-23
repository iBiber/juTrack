package com.github.ibiber.jutrack.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.ibiber.jutrack.external.DefaultValueProviderService;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem.EntryType;

@ExtendWith(MockitoExtension.class)
class JiraQueryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @Mock
    private QueryResultService queryResultService;

    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        JiraQueryController controller = new JiraQueryController(defaultValueProviderService, queryResultService, clock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

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
            .andExpect(model().attributeExists("form"))
            .andExpect(model().attribute("queryExecuted", false))
            .andExpect(view().name("index"));
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
            .andExpect(model().attribute("queryExecuted", false))
            .andExpect(view().name("index"));
    }

    @Test
    void shouldRenderResultsWhenSubmissionIsValid() throws Exception {
        ResultEntryView entry = new ResultEntryView(LocalDateTime.of(2026, 1, 10, 12, 0),
            "10 Jan 2026, 12:00", "ABC-123", "https://jira.example.com/browse/ABC-123", "Demo summary",
            "Transition to In Progress", EntryType.STATUS, "To Do", "In Progress", 0d);
        when(queryResultService.fetch(any())).thenReturn(new ResultViewModel(Collections.singletonList(entry),
            entry.asPlainTextRow(), TimelineViewModel.empty()));

        mockMvc.perform(post("/query")
                .param("jiraRootUrl", "https://jira.example.com")
                .param("userName", "user")
                .param("password", "secret")
                .param("startDate", "2026-01-01")
                .param("endDate", "2026-01-05"))
                .andExpect(status().isOk())
            .andExpect(model().attributeExists("resultView"))
            .andExpect(model().attribute("queryExecuted", true))
            .andExpect(view().name("index"));
    }
}

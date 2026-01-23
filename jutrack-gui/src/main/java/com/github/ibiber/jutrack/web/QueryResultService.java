package com.github.ibiber.jutrack.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.external.GetIssueResultItemPresenter;
import com.github.ibiber.jutrack.external.JiraIssuesProcessorService;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem;

@Component
public class QueryResultService {

    private final JiraIssuesProcessorService jiraIssuesProcessor;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    public QueryResultService(JiraIssuesProcessorService jiraIssuesProcessor) {
        this.jiraIssuesProcessor = jiraIssuesProcessor;
    }

    public ResultViewModel fetch(JiraQueryParmeter parameter) {
        CollectingPresenter presenter = new CollectingPresenter(parameter);
        jiraIssuesProcessor.getIssues(parameter, presenter);
        return presenter.asModel();
    }

    private class CollectingPresenter implements GetIssueResultItemPresenter {
        private final JiraQueryParmeter parameter;
        private final List<ResultEntryView> entries = new ArrayList<>();

        CollectingPresenter(JiraQueryParmeter parameter) {
            this.parameter = parameter;
        }

        @Override
        public void presentResults(JiraQueryParmeter ignored, Stream<JiraQueryResultItem> resultStream) {
            resultStream.forEach(item -> entries.add(mapItem(item)));
        }

        private ResultEntryView mapItem(JiraQueryResultItem item) {
            String issueUrl = parameter.jiraRootUrl + "/browse/" + item.key;
            return new ResultEntryView(item.created, item.created.format(dateTimeFormatter), item.key, issueUrl,
                    item.summary, item.changeAction);
        }

        ResultViewModel asModel() {
            List<ResultEntryView> snapshot = new ArrayList<>(entries);
            String textView = snapshot.stream().map(ResultEntryView::asPlainTextRow).collect(Collectors.joining("\n"));
            TimelineViewModel timeline = buildTimeline(snapshot);
            return new ResultViewModel(snapshot, textView, timeline);
        }

        private TimelineViewModel buildTimeline(List<ResultEntryView> snapshot) {
            LocalDate startDate = parameter.startDate;
            LocalDate endDate = parameter.endDate;
            if (snapshot.isEmpty() || startDate == null || endDate == null || endDate.isBefore(startDate)) {
                return TimelineViewModel.empty();
            }

            List<TimelineDayView> days = new ArrayList<>();
            for (LocalDate cursor = startDate; !cursor.isAfter(endDate); cursor = cursor.plusDays(1)) {
                days.add(new TimelineDayView(cursor));
            }
            int dayCount = days.size();
            if (dayCount == 0) {
                return TimelineViewModel.empty();
            }

            Map<String, IssueTimelineRowBuilder> perIssue = new LinkedHashMap<>();
            snapshot.stream().sorted(Comparator.comparing(ResultEntryView::getCreated)).forEach(entry -> {
                LocalDate entryDate = entry.getCreated().toLocalDate();
                if (entryDate.isBefore(startDate) || entryDate.isAfter(endDate)) {
                    return;
                }

                int dayIndex = (int) ChronoUnit.DAYS.between(startDate, entryDate);
                IssueTimelineRowBuilder builder = perIssue.computeIfAbsent(entry.getIssueKey(), key ->
                        new IssueTimelineRowBuilder(entry.getIssueKey(), entry.getIssueUrl(), entry.getSummary(),
                                dayCount));
                builder.addStatus(dayIndex, entry.getChangeAction());
            });

            if (perIssue.isEmpty()) {
                return TimelineViewModel.empty();
            }

            List<IssueTimelineRowView> rows = perIssue.values().stream()
                    .map(IssueTimelineRowBuilder::build)
                    .collect(Collectors.toList());

            return new TimelineViewModel(days, rows);
        }

        private final class IssueTimelineRowBuilder {
            private final String issueKey;
            private final String issueUrl;
            private final String summary;
            private final List<List<String>> statusesByDay;

            private IssueTimelineRowBuilder(String issueKey, String issueUrl, String summary, int dayCount) {
                this.issueKey = issueKey;
                this.issueUrl = issueUrl;
                this.summary = summary;
                this.statusesByDay = new ArrayList<>(dayCount);
                for (int i = 0; i < dayCount; i++) {
                    this.statusesByDay.add(new ArrayList<>());
                }
            }

            private void addStatus(int dayIndex, String status) {
                if (dayIndex >= 0 && dayIndex < statusesByDay.size()) {
                    statusesByDay.get(dayIndex).add(status);
                }
            }

            private IssueTimelineRowView build() {
                List<String> statuses = statusesByDay.stream()
                        .map(values -> values.isEmpty() ? "" : String.join("\n", values))
                        .collect(Collectors.toList());
                return new IssueTimelineRowView(issueKey, issueUrl, summary, statuses);
            }
        }
    }
}

package com.github.ibiber.jutrack.web;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
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
            return new ResultViewModel(snapshot, textView);
        }
    }
}

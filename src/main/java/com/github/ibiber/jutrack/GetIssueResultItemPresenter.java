package com.github.ibiber.jutrack;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.GetIssuesParmeter;

@Component
public class GetIssueResultItemPresenter {
	public void presentResults(GetIssuesParmeter parameter, Stream<GetIssueResultItem> resultStream) {
		resultStream.forEach(item -> System.out
		        .println(item.getDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "\t"
		                + item.key + "\t" + item.changeAction + "\t" + item.summary + "\t" + parameter.jiraRootUrl
		                + "/browse/" + item.key));
	}
}

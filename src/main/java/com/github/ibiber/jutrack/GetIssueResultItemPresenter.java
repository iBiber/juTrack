package com.github.ibiber.jutrack;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.GetIssuesParmeter;
import com.github.ibiber.jutrack.ui.GetIssuesParameterDialog;

@Component
public class GetIssueResultItemPresenter {
	@Autowired
	private GetIssuesParameterDialog parameterDialog;

	public void presentResults(GetIssuesParmeter parameter, Stream<GetIssueResultItem> resultStream) {
		//		// Print to Console
		//		resultStream.forEach(item -> System.out
		//		        .println(item.getDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "\t"
		//		                + item.key + "\t" + item.changeAction + "\t" + item.summary + "\t" + parameter.jiraRootUrl
		//		                + "/browse/" + item.key));

		// Show result to GUI
		parameterDialog.setResultText(resultStream//
		        .map(item -> item.getDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "\t"
		                + item.key + "\t" + item.changeAction + "\t" + item.summary + "\t" + parameter.jiraRootUrl
		                + "/browse/" + item.key)//
		        .collect(Collectors.joining("\n")));
	}
}

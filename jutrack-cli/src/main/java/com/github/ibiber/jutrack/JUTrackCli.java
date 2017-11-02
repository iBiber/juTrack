package com.github.ibiber.jutrack;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.JiraQueryParmeter;

@SpringBootApplication
public class JUTrackCli {
	@Autowired
	private CommandLineParameterHandler cliHandler;
	@Autowired
	private JiraIssuesProcessor processor;

	@Component
	public class CommandLineClient implements CommandLineRunner, GetIssueResultItemPresenter {
		@Override
		public void run(String... args) throws Exception {
			JiraQueryParmeter parameter = cliHandler.getParameter(args);
			processor.getIssues(parameter);
		}

		@Override
		public void presentResults(JiraQueryParmeter parameter, Stream<GetIssueResultItem> resultStream) {
			// Print to Console
			resultStream.forEach(item -> System.out
			        .println(item.created.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "\t"
			                + item.key + "\t" + item.changeAction + "\t" + item.summary + "\t" + parameter.jiraRootUrl
			                + "/browse/" + item.key));
		}
	}

	public static void main(String args[]) {
		new SpringApplicationBuilder(JUTrackCli.class).web(false).run(args);
	}
}

package com.github.ibiber.jutrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ibiber.common.general.Strings;
import com.github.ibiber.common.general.Throwables;
import com.github.ibiber.jutrack.external.DefaultValueProviderService;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;

@Component
public class CommandLineParameterHandler {
	@Autowired
	private DefaultValueProviderService defaultValue;

	public JiraQueryParmeter getParameter(String... args) {
		JiraQueryParmeter.Builder builder = new JiraQueryParmeter.Builder();

		try {
			builder.jiraRootUrl(readValue("Jira URL root", defaultValue.getJiraRootUrl()));
			builder.userName(readValue("Jira user: ", defaultValue.getUserName()));
			builder.password(readPassword("Jira password: "));

			readValidParameter(() -> builder
			        .startDate(readValue("Start date", LocalDate.now().minusDays(defaultValue.dayRange()).toString())));
			readValidParameter(() -> builder.endDate(readValue("End date", LocalDate.now().toString())));
		} catch (IOException e) {
			Throwables.propagate(e);
		}

		return builder.build();
	}

	@FunctionalInterface
	private interface UserEntryRepeater {
		public void readValidParameter() throws Exception;
	}

	private void readValidParameter(UserEntryRepeater run) {
		boolean validParameter = false;
		do {
			try {
				run.readValidParameter();
				validParameter = true;
			} catch (Exception e) {
				System.out.println("Invalid user entry: " + e.getMessage());
			}
		} while (!validParameter);
	}

	private String readValue(String format, String defaultValue) throws IOException {
		String message = format;
		if (!Strings.isNullOrEmpty(defaultValue)) {
			message += "[" + defaultValue + "]";
		}
		message += ": ";

		String result;
		if (System.console() != null) {
			result = System.console().readLine(message);
		} else {
			// This is a workaround for IDEs where the System.console() is null
			System.out.print(String.format(message));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			result = reader.readLine();
		}

		if (Strings.isNullOrEmpty(result)) {
			result = defaultValue;
		}
		return result;
	}

	private String readPassword(String format) throws IOException {
		if (System.console() != null)
			return new String(System.console().readPassword(format));

		// This is a workaround for IDEs where the System.console() is null
		return this.readValue(format, "");
	}
}

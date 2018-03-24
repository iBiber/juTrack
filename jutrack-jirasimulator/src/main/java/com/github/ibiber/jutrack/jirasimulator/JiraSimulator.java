package com.github.ibiber.jutrack.jirasimulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JiraSimulator {
	/** Regex pattern to find something like this 2017-08-25T21:50:00.484+0200 */
	private static Pattern regexDatePattern = Pattern
	        .compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{4}");
	private static final LocalDateTime lastDateInJson = LocalDateTime.of(2017, 8, 28, 22, 00);

	/** Regex pattern to replace the user_01 with the one from the request */
	private static Pattern regexUserPattern = Pattern.compile("user_01");

	@Autowired
	private CurrentDateTimeProvider currentDateTimeProvider;

	public String updateDate(String date) {
		String result = date;

		Matcher matcher = regexDatePattern.matcher(date);
		if (matcher.find()) {
			String dateString = matcher.group();
			DateTimeFormatter jsonDatePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ");
			LocalDateTime jsonDateTime = LocalDateTime.parse(dateString, jsonDatePattern);
			LocalDateTime newDate = currentDateTimeProvider.currentDateTime()
			        .minusDays(lastDateInJson.getDayOfMonth() - jsonDateTime.getDayOfMonth());

			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			        .append(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))//TODO - Does not work: .appendOffset("+HHMM", "+0000")
			        .toFormatter();
			result = matcher.replaceFirst(newDate.format(formatter) + "+0200");
		}
		return result;
	}

	public String updateUser(String currentLine, String requestUser) {
		String result = currentLine;

		Matcher matcher = regexUserPattern.matcher(result);
		if (matcher.find()) {
			result = matcher.replaceFirst(requestUser);
		}

		return result;
	}

	public String updateData(String currentLine, String requestUser) {
		String result = updateDate(currentLine);

		result = updateUser(result, requestUser);

		return result;
	}

	@RequestMapping("/rest/api/2/search")
	public String search(@RequestParam(name = "jql") String jql) throws IOException {
		String requestUser = getUserOfRequest(jql);
		try (BufferedReader buffer = new BufferedReader(
		        new InputStreamReader(new ClassPathResource("example.json").getInputStream()))) {
			return buffer.lines().map((line) -> updateData(line, requestUser)).collect(Collectors.joining("\n"));
		}
	}

	public String getUserOfRequest(String jql) {
		Pattern pattern = Pattern.compile("assignee was (.*) AND");
		Matcher matcher = pattern.matcher(jql);
		matcher.find();
		return matcher.group(1);
	}

	@RequestMapping(path = "/browse/${issue}")
	public String browse(@PathVariable(name = "issue") String issue) throws IOException {
		return issue;
	}
}

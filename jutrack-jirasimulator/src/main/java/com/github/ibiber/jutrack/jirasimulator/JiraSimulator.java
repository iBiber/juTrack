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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JiraSimulator {
	/** Regex pattern to find something like this 2017-08-25T21:50:00.484+0200 */
	private static Pattern regexDatePattern = Pattern
	        .compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{4}");
	private static final LocalDateTime lastDateInJson = LocalDateTime.of(2017, 8, 28, 22, 00);

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

	@RequestMapping("/rest/api/2/search")
	public String search() throws IOException {
		try (BufferedReader buffer = new BufferedReader(
		        new InputStreamReader(new ClassPathResource("example.json").getInputStream()))) {
			return buffer.lines().map(this::updateDate).collect(Collectors.joining("\n"));
		}
	}
}

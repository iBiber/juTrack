package com.github.ibiber.jutrack;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import com.github.ibiber.jutrack.data.JiraQueryResultItem;
import com.github.ibiber.jutrack.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.util.JiraQuery;

@RunWith(SpringRunner.class)
@RestClientTest(JiraIssuesProcessor.class)
@ContextConfiguration(classes = ModelIntegrationTest.Config.class)
public class ModelIntegrationTest {
	@Autowired
	private TestGetIssueResultItemPresenter presenter;
	@Autowired
	private JiraIssuesProcessor testee;
	@Autowired
	private MockRestServiceServer mockServer;

	@Test
	public void test() throws Exception {
		mockServer.expect(MockRestRequestMatchers.requestTo("http://localhost:8080/rest/api/2/search?"//
		        + "jql=assignee%20was%20user_01%20"//
		        + "AND%20status%20changed%20during%20(%222017-08-01%22,%222017-08-29%22)&"//
		        + "fields=key,summary"//
		        + "&expand=changelog"))//
		        .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
		        .andExpect(MockRestRequestMatchers.header("Authorization", "Basic dXNlcl8wMTphbnlQYXNzd29yZA=="))
		        .andRespond(MockRestResponseCreators.withSuccess(readExampleJson(), MediaType.APPLICATION_JSON));

		testee.getIssues(new JiraQueryParmeter("http://localhost:8080", "user_01", "anyPassword",
		        LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2017-08-01")),
		        LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2017-08-29"))));

		mockServer.verify();

		//		System.out.println(presenter.resultList.stream()//
		//		        .map(item -> item.toString())//
		//		        .collect(Collectors.joining("\n")));

		List<String> keys = presenter.resultList.stream().map(item -> item.key).distinct().collect(Collectors.toList());
		assertThat(keys, containsInAnyOrder("JUTSUP-4971", "JUTSUP-4997", "JUTSUP-4445"));
		assertThat(presenter.resultList.size(), is(12));
	}

	public String readExampleJson() throws IOException {
		try (BufferedReader buffer = new BufferedReader(
		        new InputStreamReader(new ClassPathResource("example.json").getInputStream()))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		}
	}

	public static class TestGetIssueResultItemPresenter implements GetIssueResultItemPresenter {
		public List<JiraQueryResultItem> resultList;

		public void presentResults(JiraQueryParmeter parameter, Stream<JiraQueryResultItem> resultStream) {
			resultList = resultStream.collect(Collectors.toList());
		}
	}

	@Configuration
	public static class Config {
		@Bean
		public JiraIssuesProcessor jiraIssuesProcessor(JiraIssuesQueryExecutor jiraIssuesQueryExecutor,
		        IssuesFilter<JiraQueryResultItem> filter, GetIssueResultItemPresenter presenter) {
			return new JiraIssuesProcessor(jiraIssuesQueryExecutor, filter, presenter);
		}

		@Bean
		public IssuesFilter<JiraQueryResultItem> issuesFilter() {
			return new IssuesFilter<JiraQueryResultItem>();
		}

		@Bean
		public JiraQuery jiraQuery(RestTemplateBuilder builder) {
			return new JiraQuery(builder);
		}

		@Bean
		public JiraIssuesQueryExecutor jiraIssuesQueryExecutor(JiraQuery jiraQuery) {
			return new JiraIssuesQueryExecutor(jiraQuery);
		}

		@Bean
		public GetIssueResultItemPresenter presenter() {
			return new TestGetIssueResultItemPresenter();
		}
	}
}

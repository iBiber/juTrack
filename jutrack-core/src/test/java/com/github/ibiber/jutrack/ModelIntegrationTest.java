package com.github.ibiber.jutrack;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.hamcrest.Matchers;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.github.ibiber.common.http.RestServiceQuery;
import com.github.ibiber.jutrack.domain.IssuesFilter;
import com.github.ibiber.jutrack.domain.JiraIssuesProcessor;
import com.github.ibiber.jutrack.domain.JiraIssuesQueryExecutor;
import com.github.ibiber.jutrack.domain.JiraWorklogQueryExecutor;
import com.github.ibiber.jutrack.external.GetIssueResultItemPresenter;
import com.github.ibiber.jutrack.external.JiraIssuesProcessorService;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem;

public class ModelIntegrationTest {
	private TestGetIssueResultItemPresenter presenter;
	private JiraIssuesProcessorService testee;
	private MockRestServiceServer mockServer;

	@Before
	public void setUp() {
		RestTemplate restTemplate = new RestTemplate();
		mockServer = MockRestServiceServer.createServer(restTemplate);
		RestServiceQuery restServiceQuery = new RestServiceQuery(restTemplate);
		IssuesFilter<JiraQueryResultItem> filter = new IssuesFilter<>();
		JiraIssuesQueryExecutor executor = new JiraIssuesQueryExecutor(restServiceQuery);
		JiraWorklogQueryExecutor worklogQueryExecutor = new JiraWorklogQueryExecutor(restServiceQuery);
		testee = new JiraIssuesProcessor(executor, filter, worklogQueryExecutor);
		presenter = new TestGetIssueResultItemPresenter();
	}

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

		mockServer.expect(ExpectedCount.manyTimes(),
		        MockRestRequestMatchers.requestTo(Matchers.allOf(
		                Matchers.startsWith("http://localhost:8080/rest/api/2/issue/"),
		                Matchers.endsWith("/worklog"))))
		        .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
		        .andRespond(MockRestResponseCreators.withSuccess(
		                "{\"startAt\":0,\"maxResults\":0,\"total\":0,\"worklogs\":[]}", MediaType.APPLICATION_JSON));

		testee.getIssues(new JiraQueryParmeter("http://localhost:8080", "user_01", "anyPassword",
		        LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2017-08-01")),
		        LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2017-08-29"))), presenter);

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

}

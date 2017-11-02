package com.github.ibiber.commons.http;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;

public class RestServiceQueryTest {
	@Test
	public void buildBasicAuthorizationHeader_buildCorrectly() {
		HttpHeaders headers = new HttpHeaders();
		new RestServiceQuery(new RestTemplateBuilder()).buildBasicAuthorizationHeader(headers, "UserA", "AnyPassword");

		assertThat(headers.keySet(), Matchers.contains("Authorization"));
		// Assert that the Authorization is user:password encoded with Base64
		assertThat(headers.get("Authorization"), Matchers.contains("Basic VXNlckE6QW55UGFzc3dvcmQ="));
	}
}

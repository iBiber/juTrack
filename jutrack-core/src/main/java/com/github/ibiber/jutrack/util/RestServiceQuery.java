package com.github.ibiber.jutrack.util;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.ibiber.jutrack.data.Credentials;

@Component
public class RestServiceQuery {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceQuery.class);

	private RestTemplate restTemplate;

	@Autowired
	public RestServiceQuery(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public <T> T httpGetQueryBasicAuthorization(Credentials credentials, Class<T> responseType, String path,
	        UrlParameterBuilder queryParametersBuilder) {
		String queryUrl = path + "?" + queryParametersBuilder.build();

		LOGGER.info("Query: " + queryUrl);

		HttpHeaders headers = new HttpHeaders();
		buildBasicAuthorizationHeader(headers, credentials);

		ResponseEntity<T> exchangeResult = restTemplate.exchange(queryUrl, HttpMethod.GET,
		        new HttpEntity<String>(headers), responseType);

		if (exchangeResult.getStatusCode().is2xxSuccessful()) {
			LOGGER.info("Query successful");
		} else {
			LOGGER.error("Query returns an error: " + exchangeResult.getStatusCode());
			throw new IllegalStateException(
			        "Unable to exectue the query to jira: " + exchangeResult.getStatusCode().getReasonPhrase() + " ("
			                + exchangeResult.getStatusCode().value() + ")");
		}

		return exchangeResult.getBody();
	}

	void buildBasicAuthorizationHeader(HttpHeaders headers, Credentials credentials) {
		String encodeToString = Base64.getMimeEncoder()
		        .encodeToString((credentials.userName + ":" + credentials.password).getBytes());
		headers.add("Authorization", "Basic " + encodeToString);
	}
}

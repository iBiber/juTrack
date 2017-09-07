package com.github.ibiber.jutrack.data;

import java.time.LocalDate;

public class GetIssuesParmeter {
	public final String jiraRootUrl;
	public final Credentials credentials;
	public final LocalDate startDate;
	public final LocalDate endDate;

	public GetIssuesParmeter(String jiraRootUrl, String userName, String password, LocalDate startDate,
	        LocalDate endDate) {
		super();
		this.jiraRootUrl = jiraRootUrl;
		credentials = new Credentials(userName, password);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "GetIssuesParmeter [jiraRootUrl" + jiraRootUrl + ", credentials=" + credentials + ", startDate="
		        + startDate + ", endDate=" + endDate + "]";
	}

}

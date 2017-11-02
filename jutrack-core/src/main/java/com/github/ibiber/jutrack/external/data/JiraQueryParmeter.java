package com.github.ibiber.jutrack.external.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JiraQueryParmeter {
	public final String jiraRootUrl;
	public final Credentials credentials;
	public final LocalDate startDate;
	public final LocalDate endDate;

	public JiraQueryParmeter(String jiraRootUrl, String userName, String password, LocalDate startDate,
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

	public static class Builder {
		private String jiraRootUrl;
		private String userName;
		private String password;
		private LocalDate startDate;
		private LocalDate endDate;

		public Builder jiraRootUrl(String jiraRootUrl) {
			this.jiraRootUrl = jiraRootUrl;
			return this;
		}

		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder startDate(String startDate) {
			this.startDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(startDate));
			return this;
		}

		public Builder endDate(String endDate) {
			this.endDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(endDate));
			return this;
		}

		public JiraQueryParmeter build() {
			return new JiraQueryParmeter(jiraRootUrl, userName, password, startDate, endDate);
		}
	}
}

package com.github.ibiber.jutrack.data;

import java.time.LocalDateTime;

public class JiraQueryResultItem {
	public final LocalDateTime created;
	public final String key;
	public final String summary;
	public final String changeAction;

	public JiraQueryResultItem(LocalDateTime created, String key, String summary, String changeAction) {
		super();
		this.created = created;
		this.key = key;
		this.summary = summary;
		this.changeAction = changeAction;
	}

	@Override
	public String toString() {
		return "GetIssueResultItem [created=" + created + ", key=" + key + ", summary=" + summary + ", changeAction="
		        + changeAction + "]";
	}

}
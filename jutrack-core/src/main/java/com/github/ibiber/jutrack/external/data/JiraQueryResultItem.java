package com.github.ibiber.jutrack.external.data;

import java.time.LocalDateTime;

public class JiraQueryResultItem {

	public enum EntryType {
		STATUS,
		ATTACHMENT,
		COMMENT,
		WORKLOG
	}

	public final LocalDateTime created;
	public final String key;
	public final String summary;
	public final String changeAction;
	public final EntryType type;
	public final String fromStatus;
	public final String toStatus;
	public final double worklogHours;

	private JiraQueryResultItem(LocalDateTime created, String key, String summary, String changeAction,
	        EntryType type, String fromStatus, String toStatus, double worklogHours) {
		this.created = created;
		this.key = key;
		this.summary = summary;
		this.changeAction = changeAction;
		this.type = type;
		this.fromStatus = fromStatus;
		this.toStatus = toStatus;
		this.worklogHours = worklogHours;
	}

	public static JiraQueryResultItem status(LocalDateTime created, String key, String summary, String changeAction,
	        String fromStatus, String toStatus) {
		return new JiraQueryResultItem(created, key, summary, changeAction, EntryType.STATUS, fromStatus, toStatus, 0d);
	}

	public static JiraQueryResultItem audit(LocalDateTime created, String key, String summary, String changeAction,
	        EntryType type) {
		return new JiraQueryResultItem(created, key, summary, changeAction, type, null, null, 0d);
	}

	public static JiraQueryResultItem worklog(LocalDateTime created, String key, String summary, String changeAction,
	        double hours) {
		return new JiraQueryResultItem(created, key, summary, changeAction, EntryType.WORKLOG, null, null, hours);
	}

	@Override
	public String toString() {
		return "GetIssueResultItem [created=" + created + ", key=" + key + ", summary=" + summary + ", changeAction="
		        + changeAction + ", type=" + type + ", fromStatus=" + fromStatus + ", toStatus=" + toStatus
		        + ", worklogHours=" + worklogHours + "]";
	}

}
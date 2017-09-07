package com.github.iBiber.juTrack.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetIssueResultItem {
    public final String created;
    public final String key;
    public final String summary;
    public final String changeAction;

    public GetIssueResultItem(String created, String key, String summary, String changeAction) {
        super();
        this.created = created;
        this.key = key;
        this.summary = summary;
        this.changeAction = changeAction;
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.parse(created, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
    }

    @Override
    public String toString() {
        return "GetIssueResultItem [created=" + created + ", key=" + key + ", summary=" + summary + ", changeAction="
                + changeAction + "]";
    }

}
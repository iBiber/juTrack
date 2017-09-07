package com.github.ibiber.jutrack.data.jira;

import java.util.List;

public class History {
    public final String id;
    public final Author author;
    public final String created;
    public final List<HistoryItem> items;

    /** Constructor needed by framework */
    History() {
        this(null, null, null, null);
    }

    public History(String id, Author author, String created, List<HistoryItem> items) {
        super();
        this.id = id;
        this.author = author;
        this.created = created;
        this.items = items;
    }

    @Override
    public String toString() {
        return "ChangelogHistory [id=" + id + ", author=" + author + ", created=" + created + ", items=" + items + "]";
    }

}

package com.github.ibiber.jutrack.data.jira;

public class Author {
    public final String name;
    public final String emailAddress;
    public final String displayName;
    public final String active;

    /** Constructor needed by framework */
    Author() {
        this(null, null, null, null);
    }

    public Author(String name, String emailAddress, String displayName, String active) {
        super();
        this.name = name;
        this.emailAddress = emailAddress;
        this.displayName = displayName;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Author [name=" + name + ", emailAddress=" + emailAddress + ", displayName=" + displayName + ", active="
                + active + "]";
    }
}

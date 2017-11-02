package com.github.ibiber.jutrack.external.data;

public class Credentials {
    public final String userName;
    public final String password;

    public Credentials(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credentials [userName=" + userName + ", password set=" + (password != null && password.length() > 0)
                + "]";
    }
}

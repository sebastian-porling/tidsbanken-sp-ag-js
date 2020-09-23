package se.experis.tidsbanken.server.utils;

public class Credentials {
    final private String email;
    final private String password;
    final private String code;

    public Credentials(String email, String password, String code) {
        this.email = email;
        this.password = password;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}

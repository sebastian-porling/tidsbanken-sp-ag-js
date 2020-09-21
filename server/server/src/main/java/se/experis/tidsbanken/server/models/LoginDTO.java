package se.experis.tidsbanken.server.models;

public class LoginDTO {
    final private String token;
    final private User user;

    public LoginDTO(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}

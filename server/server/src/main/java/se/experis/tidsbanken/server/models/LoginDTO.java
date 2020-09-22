package se.experis.tidsbanken.server.models;

import java.util.HashMap;

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

    public HashMap<String, Object> getUser() {
        final HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("user_id", user.getId());
        userInfo.put("full_name", user.getFullName());
        userInfo.put("profile_pic", user.getProfilePic());
        return userInfo;
    }
}

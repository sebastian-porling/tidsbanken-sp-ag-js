package se.experis.tidsbanken.server.DTOs;

import se.experis.tidsbanken.server.models.User;

import java.util.HashMap;

/**
 * Represents the data given to a loged in user
 */
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
        userInfo.put("id", user.getId());
        userInfo.put("email", user.getEmail());
        userInfo.put("full_name", user.getFullName());
        userInfo.put("profile_pic", user.getProfilePic());
        userInfo.put("is_admin", user.isAdmin());
        userInfo.put("two_factor_auth", user.isTwoFactorAuth());
        userInfo.put("vacation_days", user.getVacationDays());
        userInfo.put("used_vacation_days", user.getUsedVacationDays());
        return userInfo;
    }
}

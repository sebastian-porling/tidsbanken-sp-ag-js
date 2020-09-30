package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.samstevens.totp.secret.DefaultSecretGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isAdmin;

    @Column(columnDefinition = "varchar(255) default null")
    private String profilePic;

    @Column(nullable = false)
    private Boolean twoFactorAuth = false;

    @Column(nullable = false)
    private Integer vacationDays;

    @Column(nullable = false ,columnDefinition = "Integer default 0")
    private Integer usedVacationDays = 0;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

    @Column(nullable = false)
    private Date createdAt = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    private Date modifiedAt = new java.sql.Timestamp(new Date().getTime());

    @Column(columnDefinition = "varchar(255) default null")
    private String secret;

    @JsonIgnore
    public String generateSecret() {
        this.secret = new DefaultSecretGenerator().generate();
        return this.secret;
    }

    public void resetSecret() {
        this.secret = null;
    }

    @JsonIgnore
    public String getSecret() {
        return this.secret;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("is_admin")
    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @JsonProperty("profile_pic")
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @JsonProperty("two_factor_auth")
    public Boolean isTwoFactorAuth() {
        return twoFactorAuth;
    }

    public void setTwoFactorAuth(boolean twoFactorAuth) {
        this.twoFactorAuth = twoFactorAuth;
    }

    @JsonProperty("vacation_days")
    public Integer getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(Integer vacationDays) {
        this.vacationDays = vacationDays;
    }

    @JsonProperty("used_vacation_days")
    public Integer getUsedVacationDays() {
        return usedVacationDays;
    }

    public void setUsedVacationDays(Integer usedVacationDays) {
        this.usedVacationDays = usedVacationDays;
    }

    @JsonIgnore
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("modified_at")
    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

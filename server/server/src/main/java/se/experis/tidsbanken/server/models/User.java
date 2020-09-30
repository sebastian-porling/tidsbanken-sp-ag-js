package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.samstevens.totp.secret.DefaultSecretGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email(message = "Must be a valid email")
    private String email;

    @Column(nullable = false)
    @Size(min = 5, message = "Full name must be longer than 5 characters")
    @Pattern(regexp = "^[a-zA-Z0-9.\\-\\/+=@_ ]*$", message = "Full name cannot contain any special characters")
    private String fullName;

    @Column(nullable = false)
    @Size(min = 6, max = 30, message = "Passoword needs to be between 6 and 30 characters")
    private String password;

    @Column(nullable = false)
    private Boolean isAdmin;

    @Column(columnDefinition = "varchar(255) default null")
    @Pattern(regexp = "^(https?|chrome):\\/\\/[^\\s$.?#].[^\\s]*$", message = "Profile pic url must be a valid format")
    private String profilePic;

    @Column(nullable = false)
    private boolean twoFactorAuth = false;

    @Column(nullable = false)
    @Positive(message = "Vacation days must be a positive number")
    @Max(value = 30, message = "Vacation days can't exceed 30 days")
    private Integer vacationDays;

    @Column(nullable = false ,columnDefinition = "Integer default 0")
    @PositiveOrZero(message = "Used vacation days must be a positive number or zero")
    private Integer usedVacationDays = 0;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

    @Column(nullable = false)
    @PastOrPresent
    private Date createdAt = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    @PastOrPresent
    private Date modifiedAt = new java.sql.Timestamp(new Date().getTime());

    @Column(columnDefinition = "varchar(255) default null")
    private String secret;

    @JsonIgnore
    public String generateSecret() {
        this.secret = new DefaultSecretGenerator().generate();
        return this.secret;
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
    public boolean isTwoFactorAuth() {
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

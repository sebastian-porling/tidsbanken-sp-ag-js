package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message = "Comment message can not be null")
    @Size(min = 2, max = 250, message = "Comment has to be between 2 and 250 characters")
    //  Change pattern to allow ! . , etc
    // @Pattern(regexp = "^[a-zA-Z0-9.\\-\\/+=@_ ]*$", message = "Comment can not contain any special characters")
    private String message;

    @ManyToOne
    private VacationRequest request;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd h:mm a")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    //private Date createdAt = new java.sql.Date(System.currentTimeMillis());

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd h:mm a")
    private Timestamp modifiedAt = new Timestamp(System.currentTimeMillis());
    //private Date modifiedAt = new java.sql.Date(System.currentTimeMillis());

    public Comment() { }



    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public HashMap<String, Object> getRequest() {
        final HashMap<String, Object> request = new HashMap<>();
        request.put("id", this.request.getId());
        return request;
    }

    public HashMap<String, Object> getUser() {
        final HashMap<String, Object> user = new HashMap<>();
        user.put("user_id", this.user.getId());
        user.put("email", this.user.getEmail());
        user.put("name", this.user.getFullName());
        user.put("profile_pic", this.user.getProfilePic());
        return user;
    }

    @JsonIgnore
    public User getOriginalUser(){
        return this.user;
    }

    @JsonProperty("created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("modified_at")
    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public Comment setUser(User user) {
        this.user = user;
        return this;
    }

    public Comment setRequest(VacationRequest request) {
        this.request = request;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void updateModifiedAt() {
        this.modifiedAt = new Timestamp(System.currentTimeMillis());
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

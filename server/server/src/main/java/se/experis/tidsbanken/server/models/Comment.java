package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    private VacationRequest request;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Date createdAt = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    private Date modifiedAt = new java.sql.Timestamp(new Date().getTime());

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

    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("modified_at")
    public Date getModifiedAt() {
        return modifiedAt;
    }

}

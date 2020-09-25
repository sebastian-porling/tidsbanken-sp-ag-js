package se.experis.tidsbanken.server.models;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String message;

    @NonNull
    private Boolean read = false;

    @ManyToOne
    private User user;

    public Notification() { }

    public Notification(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getRead() {
        return this.read;
    }

    public Notification setRead(Boolean read) {
        this.read = read;
        return this;
    }
}

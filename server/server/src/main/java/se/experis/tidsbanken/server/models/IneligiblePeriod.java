package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.sql.Date;
import java.util.HashMap;

@Entity
public class IneligiblePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Date start;

    @Column(nullable = false)
    @NotNull
    private Date end;

    @ManyToOne
    private User moderator;

    @Column(nullable = false)
    private Date createdAt = new java.sql.Date(System.currentTimeMillis());

    @Column(nullable = false)
    private Date modifiedAt = new java.sql.Date(System.currentTimeMillis());

    public IneligiblePeriod() {}

    public Long getId() {
        return id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public HashMap<String, Object> getModerator() {
        final HashMap<String, Object> moderator = new HashMap<>();
        moderator.put("id", this.moderator.getId());
        moderator.put("email", this.moderator.getEmail());
        moderator.put("full_name", this.moderator.getFullName());
        moderator.put("profile_pic", this.moderator.getProfilePic());
        return moderator;
    }

    @JsonIgnore
    public User getOriginalModerator() {
        return this.moderator;
    }

    public IneligiblePeriod setModerator(User moderator) {
        this.moderator = moderator;
        return this;
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

    @Override
    public String toString() {
        return "IneligiblePeriod{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", moderator=" + moderator +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    /**
     * Checks so this ineligible period doesn't overlap with given ineligible period
     * @param ip Ineligible Period
     * @return true if it doesn't overlap
     */
    public boolean excludesInPeriod(IneligiblePeriod ip) {
        return (ip.start.before(this.start) && ip.end.before(this.start)) ||
                (ip.start.after(this.end) && ip.end.after(this.end));
    }

}

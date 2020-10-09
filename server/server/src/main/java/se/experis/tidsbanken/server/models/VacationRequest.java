package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.util.HashMap;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9.\\-\\/+=@_ ]*$", message = "Title can not contain any special characters")
    @Size(min = 6, max = 100, message = "Title must be between 6 and 100 characters")
    private String title;

    @Column(nullable = false)
    private Date start;

    @Column(nullable = false)
    private Date end;

    @ManyToOne
    private User owner;

    @ManyToOne
    @NotNull
    private Status status;

    @Column(nullable = false)
    private Date createdAt = new Date(System.currentTimeMillis());

    @Column(nullable = false)
    private Date modifiedAt = new Date(System.currentTimeMillis());

    @ManyToOne
    private User moderator;

    @Column
    private Date moderationDate;

    public VacationRequest() {}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @JsonIgnore
    public User getOriginalOwner() {
        return owner;
    }

    public HashMap<String, Object> getOwner() {
        return getUser(owner);
}

    public VacationRequest setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public VacationRequest setStatus(Status status) {
        this.status = status;
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

    @JsonIgnore
    public User getOriginalModerator() {
        return moderator;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public HashMap<String, Object> getModerator() {
        if(moderator != null) { return getUser(moderator); }
        return null;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("moderation_date")
    public Date getModerationDate() {
        return moderationDate;
    }

    public void setModerationDate(Date moderationDate) {
        this.moderationDate = moderationDate;
    }

    @Override
    public String toString() {
        return "VacationRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", owner=" + owner +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", moderator=" + moderator +
                ", moderationDate=" + moderationDate +
                '}';
    }

    private HashMap<String, Object> getUser(User user) {
        final HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("owner_id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("full_name", user.getFullName());
        userMap.put("profile_pic", user.getProfilePic());
        return userMap;
    }

    /**
     * Checks if vacation request is approved
     * @return true if status is approved
     */
    @JsonIgnore
    public boolean onlyApproved() {
        return this.status.getStatus().equals("Approved");
    }

    /**
     * Checks if vacation request is pending
     * @return true if status is pending
     */
    @JsonIgnore
    public boolean isPending() {
        return this.status.getStatus().equals("Pending");
    }

    /**
     * Checks if the vacation request is denied
     * @return true if denied
     */
    @JsonIgnore
    public boolean isDenied() {
        return this.status.getStatus().equals("Denied");
    }

    /**
     * Checks so this vacation request doesn't overlap with given vacation request
     * @param vr Vacation Request
     * @return true if it doesn't overlap
     */
    @JsonIgnore
    public boolean excludesInPeriod(VacationRequest vr) {
        return (this.start.before(vr.start) && this.end.before(vr.start)) ||
                (this.start.after(vr.end) && this.end.after(vr.end));
    }

    /**
     * Checks so this vacation request doesn't overlap with given ineligible period
     * @param ip Ineligible Period
     * @return true if it doesn't overlap
     */
    @JsonIgnore
    public boolean excludesInIP(IneligiblePeriod ip) {
        return (this.start.before(ip.getStart()) && this.end.before(ip.getStart())) ||
                (this.start.after(ip.getEnd()) && this.end.after(ip.getEnd()));
    }


}

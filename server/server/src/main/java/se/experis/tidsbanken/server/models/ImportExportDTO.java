package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.sql.Date;
import java.util.List;

public class ImportExportDTO {
    @NonNull
    private String title;
    @NonNull
    private Date start;
    @NonNull
    private Date end;
    @NonNull
    private Long userId;
    @NonNull
    private String status;
    private Date createdAt;
    private Date modifiedAt;
    private Long moderatorId;
    private Date moderationDate;
    private List<CommentDTO> comments;

    public ImportExportDTO(String title, Date start, Date end, Long userId, String status, Date createdAt, Date modifiedAt, Long moderatorId, Date moderationDate, List<CommentDTO> comments) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.moderatorId = moderatorId;
        this.moderationDate = moderationDate;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("modified_at")
    public Date getModifiedAt() {
        return modifiedAt;
    }

    @JsonProperty("moderator_id")
    public Long getModeratorId() {
        return moderatorId;
    }

    @JsonProperty("moderation_date")
    public Date getModerationDate() {
        return moderationDate;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }
}

package se.experis.tidsbanken.server.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.sql.Date;

public class CommentDTO {
    @NonNull
    private String message;
    @NonNull
    private Long userId;
    private Date createdAt;
    private Date modifiedAt;

    public CommentDTO(String message, Long userId, Date createdAt, Date modifiedAt) {
        this.message = message;
        this.userId = userId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
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

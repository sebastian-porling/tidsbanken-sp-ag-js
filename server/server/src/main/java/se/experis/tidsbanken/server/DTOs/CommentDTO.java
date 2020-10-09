package se.experis.tidsbanken.server.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.tomcat.jni.Local;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a comment for a import export dto
 */
public class CommentDTO {
    @NonNull
    private String message;
    @NonNull
    private Long userId;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public CommentDTO(String message, Long userId, Timestamp createdAt, Timestamp modifiedAt) {
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
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("modified_at")
    public Timestamp getModifiedAt() {
        return modifiedAt;
    }
}

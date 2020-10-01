package se.experis.tidsbanken.server.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
public class LoginAttempt {
    @Id
    private String ip;

    @Column(columnDefinition = "Integer default 1")
    private Integer failedAttempts = 1;

    @Column
    private Timestamp blockedTimeStamp;

    public LoginAttempt() {}

    public LoginAttempt(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public Timestamp getBlockedTimeStamp() {
        return blockedTimeStamp;
    }

    public LoginAttempt incrementFailedAttempts() {
        this.failedAttempts++;
        return this;
    }

    public LoginAttempt setToBlocked() {
        this.blockedTimeStamp = new java.sql.Timestamp(System.currentTimeMillis() + 1000 * 60 * 10);
        return this;
    }
}

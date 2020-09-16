package se.experis.tidsbanken.server.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long user_id;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String full_name;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public boolean isAdmin;

    @Column
    public String profile_pic;

    @Column(nullable = false)
    public boolean twoFactorAuth = false;

    @Column(nullable = false)
    public Integer vacation_days;

    @Column(nullable = false)
    public Integer used_vacation_days = 0;

    @Column(nullable = false)
    public boolean isActive = true;

    @Column(nullable = false)
    public Date created_at = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    public Date modified_at = new java.sql.Timestamp(new Date().getTime());
}

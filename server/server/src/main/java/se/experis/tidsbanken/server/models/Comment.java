package se.experis.tidsbanken.server.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long comment_id;

    @Column(nullable = false)
    public String message;

    @ManyToOne
    public VacationRequest request;

    @ManyToOne
    public User user;

    @Column(nullable = false)
    public Date created_at = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    public Date modified_at = new java.sql.Timestamp(new Date().getTime());
}

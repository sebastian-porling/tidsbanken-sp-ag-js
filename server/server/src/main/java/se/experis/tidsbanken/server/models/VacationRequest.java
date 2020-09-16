package se.experis.tidsbanken.server.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long request_id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public Date period_start;

    @Column(nullable = false)
    public Date period_end;

    @ManyToOne
    public AppUser owner;

    @ManyToOne
    public RequestStatus requestStatus;

    @Column(nullable = false)
    public Date created_at = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    public Date modified_at = new java.sql.Timestamp(new Date().getTime());

    @ManyToOne
    public AppUser moderator;

    @Column
    public Date moderation_date;
}

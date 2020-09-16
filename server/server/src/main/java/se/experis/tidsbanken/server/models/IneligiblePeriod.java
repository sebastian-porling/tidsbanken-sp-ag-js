package se.experis.tidsbanken.server.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class IneligiblePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long ip_id;

    @Column(nullable = false)
    public Date period_start;

    @Column(nullable = false)
    public Date period_end;

    @ManyToOne
    public AppUser moderator;

    @Column(nullable = false)
    public Date created_at = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    public Date modified_at = new java.sql.Timestamp(new Date().getTime());

}

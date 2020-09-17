package se.experis.tidsbanken.server.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class IneligiblePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long ip_id;

    @Column(nullable = false)
    @NotNull
    public Date start;

    @Column(nullable = false)
    @NotNull
    public Date end;

    @ManyToOne
    @NotNull
    public AppUser moderator;

    @Column(nullable = false)
    public Date created_at = new java.sql.Date(System.currentTimeMillis());
    @Column(nullable = false)
    public java.sql.Date modified_at = new java.sql.Date(System.currentTimeMillis());

    public boolean excludesInPeriod(IneligiblePeriod ip) {
        return (ip.start.before(this.start) && ip.end.before(this.start)) ||
                (ip.start.after(this.end) && ip.end.after(this.end));
    }
}

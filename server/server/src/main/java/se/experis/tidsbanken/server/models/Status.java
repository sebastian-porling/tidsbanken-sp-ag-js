package se.experis.tidsbanken.server.models;

import javax.persistence.*;

@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer status_id;

    @Column(nullable = false)
    public String status;
}

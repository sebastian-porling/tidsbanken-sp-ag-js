package se.experis.tidsbanken.server.models;

import javax.persistence.*;

@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String status;

    public Status(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Status() {}

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

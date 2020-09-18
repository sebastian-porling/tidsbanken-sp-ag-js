package se.experis.tidsbanken.server.models;

public enum StatusType {
    PENDING (new Status(1, "Pending")),
    APPROVED( new Status(2, "Approved")),
    DENIED  (new Status(3, "Denied"));

    private final Status status;
    StatusType(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}

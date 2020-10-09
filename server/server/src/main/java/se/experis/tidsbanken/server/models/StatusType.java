package se.experis.tidsbanken.server.models;

/**
 * Represents all available status types
 */
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

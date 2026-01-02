package org.gatesystem.model;

import java.time.LocalDateTime;

public class EntryLog {
    public static enum Status {
        IN,
        OUT
    } //status of vehicles

    private int id;
    private final String carName;
    private final String model;
    private final String licensePlate;
    private final String ownerName;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final Status status;
    private final String guardUsername;

    public EntryLog(
            String carName,
            String model,
            String licensePlate,
            String ownerName,
            LocalDateTime entryTime,
            LocalDateTime exitTime,
            String guardUsername,
            Status status
    ) {
        this.carName = carName;
        this.model = model;
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.guardUsername = guardUsername;
        this.status = status;
    }

    public int getId() { return id; }
    public String getCarName() { return carName; }
    public String getModel() { return model; }
    public String getLicensePlate() { return licensePlate; }
    public String getOwnerName() { return ownerName; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public String getGuardUsername() { return guardUsername; }
    public Status getStatus() { return status; }

    public void setId(int id) { this.id = id; }
}

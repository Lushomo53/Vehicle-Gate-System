package org.example.model;

import java.time.LocalDateTime;

public class EntryLog {
    public static enum Status {
        IN,
        OUT
    } //status of vehicles

    private String carName;
    private String model;
    private String licensePlate;
    private String ownerName;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Status status;

    public EntryLog(
            String carName,
            String model,
            String licensePlate,
            String ownerName,
            LocalDateTime entryTime,
            LocalDateTime exitTime,
            Status status
    ) {
        this.carName = carName;
        this.model = model;
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.status = status;
    }

    public String getCarName() { return carName; }
    public String getModel() { return model; }
    public String getLicensePlate() { return licensePlate; }
    public String getOwnerName() { return ownerName; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public Status getStatus() { return status; }
}

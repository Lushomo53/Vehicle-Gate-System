package org.example.model;

import java.time.LocalDateTime;

public class Vehicle {
    private final String name;
    private final String model;
    private String licensePlate;
    private String ownerName;
    private LocalDateTime latestEntryTime;
    private LocalDateTime latestExitTime;

    public Vehicle(String name, String model, String licensePlate, String ownerName) {
        this.name = name;
        this.model = model;
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        latestEntryTime = LocalDateTime.now();
        latestExitTime = null;
    }

    public String getName() { return name; }
    public String getModel() { return model; }
    public String getLicensePlate() { return licensePlate; }
    public String getOwnerName() { return ownerName; }
    public LocalDateTime getLatestEntryTime() { return latestEntryTime;}
    public LocalDateTime getLatestExitTime() { return latestExitTime;}

    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public void setLatestEntryTime(LocalDateTime latestEntryTime) { this.latestEntryTime = latestEntryTime; }
    public void setLatestExitTime(LocalDateTime latestExitTime) { this.latestExitTime = latestExitTime; }

    public EntryLog.Status getStatus() {
        if (latestExitTime == null) return EntryLog.Status.IN;

        return latestEntryTime.isAfter(latestExitTime) ? EntryLog.Status.IN : EntryLog.Status.OUT;
    }
}

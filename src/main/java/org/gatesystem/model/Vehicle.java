package org.gatesystem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Vehicle {
    private int id;
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty model = new SimpleStringProperty();
    private final StringProperty licensePlate = new SimpleStringProperty();
    private final StringProperty ownerName = new SimpleStringProperty();
    private LocalDateTime latestEntryTime;
    private LocalDateTime latestExitTime;

    public Vehicle(String name, String model, String licensePlate, String ownerName) {
        this.name.set(name);
        this.model.set(model);
        this.licensePlate.set(licensePlate);
        this.ownerName.set(ownerName);
        latestEntryTime = LocalDateTime.now();
        latestExitTime = null;
    }


    public int getId() { return id; }
    public String getName() { return name.get(); }
    public String getModel() { return model.get(); }
    public String getLicensePlate() { return licensePlate.get(); }
    public String getOwnerName() { return ownerName.get(); }
    public LocalDateTime getLatestEntryTime() { return latestEntryTime;}
    public LocalDateTime getLatestExitTime() { return latestExitTime;}
    public StringProperty nameProperty() { return name; }
    public StringProperty modelProperty() { return model; }
    public StringProperty ownerNameProperty() { return ownerName; }

    public void setId(int id) { this.id = id; }
    public void setModel(String model) { this.model.set(model); }
    public void setLicensePlate(String licensePlate) { this.licensePlate.set(licensePlate); }
    public void setOwnerName(String ownerName) { this.ownerName.set(ownerName); }
    public void setLatestEntryTime(LocalDateTime latestEntryTime) { this.latestEntryTime = latestEntryTime; }
    public void setLatestExitTime(LocalDateTime latestExitTime) { this.latestExitTime = latestExitTime; }
    public void setName(String name) { this.name.set(name); }

    public EntryLog.Status getStatus() {
        if (latestExitTime == null) return EntryLog.Status.IN;

        return latestEntryTime.isAfter(latestExitTime) ? EntryLog.Status.IN : EntryLog.Status.OUT;
    }


}

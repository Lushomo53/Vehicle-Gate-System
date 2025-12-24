package org.example.controller;

import org.example.model.Vehicle;
import org.example.repo.VehicleRepository;

import java.util.List;

public class VehicleListController {
    private final VehicleRepository repo;

    public VehicleListController() {
        repo = new VehicleRepository();
    }

    public List<Vehicle> getAllVehicles() {
        return repo.getAllVehicles();
    }
}

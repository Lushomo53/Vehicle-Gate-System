package org.gatesystem.controller;

import org.gatesystem.model.Vehicle;
import org.gatesystem.repo.VehicleRepository;

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

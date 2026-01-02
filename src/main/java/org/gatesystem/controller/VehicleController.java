package org.gatesystem.controller;

import org.gatesystem.model.Vehicle;
import org.gatesystem.repo.VehicleRepository;
import org.gatesystem.util.RegexPatterns;
import org.gatesystem.views.components.FilterPane;

import java.util.List;

public class VehicleController {
    private final VehicleRepository repo;
    private List<Vehicle> vehiclesToDisplay;
    private VehicleFilterCache filterCache;

    public VehicleController() {
        repo = new VehicleRepository();
        vehiclesToDisplay = repo.getAllVehicles();
        filterCache = new VehicleFilterCache("", false, "");
    }

    public List<Vehicle> getAllVehicles() {
        return repo.getAllVehicles();
    }

    public List<Vehicle> updatedVehicles() {
        filter(
                filterCache.search(),
                filterCache.filterBySelected(),
                filterCache.filterBy()
        );
        return vehiclesToDisplay;
    }

    public VehicleFilterStatus filter(String search, boolean filterBySelected, String filterBy) {
        filterCache = new VehicleFilterCache(search, filterBySelected, filterBy);

        if (search.isEmpty()) {
            vehiclesToDisplay = repo.getAllVehicles();
            return VehicleFilterStatus.FILTER_SUCCESS;
        }

        if (!filterBySelected) {
            return VehicleFilterStatus.FILTER_BY_NOT_SELECTED;
        }

        switch (filterBy) {
            case FilterPane.CAR_NAME_OPTION ->
                    vehiclesToDisplay = repo.findByName(search);

            case FilterPane.LICENSE_PLATE_OPTION -> {
                if (!RegexPatterns.LICENSE_PLATE_PATTERN.matcher(search).matches())
                    return VehicleFilterStatus.INVALID_LICENSE_PLATE;

                vehiclesToDisplay = repo.findByLicensePlate(formatToLicensePlate(search));
            }

            case FilterPane.OWNER_NAME_OPTION ->
                    vehiclesToDisplay = repo.findByOwner(search);
        }

        return VehicleFilterStatus.FILTER_SUCCESS;
    }

    public VehicleRegistrationStatus registerVehicle(String name, String model, String licensePlate, String ownerName) {
        if (name.isEmpty() || model.isEmpty() || licensePlate.isEmpty() || ownerName.isEmpty()) return VehicleRegistrationStatus.EMPTY_FIELDS;
        if (!RegexPatterns.LICENSE_PLATE_PATTERN.matcher(licensePlate).matches()) return VehicleRegistrationStatus.INVALID_LICENSE_PLATE;
        if (repo.vehicleAlreadyExists(formatToLicensePlate(licensePlate))) return VehicleRegistrationStatus.VEHICLE_ALREADY_EXISTS;

        Vehicle newVehicle = new Vehicle(name, model, formatToLicensePlate(licensePlate), ownerName);
        repo.registerVehicle(newVehicle);
        return VehicleRegistrationStatus.REGISTRATION_SUCCESS;
    }

    public VehicleDeleteStatus deleteVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return VehicleDeleteStatus.NO_SELECTION;
        }

        boolean deleted = repo.deleteByPlate(vehicle.getLicensePlate());

        return deleted
                ? VehicleDeleteStatus.DELETE_SUCCESS
                : VehicleDeleteStatus.DELETE_FAILED;
    }

    public VehicleUpdateStatus updateVehicle(
            String licensePlate,
            String name,
            String model,
            String ownerName
    ) {

        if (name.isBlank() || model.isBlank() || ownerName.isBlank()) {
            return VehicleUpdateStatus.EMPTY_FIELDS;
        }

        if (!RegexPatterns.LICENSE_PLATE_PATTERN.matcher(licensePlate).matches()) {
            return VehicleUpdateStatus.INVALID_LICENSE_PLATE;
        }

        boolean updated = repo.updateVehicle(
                licensePlate,
                name,
                model,
                ownerName
        );

        if (!updated) {
            return VehicleUpdateStatus.VEHICLE_NOT_FOUND;
        }

        return VehicleUpdateStatus.UPDATE_SUCCESS;
    }

    private static String normalizePlate(String text) {
        return text
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "");
    }


    public static String formatToLicensePlate(String text) {
        String normalized = normalizePlate(text);

        if (!RegexPatterns.LICENSE_PLATE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid license plate format");
        }

        return normalized.substring(0, 3) + " " + normalized.substring(3);
    }
}

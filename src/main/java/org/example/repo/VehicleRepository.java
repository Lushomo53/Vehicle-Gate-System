package org.example.repo;

import org.example.model.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

public class VehicleRepository {
    public List<Vehicle> getAllVehicles() {
        return List.of(
                new Vehicle(
                        "Toyota",
                        "Corolla",
                        "ABC-1234",
                        "Lushomo Lungo"
                ),
                new Vehicle(
                        "Honda",
                        "Civic",
                        "BCY-4367",
                        "George Cluny"
                        ),
                new Vehicle(
                        "Ford",
                        "Ford",
                        "CBA-1456",
                        "John Doe"
                        )
        );
    }
}

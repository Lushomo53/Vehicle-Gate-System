package org.gatesystem.repo;

import org.gatesystem.model.Vehicle;
import org.gatesystem.util.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    public VehicleRepository() {
        // no dummy data anymore
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        String sql = """
                SELECT name, model, license_plate, owner_name,
                       latest_entry_time, latest_exit_time
                FROM vehicles
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicle v = mapRow(rs);
                vehicles.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public Vehicle findVehicleByPlate(String licensePlate) {
        String sql = """
                SELECT name, model, license_plate, owner_name,
                       latest_entry_time, latest_exit_time
                FROM vehicles
                WHERE license_plate = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Vehicle> findByName(String name) {
        return findByLike("name", name);
    }

    public List<Vehicle> findByLicensePlate(String plate) {
        return findByLike("license_plate", plate);
    }

    public List<Vehicle> findByOwner(String owner) {
        return findByLike("owner_name", owner);
    }

    private List<Vehicle> findByLike(String column, String value) {
        List<Vehicle> vehicles = new ArrayList<>();

        String sql = """
                SELECT name, model, license_plate, owner_name,
                       latest_entry_time, latest_exit_time
                FROM vehicles
                WHERE %s LIKE ?
                """.formatted(column);

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + value + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public void registerVehicle(Vehicle vehicle) {
        String sql = """
                INSERT INTO vehicles
                (name, model, license_plate, owner_name, latest_entry_time)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehicle.getName());
            ps.setString(2, vehicle.getModel());
            ps.setString(3, vehicle.getLicensePlate());
            ps.setString(4, vehicle.getOwnerName());
            ps.setTimestamp(5, Timestamp.valueOf(vehicle.getLatestEntryTime()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteByPlate(String plate) {
        String sql = "DELETE FROM vehicles WHERE license_plate = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plate);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateVehicle(
            String licensePlate,
            String name,
            String model,
            String ownerName
    ) {
        String sql = """
                UPDATE vehicles
                SET name = ?, model = ?, owner_name = ?
                WHERE license_plate = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, model);
            ps.setString(3, ownerName);
            ps.setString(4, licensePlate);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateLatestEntryTime(Vehicle vehicle, LocalDateTime latestEntryTime) {
        updateTimestamp(
                "latest_entry_time",
                latestEntryTime,
                vehicle.getLicensePlate()
        );
    }

    public void updateLatestExitTime(Vehicle vehicle, LocalDateTime latestExitTime) {
        updateTimestamp(
                "latest_exit_time",
                latestExitTime,
                vehicle.getLicensePlate()
        );
    }

    private void updateTimestamp(String column, LocalDateTime time, String plate) {
        String sql = "UPDATE vehicles SET " + column + " = ? WHERE license_plate = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(time));
            ps.setString(2, plate);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean vehicleAlreadyExists(String licensePlate) {
        String sql = "SELECT 1 FROM vehicles WHERE license_plate = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        Vehicle v = new Vehicle(
                rs.getString("name"),
                rs.getString("model"),
                rs.getString("license_plate"),
                rs.getString("owner_name")
        );

        Timestamp entryTs = rs.getTimestamp("latest_entry_time");
        Timestamp exitTs = rs.getTimestamp("latest_exit_time");

        if (entryTs != null) v.setLatestEntryTime(entryTs.toLocalDateTime());
        if (exitTs != null) v.setLatestExitTime(exitTs.toLocalDateTime());

        return v;
    }
}

package org.gatesystem.repo;

import org.gatesystem.model.EntryLog;
import org.gatesystem.util.DatabaseConnector;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EntryLogRepository {

    private EntryLog mapRow(ResultSet rs) throws SQLException {
        EntryLog log = new EntryLog(
                rs.getString("car_name"),
                rs.getString("model"),
                rs.getString("license_plate"),
                rs.getString("owner_name"),
                rs.getTimestamp("entry_time").toLocalDateTime(),
                rs.getTimestamp("exit_time") != null
                        ? rs.getTimestamp("exit_time").toLocalDateTime()
                        : null,
                rs.getString("guard_username"),
                EntryLog.Status.valueOf(rs.getString("status"))
        );
        log.setId(rs.getInt("id"));
        return log;
    }

    public List<EntryLog> getAllLogs() {
        String sql = "SELECT * FROM entry_logs ORDER BY entry_time DESC";
        List<EntryLog> logs = new ArrayList<>();

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                logs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public List<EntryLog> getFindLogsByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM entry_logs WHERE license_plate = ?";
        return fetchMany(sql, ps -> ps.setString(1, licensePlate));
    }

    public List<EntryLog> getLogsByName(String carName) {
        String[] parts = carName.split("\\s+", 2);

        if (parts.length == 1) {
            return fetchMany(
                    "SELECT * FROM entry_logs WHERE car_name = ?",
                    ps -> ps.setString(1, parts[0])
            );
        }

        return fetchMany(
                "SELECT * FROM entry_logs WHERE car_name = ? AND model = ?",
                ps -> {
                    ps.setString(1, parts[0]);
                    ps.setString(2, parts[1]);
                }
        );
    }

    public List<EntryLog> getLogsByOwner(String owner) {
        return fetchMany(
                "SELECT * FROM entry_logs WHERE owner_name = ?",
                ps -> ps.setString(1, owner)
        );
    }

    public List<EntryLog> getLogsBetween(LocalDate from, LocalDate to) {
        return fetchMany(
                "SELECT * FROM entry_logs WHERE entry_time BETWEEN ? AND ?",
                ps -> {
                    ps.setTimestamp(1, Timestamp.valueOf(from.atStartOfDay()));
                    ps.setTimestamp(2, Timestamp.valueOf(to.plusDays(1).atStartOfDay()));
                }
        );
    }

    public List<EntryLog> getLogsForVehiclesStatus(String status) {
        return fetchMany(
                "SELECT * FROM entry_logs WHERE status = ?",
                ps -> ps.setString(1, status)
        );
    }

    public Optional<EntryLog> findLatestByLicense(String plate) {
        String sql = """
            SELECT * FROM entry_logs
            WHERE license_plate = ?
            ORDER BY entry_time DESC
            LIMIT 1
        """;

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(EntryLog log) {
        String sql = """
            INSERT INTO entry_logs
            (car_name, model, license_plate, owner_name, entry_time, exit_time, guard_username, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, log.getCarName());
            ps.setString(2, log.getModel());
            ps.setString(3, log.getLicensePlate());
            ps.setString(4, log.getOwnerName());
            ps.setTimestamp(5, Timestamp.valueOf(log.getEntryTime()));
            ps.setTimestamp(6,
                    log.getExitTime() != null
                            ? Timestamp.valueOf(log.getExitTime())
                            : null
            );
            ps.setString(7, log.getGuardUsername());
            ps.setString(8, log.getStatus().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ---------- helper ---------- */

    private List<EntryLog> fetchMany(
            String sql,
            SQLConsumer<PreparedStatement> paramSetter
    ) {
        List<EntryLog> logs = new ArrayList<>();

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            paramSetter.accept(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                logs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }
}

package org.gatesystem.controller;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.model.EntryLog;
import org.gatesystem.model.Vehicle;
import org.gatesystem.repo.EntryLogRepository;
import org.gatesystem.repo.VehicleRepository;
import org.gatesystem.util.FileHandler;
import org.gatesystem.util.LocalDateParser;
import org.gatesystem.util.RegexPatterns;
import org.gatesystem.views.components.FilterPane;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EntryLogController {
    private final EntryLogRepository repo;
    private final VehicleRepository vehicleRepo;
    private List<EntryLog> logsToDisplay;
    private Throwable errorStatus;
    private FilterCache filterCache;

    public EntryLogController() {
        repo = new EntryLogRepository();
        vehicleRepo = new VehicleRepository();
        logsToDisplay = repo.getLogsBetween(LocalDate.now().minusDays(5), LocalDate.now());
        filterCache = new FilterCache("", false, "", "", "", LocalDate.now().minusDays(7), LocalDate.now(), FilterPane.ALL_OPTION);
    }

    public List<EntryLog> updatedLogs() {
        filter(filterCache.search, filterCache.filterBySelected, filterCache.filterBy, filterCache.dateFromText, filterCache.dateToText, filterCache.dateFrom, filterCache.dateTo, filterCache.entryStatus);
        return logsToDisplay;
    }

    public List<EntryLog> getAllLogs() {
        return repo.getAllLogs();
    }

    public FilterStatus filter(String search, boolean filterBySelected, String filterBy, String dateFromText, String dateToText, LocalDate dateFrom, LocalDate dateTo, String entryStatus) {
        filterCache = new FilterCache(search, filterBySelected, filterBy, dateFromText, dateToText, dateFrom, dateTo, entryStatus);
        if (!search.isEmpty()) {
            if (!filterBySelected) return FilterStatus.FILTER_BY_NOT_SELECTED;

            switch (filterBy) {
                case FilterPane.CAR_NAME_OPTION -> logsToDisplay = repo.getLogsByName(search);
                case FilterPane.LICENSE_PLATE_OPTION -> {
                    if (!RegexPatterns.LICENSE_PLATE_PATTERN.matcher(search).matches()) return FilterStatus.INVALID_LICENSE_PLATE;
                    logsToDisplay = repo.getFindLogsByLicensePlate(VehicleController.formatToLicensePlate(search));
                }
                case FilterPane.OWNER_NAME_OPTION -> logsToDisplay = repo.getLogsByOwner(search);
            }
        }

        if (!dateFromText.isEmpty()) {
            dateFrom = LocalDateParser.parse(dateFromText);
            if (dateFrom == null) return FilterStatus.INVALID_DATE_FROM;
        }

        if (!dateToText.isEmpty()) {
            dateTo = LocalDateParser.parse(dateToText);
            if (dateTo == null) return FilterStatus.INVALID_DATE_TO;
        }

        if (dateFrom == null) dateFrom = LocalDate.now().minusDays(7);
        if (dateTo == null) dateTo = LocalDate.now();

        if (search.isEmpty()) {
            logsToDisplay = repo.getLogsBetween(dateFrom, dateTo);
        } else {
            LocalDate finalDateFrom = dateFrom;
            LocalDate finalDateTo = dateTo;

            logsToDisplay = logsToDisplay.stream()
                    .filter(log -> log.getEntryTime().isAfter(finalDateFrom.atStartOfDay()) && log.getEntryTime().isBefore(finalDateTo.plusDays(1).atStartOfDay()))
                    .collect(Collectors.toList());

        }

        switch (entryStatus) {
            case FilterPane.ALL_OPTION -> {}
            case FilterPane.IN_OPTION -> logsToDisplay = logsToDisplay.stream().filter(log -> log.getStatus() == EntryLog.Status.IN).collect(Collectors.toList());
            case FilterPane.OUT_OPTION -> logsToDisplay = logsToDisplay.stream().filter(log -> log.getStatus() == EntryLog.Status.OUT).collect(Collectors.toList());
        }

        return FilterStatus.FILTER_SUCCESS;
    }

    public CheckStatus checkIn(String plate) {
        plate = VehicleController.formatToLicensePlate(plate);
        if (!RegexPatterns.LICENSE_PLATE_PATTERN.matcher(plate).matches())
            return CheckStatus.INVALID_LICENSE_FORMAT;

        Vehicle vehicle = vehicleRepo.findVehicleByPlate(plate);
        if (vehicle == null)
            return CheckStatus.VEHICLE_NOT_FOUND;

        if (vehicle.getStatus() == EntryLog.Status.OUT)
            return CheckStatus.ALREADY_CHECKED_OUT;

        Optional<EntryLog> latest = repo.findLatestByLicense(plate);

        LocalDateTime time = LocalDateTime.now();

        if (latest.isPresent()) {
            EntryLog inLog = latest.get();
            repo.save(new EntryLog(
                    inLog.getCarName(),
                    inLog.getModel(),
                    plate,
                    inLog.getOwnerName(),
                    time,
                    inLog.getExitTime(),
                    SessionManager.currentSession.getCurrentUser().getUsername(),
                    EntryLog.Status.IN
            ));
        } else {
            repo.save(new EntryLog(
                    vehicle.getName(),
                    vehicle.getModel(),
                    vehicle.getLicensePlate(),
                    vehicle.getOwnerName(),
                    time,
                    vehicle.getLatestExitTime(),
                    SessionManager.currentSession.getCurrentUser().getUsername(),
                    EntryLog.Status.IN
            ));
        }

        new VehicleRepository().updateLatestEntryTime(vehicle, LocalDateTime.now());

        return CheckStatus.CHECK_IN_SUCCESS;
    }

    public CheckStatus checkOut(String plate) {
        plate = VehicleController.formatToLicensePlate(plate);

        if (!RegexPatterns.LICENSE_PLATE_PATTERN.matcher(plate).matches())
            return CheckStatus.INVALID_LICENSE_FORMAT;

        Vehicle vehicle = vehicleRepo.findVehicleByPlate(plate);
        if (vehicle == null)
            return CheckStatus.VEHICLE_NOT_FOUND;


        if (vehicle.getStatus() == EntryLog.Status.OUT)
            return CheckStatus.ALREADY_CHECKED_OUT;

        Optional<EntryLog> latest = repo.findLatestByLicense(plate);

        LocalDateTime time = LocalDateTime.now();

        if (latest.isPresent()) {
            EntryLog inLog = latest.get();
            repo.save(new EntryLog(
                    inLog.getCarName(),
                    inLog.getModel(),
                    plate,
                    inLog.getOwnerName(),
                    inLog.getEntryTime(),
                    time,
                    SessionManager.currentSession.getCurrentUser().getUsername(),
                    EntryLog.Status.OUT
            ));
        } else {
            repo.save(new EntryLog(
                    vehicle.getName(),
                    vehicle.getModel(),
                    vehicle.getLicensePlate(),
                    vehicle.getOwnerName(),
                    vehicle.getLatestEntryTime(),
                    time,
                    SessionManager.currentSession.getCurrentUser().getUsername(),
                    EntryLog.Status.OUT
            ));
        }

        new VehicleRepository().updateLatestExitTime(vehicle, time);

        return CheckStatus.CHECK_OUT_SUCCESS;
    }

    public ExportStatus exportEntryLogsToCSV(List<EntryLog> logs, Window window) {
        File documentsDir = new File(System.getProperty("user.home"), "Documents");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save export as");
        if (documentsDir.exists()) {
            fileChooser.setInitialDirectory(documentsDir);
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fileChooser.setInitialFileName("entry_logs.csv");
        File selectedFile = fileChooser.showSaveDialog(window);

        if (selectedFile != null) {
            if (!selectedFile.getName().toLowerCase().endsWith(".csv")) selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
            try {
                FileHandler.exportLogsToCSV(selectedFile.getAbsolutePath(), logs);
                return ExportStatus.EXPORT_SUCCESS;
            } catch (IOException e) {
                errorStatus = e;
                return ExportStatus.EXPORT_FAILURE;
            }
        } else {
            return ExportStatus.EXPORT_CANCELLED;
        }
    }

    public Throwable getErrorStatus() {
        if (errorStatus == null) errorStatus = new Throwable("An unexpected error occurred");
        return errorStatus;
    }
}

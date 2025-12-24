package org.example.controller;

import org.example.model.EntryLog;
import org.example.repo.EntryLogRepository;
import java.util.*;
import java.util.stream.Collectors;

public class EntryLogController {
    private EntryLogRepository repo;

    public EntryLogController() { repo = new EntryLogRepository(); }

    public List<EntryLog> getAllLogs() {
        return repo.getAllLogs();
    }

    public List<EntryLog> filterByLicensePlate(String licensePlate) {
        return repo.getAllLogs().
                stream()
                .filter(log -> log.getLicensePlate().equals(licensePlate))
                .collect(Collectors.toList());
    }
}

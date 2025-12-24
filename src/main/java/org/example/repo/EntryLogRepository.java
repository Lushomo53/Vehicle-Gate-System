package org.example.repo;

import org.example.model.EntryLog;

import java.time.LocalDateTime;
import java.util.List;

public class EntryLogRepository {
    public List<EntryLog> getAllLogs() {
        return List.of(
                new EntryLog(
                        "Toyota",
                        "Corolla",
                        "ABC-1234",
                        "Lushomo Lungo",
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now().minusDays(2),
                        EntryLog.Status.IN
                        ),
                new EntryLog(
                        "Honda",
                        "Civic",
                        "BCY-4367",
                        "George Cluny",
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().minusHours(2),
                        EntryLog.Status.IN
                ),
                new EntryLog(
                        "Ford",
                        "Ranger",
                        "CBA-1456",
                        "John Doe",
                        LocalDateTime.now().minusDays(5),
                        null,
                        EntryLog.Status.IN
                )
        );
    }
}

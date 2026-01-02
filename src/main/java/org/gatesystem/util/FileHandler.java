package org.gatesystem.util;

import org.gatesystem.model.EntryLog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileHandler {
    public static void exportLogsToCSV(String filename, List<EntryLog> logs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            writer.write("Car Name,Model,LicensePlate,Owner,Entry Time,Exit Time,Status");
            writer.newLine();
            for (EntryLog log : logs) {
                writer.write(
                        log.getCarName()
                        + "," + log.getModel()
                        + "," + log.getLicensePlate()
                        + "," + log.getOwnerName()
                        + "," + log.getEntryTime()
                        + "," + log.getExitTime()
                        + "," + log.getStatus()
                );
                writer.newLine();
            }
        }
    }

}

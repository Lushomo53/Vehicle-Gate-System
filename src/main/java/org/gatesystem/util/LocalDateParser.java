package org.gatesystem.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateParser {
    private static String[] formats = {
            "dd/MM/yyyy",
            "dd-MM-yyyy",
            "dd.MM.yyyy",
            "yyyy/MM/dd",
            "yyyy-MM-dd",
            "yyyy.MM.dd"
    };
    
    public static LocalDate parse(String text) {
        LocalDate date = null;
        for (String format : formats) {
            try {
                date = LocalDate.parse(text, DateTimeFormatter.ofPattern(format));
            } catch (Exception ignore) {}
        }

        return date;
    }
}

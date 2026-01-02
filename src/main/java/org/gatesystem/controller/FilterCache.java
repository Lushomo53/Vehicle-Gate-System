package org.gatesystem.controller;

import org.gatesystem.model.EntryLog;
import org.gatesystem.util.LocalDateParser;
import org.gatesystem.views.components.FilterPane;

import java.time.LocalDate;

public class FilterCache {
    public String search;
    public boolean filterBySelected;
    public String filterBy;
    public String dateFromText;
    public String dateToText;
    public LocalDate dateFrom;
    public LocalDate dateTo;
    public String entryStatus;

    public FilterCache(
            String search,
            boolean filterBySelected,
            String filterBy,
            String dateFromText,
            String dateToText,
            LocalDate dateFrom,
            LocalDate dateTo,
            String entryStatus
    ) {
        this.search = search;
        this.filterBySelected = filterBySelected;
        this.filterBy = filterBy;
        this.dateFromText = dateFromText;
        this.dateToText = dateToText;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.entryStatus = entryStatus;
    }

    public boolean matchesFilter(EntryLog log) {
        boolean matches = false;

        if (!search.isEmpty()) {
            if (filterBySelected) {
                switch (filterBy) {
                    case FilterPane.CAR_NAME_OPTION -> matches = log.getCarName().equalsIgnoreCase(search);
                    case FilterPane.LICENSE_PLATE_OPTION -> matches = log.getLicensePlate().equalsIgnoreCase(search);
                    case FilterPane.OWNER_NAME_OPTION -> matches = log.getOwnerName().equalsIgnoreCase(search);
                }
            } else {
                matches = true;
            }
        } else {
            matches = true;
        }

        if (!dateFromText.isEmpty()) {
            dateFrom = LocalDateParser.parse(dateFromText);
            if (dateFrom == null) dateFrom = LocalDate.now().minusDays(7);
        }

        if (!dateToText.isEmpty()) {
            dateTo = LocalDateParser.parse(dateToText);
            if (dateTo == null) dateFrom = LocalDate.now();
        }

        matches = log.getEntryTime().isAfter(dateFrom.atStartOfDay()) && log.getExitTime().isBefore(dateTo.plusDays(1).atStartOfDay());
        return matches;
    }
}

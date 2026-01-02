package org.gatesystem.controller;

public record VehicleFilterCache(
        String search,
        boolean filterBySelected,
        String filterBy
) {}


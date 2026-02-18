package com.smartfarming.engine;

import com.smartfarming.model.Crop;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SchedulePlanner {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    public record ScheduleEntry(String cropName, String plantingDate, String harvestDate, int durationDays) {}

    public List<ScheduleEntry> buildSchedule(List<Crop> crops, String season) {
        // Planting dates based on Sri Lankan seasons
        LocalDate plantingDate = season.equalsIgnoreCase("Yala")
                ? LocalDate.of(LocalDate.now().getYear(), 4, 10)  // Yala: April
                : LocalDate.of(LocalDate.now().getYear(), 10, 1); // Maha: October

        return crops.stream().map(crop -> {
            LocalDate harvest = plantingDate.plusDays(crop.getGrowthDays());
            return new ScheduleEntry(
                    crop.getName(),
                    plantingDate.format(FMT),
                    harvest.format(FMT),
                    crop.getGrowthDays()
            );
        }).toList();
    }
}

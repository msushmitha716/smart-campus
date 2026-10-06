package com.smartcampus.controller;

import com.smartcampus.model.CampusLocation;
import com.smartcampus.model.Report;
import com.smartcampus.repository.CampusLocationRepository;
import com.smartcampus.repository.ReportRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private final ReportRepository reportRepository;
    private final CampusLocationRepository locationRepository;

    public AdminController(ReportRepository reportRepository,
                           CampusLocationRepository locationRepository) {
        this.reportRepository = reportRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/api/reports")
    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    @PostMapping("/api/locations")
    public CampusLocation addLocation(
            @RequestParam String name) {

        String locationName = name.trim();

        if (locationName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Location name cannot be empty."
            );
        }

        if (locationRepository.findByName(locationName).isPresent()) {
            throw new IllegalArgumentException(
                    "Location already exists."
            );
        }

        CampusLocation location =
                new CampusLocation(
                        locationName,
                        "Low Crowd",
                        false
                );

        return locationRepository.save(location);
    }

    @DeleteMapping("/api/locations/{id}")
    public String deleteLocation(
            @PathVariable Long id) {

        CampusLocation location =
                locationRepository.findById(id)
                        .orElse(null);

        if (location == null) {
            return "Location not found";
        }

        String locationName =
                location.getName();

        List<Report> reports =
                reportRepository.findAll();

        for (Report report : reports) {

            if (report.getLocation().equals(locationName)) {
                reportRepository.delete(report);
            }
        }

        locationRepository.delete(location);

        return "Location deleted successfully";
    }
}
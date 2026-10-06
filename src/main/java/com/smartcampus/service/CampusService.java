package com.smartcampus.service;

import com.smartcampus.model.CampusLocation;
import com.smartcampus.model.Report;
import com.smartcampus.repository.CampusLocationRepository;
import com.smartcampus.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusService {

    private final CampusLocationRepository locationRepository;
    private final ReportRepository reportRepository;

    public CampusService(CampusLocationRepository locationRepository,
                         ReportRepository reportRepository) {
        this.locationRepository = locationRepository;
        this.reportRepository = reportRepository;
    }

    public List<CampusLocation> getAllLocations() {
        updateAllLocationStatuses();
        return locationRepository.findAll();
    }

    public void submitReport(String locationName, String issue) {

        Report report = new Report(locationName, issue);
        reportRepository.save(report);

        updateAllLocationStatuses();
    }

    private void updateAllLocationStatuses() {

        List<CampusLocation> locations =
                locationRepository.findAll();

        List<Report> reports =
                reportRepository.findAll();

        int totalCampusVotes = reports.size();

        for (CampusLocation location : locations) {

            int locationVotes = (int) reports.stream()
                    .filter(report ->
                            report.getLocation().equals(location.getName()))
                    .count();

            int crowdedVotes = (int) reports.stream()
                    .filter(report ->
                            report.getLocation().equals(location.getName())
                            && report.getIssue().equals("Crowded Area"))
                    .count();

            int blockedVotes = (int) reports.stream()
                    .filter(report ->
                            report.getLocation().equals(location.getName())
                            && report.getIssue().equals("Blocked Pathway"))
                    .count();

            location.setTotalVotes(locationVotes);
            location.setCrowdedVotes(crowdedVotes);
            location.setBlockedVotes(blockedVotes);

            double percentage = 0;

            if (totalCampusVotes > 0) {
                percentage =
                        (locationVotes * 100.0) / totalCampusVotes;
            }

            if (percentage < 20) {

                location.setCrowdStatus("Low Crowd");

            } else if (percentage < 50) {

                location.setCrowdStatus("Medium Crowd");

            } else {

                location.setCrowdStatus("High Crowd");
            }

            location.setBlocked(blockedVotes > 0);

            locationRepository.save(location);
        }
    }
}
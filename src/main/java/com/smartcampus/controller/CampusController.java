package com.smartcampus.controller;

import com.smartcampus.model.CampusLocation;
import com.smartcampus.service.CampusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CampusController {

    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    @GetMapping("/api/locations")
    public List<CampusLocation> getLocations() {
        return campusService.getAllLocations();
    }
}
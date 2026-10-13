package com.smartcampus.controller;

import com.smartcampus.model.CampusLocation;
import com.smartcampus.repository.CampusLocationRepository;
import com.smartcampus.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/manage")
public class AdminPanelController {

    private final JdbcTemplate jdbc;
    private final CampusLocationRepository locationRepository;
    private final UserRepository userRepository;

    public AdminPanelController(JdbcTemplate jdbc,
                                CampusLocationRepository locationRepository,
                                UserRepository userRepository) {
        this.jdbc = jdbc;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/locations")
    public String locations(Model model) {
        model.addAttribute("locations",
                jdbc.queryForList("SELECT id, name, crowd_status, blocked FROM campus_location ORDER BY name"));
        return "admin-locations";
    }

    @PostMapping("/locations/add")
    public String addLocation(@RequestParam String name, RedirectAttributes ra) {
        String n = name == null ? "" : name.trim();
        if (n.isEmpty()) {
            ra.addFlashAttribute("error", "Location name cannot be empty.");
            return "redirect:/admin/manage/locations";
        }
        Integer exists = jdbc.queryForObject(
                "SELECT COUNT(*) FROM campus_location WHERE LOWER(name) = LOWER(?)", Integer.class, n);
        if (exists != null && exists > 0) {
            ra.addFlashAttribute("error", "'" + n + "' already exists.");
            return "redirect:/admin/manage/locations";
        }
        CampusLocation loc = new CampusLocation();
        loc.setName(n);
        locationRepository.save(loc);
        ra.addFlashAttribute("success", "Location '" + n + "' added.");
        return "redirect:/admin/manage/locations";
    }

    @PostMapping("/locations/delete")
    public String deleteLocation(@RequestParam long id, RedirectAttributes ra) {
        int rows = jdbc.update("DELETE FROM campus_location WHERE id = ?", id);
        ra.addFlashAttribute(rows > 0 ? "success" : "error",
                rows > 0 ? "Location deleted." : "Location not found.");
        return "redirect:/admin/manage/locations";
    }

    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", userRepository.findAll());
        return "admin-students";
    }
}
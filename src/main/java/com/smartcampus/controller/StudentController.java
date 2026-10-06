package com.smartcampus.controller;

import com.smartcampus.service.CampusService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentController {

    private final CampusService campusService;

    public StudentController(CampusService campusService) {
        this.campusService = campusService;
    }

    @PostMapping("/report")
    @ResponseBody
    public String reportIssue(@RequestParam String location,
                              @RequestParam String issue) {

        campusService.submitReport(location, issue);

        return """
                <html>
                <head>
                    <title>Report Submitted</title>
                </head>
                <body>
                    <h1>Report Submitted Successfully</h1>
                    <p>Location: %s</p>
                    <p>Issue: %s</p>
                    <p>The campus information has been updated.</p>
                    <a href="/">Back to Student Portal</a>
                </body>
                </html>
                """.formatted(location, issue);
    }
}
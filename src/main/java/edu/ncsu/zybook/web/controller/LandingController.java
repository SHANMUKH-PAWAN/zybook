package edu.ncsu.zybook.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landing")
public class LandingController {

    @GetMapping("/admin")
    public String adminLanding() {
        return "landing/adminlanding";
    }

    @GetMapping("/faculty")
    public String facultyLanding() {
        return "landing/facultylanding";
    }

    @GetMapping("/student")
    public String studentLanding() {
        return "landing/studentlanding";
    }

    @GetMapping("/ta")
    public String taLanding() {
        return "landing/talanding";
    }
}
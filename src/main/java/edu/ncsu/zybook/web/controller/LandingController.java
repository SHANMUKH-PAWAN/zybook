package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.persistence.repository.UserRepository;
import edu.ncsu.zybook.security.CustomUserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landing")
public class LandingController {

    private UserRepository userRepository;

    public LandingController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String landing(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return "redirect:/landing/"+ userRepository.findById(currentUser.getId()).get().getRoleName();
    }

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
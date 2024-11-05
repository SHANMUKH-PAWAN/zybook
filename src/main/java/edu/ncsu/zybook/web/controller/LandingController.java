package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.persistence.repository.UserRepository;
import edu.ncsu.zybook.security.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        String role = userRepository.findById(currentUser.getId()).get().getRoleName();
        if(role.equals("")) return "redirect:/landing/student";
        if(role.equals("student")) return "redirect:/landing/studentnew";

        return "redirect:/landing/"+ role;
    }

    @GetMapping("/admin")
    public String adminLanding() {
        return "landing/adminlanding";
    }

    @GetMapping("/faculty")
    public String facultyLanding(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        model.addAttribute("userId",currentUser.getId());
        return "landing/facultylanding";
    }

    @GetMapping("/student")
    public String studentLanding() {
        return "landing/studentlanding";
    }

    @GetMapping("/studentnew")
    public String studentnewLanding() {
        return "landing/studentnewlanding";
    }

    @GetMapping("/ta")
    public String taLanding() {
        return "landing/talanding";
    }
}
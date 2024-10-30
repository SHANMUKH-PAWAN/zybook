package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.User;
import edu.ncsu.zybook.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final IUserService userService;

    @Autowired
    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String loginPage(@RequestParam(name = "role", required = false) String roleName, Model model) {
        model.addAttribute("role", roleName != null ? roleName : "Unknown");
        return "login/login";
    }

    @PostMapping
    public String authenticateUser(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            User user = userOptional.get();
            String role = user.getRoleName();

            if (role == null) {
                model.addAttribute("error", "User role is not assigned.");
                return "login/login";
            }

            switch (role.toLowerCase()) {
                case "student":
                    return "redirect:/landing/student";
                case "faculty":
                    return "redirect:/landing/faculty";
                case "admin":
                    return "redirect:/landing/admin";
                case "ta":
                    return "redirect:/landing/ta";
                default:
                    model.addAttribute("error", "Invalid role assigned to user.");
                    return "login/login";
            }
        } else {
            model.addAttribute("error", "Invalid email or password.");
            return "login/login";
        }
    }
}

package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.Notification;
import edu.ncsu.zybook.domain.model.User;
import edu.ncsu.zybook.security.CustomUserDetails;
import edu.ncsu.zybook.security.SecurityConfig;
import edu.ncsu.zybook.service.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;
    SecurityConfig securityConfig;

    public UserController(IUserService userService, SecurityConfig securityConfig) {
        this.userService = userService;
        this.securityConfig = securityConfig;
    }

    // Allow user to view their own profile or allow access to admins
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String getUser(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable int id, Model model) {
        System.out.println("Current logged in User "+ currentUser.getId());
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/user";
        } else {
            return "user/not-found";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "user/create";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createUser(@ModelAttribute User user) {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.create(user);
        return "redirect:/users";
    }

    // Allow user to edit their own profile or allow access to admins
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/create";
        } else {
            return "user/not-found";
        }
    }

    // Allow user to update their own profile or allow access to admins
    @PreAuthorize("#user.id == principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public String updateUser(@ModelAttribute User user) {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(user);
        return "redirect:/users";
    }

    // Only admins can delete a user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/notifications/{id}")
    public String getNotifications(@PathVariable int id, Model model) {
        List<Notification> notifications = userService.getNotification(id);
        model.addAttribute("notifications", notifications);
        return "user/notifications";
    }
}

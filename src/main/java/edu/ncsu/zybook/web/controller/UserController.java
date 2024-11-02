package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.User;
import edu.ncsu.zybook.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {

        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable int id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/user";
        } else {
            return "user/not-found";
        }
    }

    @GetMapping("/all")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping
    public String getCourseUsers(Model model, String courseId) {
        List<User> users = userService.getWaitingList(courseId);
        model.addAttribute("users", users);
        model.addAttribute("courseId", courseId);
        return "user/waitlist";
    }

    @GetMapping("/waiting/{id}")
    public String getWaitingList(@PathVariable String id, Model model) {
        List<User> users = userService.getWaitingList(id);
        model.addAttribute("users", users);
        model.addAttribute("courseId", id);
        return "user/waitlist";
    }

    @PostMapping("/approve/{courseId}/{userId}")
    public String approve(@PathVariable String courseId, @PathVariable int userId, Model model) {
        userService.approve(courseId, userId);
        //model.addAttribute("courseId", courseId);
        return "redirect:/users/waiting/" + courseId;
    }

    @PostMapping("/reject/{courseId}/{userId}")
    public String reject(@PathVariable String courseId, @PathVariable int userId){
        userService.reject(courseId, userId);
        return "redirect:/users/waiting/" + courseId;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "user/create";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user) {
        userService.create(user);
        return "redirect:/users";
    }

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

    @PutMapping("/{id}")
    public String updateUser(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.delete(id);
        return "redirect:/users";
    }
}

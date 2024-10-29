package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.UserRegistersCourse;
import edu.ncsu.zybook.service.ICourseService;
import edu.ncsu.zybook.service.IUserRegistersCourseService;
import edu.ncsu.zybook.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/enrollments")
public class UserRegistersCourseController {
    IUserService userService;
    ICourseService courseService;
    IUserRegistersCourseService userRegistersCourseService;

    @GetMapping("/{id}")
    public String getAllCourses(@PathVariable int id, Model model) {
        List <UserRegistersCourse> courses = userRegistersCourseService.findAllByUser(id);
        model.addAttribute("courses", courses);
        return "enrollments/list";
    }

    @GetMapping("/{userId}/{courseId}")
    public String getEnrollment(@PathVariable int userId, @PathVariable String courseId, Model model) {
        Optional<UserRegistersCourse> userRegistersCourse = userRegistersCourseService.findById(userId, courseId);
        if (userRegistersCourse.isPresent()) {
            model.addAttribute("enrollment", userRegistersCourse.get());
            return "enrollments/enrollment";
        } else{
            return "enrollments/not-found";
        }
    }

    @GetMapping("/new")
    public String newEnrollment(Model model) {
        model.addAttribute("enrollment", new UserRegistersCourse());
        return "enrollments/create";
    }
}

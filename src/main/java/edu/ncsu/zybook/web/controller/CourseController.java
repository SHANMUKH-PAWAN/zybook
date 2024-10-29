package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.Course;
import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.domain.model.User;
import edu.ncsu.zybook.service.ICourseService;
import edu.ncsu.zybook.service.ITextbookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService courseService;
    private final ITextbookService textbookService;

    public CourseController(ICourseService courseService, ITextbookService textbookService) {
        this.courseService = courseService;
        this.textbookService = textbookService;
    }
    @GetMapping("/{id}")
    public String getCourse(@PathVariable String id, Model model) {
        Optional<Course> coursevariable = courseService.findById(id);
        if (coursevariable.isPresent()) {
            Course course = coursevariable.get();

            Optional<Textbook> textbook= textbookService.findById(course.getTbookId());
            if (textbook.isPresent())
            {model.addAttribute("textbook", textbook.get());
            }
            else {model.addAttribute("textbook", null);
            }
            model.addAttribute("course", course);
            return "course/course";
        }else {
            return "course/not-found";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/create";
    }

    @PostMapping
    public String createCourse(@ModelAttribute Course course) {
        courseService.create(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Optional<Course> course = courseService.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "course/create";
        } else {
            return "course/not-found";
        }
    }

    @PutMapping("/update")
    public String updateCourse(@ModelAttribute Course course) {
        courseService.update(course);
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable String id) {
        courseService.delete(id);
        return "redirect:/courses";
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "course/list";
    }

}


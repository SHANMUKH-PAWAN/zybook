package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.ActiveCourse;
import edu.ncsu.zybook.domain.model.Course;
import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.service.ICourseService;
import edu.ncsu.zybook.service.ITextbookService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        List<Integer> inUseTbooks = courseService.findAll().stream().map(Course::getTbookId).toList();
        List<Integer> tbooks = textbookService.getAllTextbooks(0,100,"uid","ASC").stream().map(Textbook::getUid).toList();
        List<Integer> unUsedTbookIds = new ArrayList<>(tbooks);
        unUsedTbookIds.removeAll(inUseTbooks);
        List<Textbook> unUsedTbooks = unUsedTbookIds.stream()
                .map(textbookService::findById)
                .flatMap(Optional::stream)
                .toList();
        model.addAttribute("unUsedTbooks", unUsedTbooks);
        return "course/create";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public String createCourse(@ModelAttribute Course course) {
        courseService.create(course);
        return "redirect:/courses";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
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

    @PreAuthorize("hasAnyRole( 'ADMIN')")
    @PutMapping("/update")
    public String updateCourse(@ModelAttribute Course course) {
        System.out.println(course);
        courseService.update(course);
        return "redirect:/courses";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable String id) {
        courseService.delete(id);
        return "redirect:/courses";
    }

    @GetMapping
    public String getAllCourses(Model model) {
        System.out.println("Inside get all courses controller");
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "course/list";
    }
    @GetMapping("/active")
    public String getActiveCourse(@RequestParam("userId") int userId, Model model) {
        List<ActiveCourse> activeCourses = courseService.getActiveCourses(userId);
        System.out.println(activeCourses);
        model.addAttribute("courses", activeCourses);
        return "course/list";
    }
    @GetMapping("/evaluation")
    public String getEvaluationCourse(@RequestParam("userId") int userId, Model model) {
        List<Course> evalutionCourse = courseService.getEvaluationCourse(userId);
        model.addAttribute("courses", evalutionCourse);
        return "course/list";
    }
    @GetMapping("/all")
    public String getAllCoursesForUser(@RequestParam("userId") int userId, Model model) {
        List<Course> courses = courseService.getAllCoursesForUser(userId);
        model.addAttribute("courses", courses);
        return "course/list";
    }
}
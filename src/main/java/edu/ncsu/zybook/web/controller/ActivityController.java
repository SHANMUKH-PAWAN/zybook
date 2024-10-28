package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.User;
import edu.ncsu.zybook.service.IActivityService;
import edu.ncsu.zybook.service.impl.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/activity")
public class ActivityController {
    private final IActivityService activityService;

    public ActivityController(IActivityService activityService) {
        this.activityService = activityService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getActivity(@PathVariable int id) {
        Optional<User> user = activityService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public  ResponseEntity<Void> createUser(@RequestBody User user) {
        System.out.println("Entered post!!");
        userService.create(user);
        return ResponseEntity.ok().build();
    }
}

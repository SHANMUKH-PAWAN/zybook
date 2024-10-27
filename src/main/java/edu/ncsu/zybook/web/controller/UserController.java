package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.domain.model.User;
import edu.ncsu.zybook.service.IUserService;
import edu.ncsu.zybook.service.impl.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public  ResponseEntity<Void> createUser(@RequestBody User user) {
        System.out.println("Entered post!!");
        userService.create(user);
        return ResponseEntity.ok().build();
    }
    @PutMapping
    public ResponseEntity<String> updateUser( @RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok("User updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTextbook(@PathVariable int id){
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<User> getAllUsers(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        List<User> users = userService.getAllUsers(offset, limit, sortBy, sortDirection);
        return users;
    }
}

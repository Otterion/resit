package ru.shers.resit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shers.resit.entities.Retake;
import ru.shers.resit.entities.Role;
import ru.shers.resit.entities.User;
import ru.shers.resit.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        System.out.println("here");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<Collection<Role>> getUserRoles(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(user.getRoles()))
                .orElse(ResponseEntity.notFound().build());
    }
}

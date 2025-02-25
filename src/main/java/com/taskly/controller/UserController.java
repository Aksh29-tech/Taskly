package com.taskly.controller;

import com.taskly.entity.User;
import com.taskly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public User addUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/getUser/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return userService.findByUsername(username)
                        .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String token) {
        userService.verifyUser(token);
        return "Account successfully verified!";
    }

    @GetMapping("/getLoggedInUser")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }
}

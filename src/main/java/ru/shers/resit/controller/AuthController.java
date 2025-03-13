package ru.shers.resit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shers.resit.entities.User;
import ru.shers.resit.service.UserService;
import ru.shers.resit.util.JwtTokenUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            final String token = jwtTokenUtils.generateToken(userDetails);
            System.out.println(token);
            return ResponseEntity.ok(token);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные имя пользователя или пароль");
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println(user.getUsername());
        try {
            User registeredUser = userService.createUser(user);
            return ResponseEntity.ok(registeredUser);
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

}

package org.ideamap.user;

import org.ideamap.security.JwtService;
import org.ideamap.user.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // 1. Marks this as a controller for RESTful services
@RequestMapping("/api/users") // 2. All endpoints in this class will start with /api/users
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login") // 4. Maps HTTP POST requests to /api/users/login
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        MongoUser user = userService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (user != null) {
            String jwtToken = jwtService.generateToken(user);

            // Return the token in a JSON response
            // It's best practice to return an object/Map rather than a raw string.
            Map<String, String> responseBody = Map.of(
                    "message", "Login successful!",
                    "token", jwtToken);

            return ResponseEntity.ok(responseBody);
        } else {
            // 6. Return a 401 Unauthorized response on failure
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }
}
package org.ideamap.user;

import org.ideamap.user.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Marks this as a controller for RESTful services
@RequestMapping("/api/users") // 2. All endpoints in this class will start with /api/users
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    // 3. Inject the UserService to handle business logic
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login") // 4. Maps HTTP POST requests to /api/users/login
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = userService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (isAuthenticated) {
            // 5. Return a 200 OK response on success
            return ResponseEntity.ok("Login successful!");
        } else {
            // 6. Return a 401 Unauthorized response on failure
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }
}
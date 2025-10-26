package org.ideamap.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // 1. Marks this as a Spring service component
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // 2. Inject the UserRepository to access the database
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String password) {
        // 3. Find the user by their username
       MongoUser user = userRepository.findByUsername(username);

        if (user != null) {
            // 4. Check if the provided password matches the one in the database
            // In a real app, passwords should be hashed!
            return passwordEncoder.matches(password, user.getPassword());
        }

        // 5. If user is not found, authentication fails
        return false;
    }
}
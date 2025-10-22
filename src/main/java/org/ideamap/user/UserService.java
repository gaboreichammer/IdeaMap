package org.ideamap.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // 1. Marks this as a Spring service component
public class UserService {

    private final UserRepository userRepository;

    // 2. Inject the UserRepository to access the database
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user by checking their username and password.
     * @param username The username to check.
     * @param password The password to check.
     * @return true if credentials are valid, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        // 3. Find the user by their username
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 4. Check if the provided password matches the one in the database
            // In a real app, passwords should be hashed!
            return user.getPassword().equals(password);
        }

        // 5. If user is not found, authentication fails
        return false;
    }
}
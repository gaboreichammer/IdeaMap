package org.ideamap.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marks this as a Spring repository bean
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA will automatically create a query for this method:
    // "SELECT * FROM users WHERE username = ?"
    Optional<User> findByUsername(String username);
}
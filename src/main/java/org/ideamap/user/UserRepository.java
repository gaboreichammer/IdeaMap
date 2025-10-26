package org.ideamap.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marks this as a Spring repository bean
public interface UserRepository extends MongoRepository<MongoUser, Long> {
    MongoUser findByUsername(String username);
}
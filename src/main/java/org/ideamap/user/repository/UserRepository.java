package org.ideamap.user.repository;
import org.ideamap.user.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this as a Spring repository bean
public interface UserRepository extends MongoRepository<MongoUser, Long> {
    MongoUser findByUsername(String username);
}
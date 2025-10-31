package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.Idea;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaRepository extends MongoRepository<Idea, String> {
    // Spring Data automatically implements basic CRUD (save, findById, findAll, etc.)
    // You can define custom query methods here, e.g.,
    Idea findByName(String name);

    Idea findById(ObjectId id);
}

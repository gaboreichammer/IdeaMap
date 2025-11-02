package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaRepository extends MongoRepository<IdeaEntity, String> {
    // Spring Data automatically implements basic CRUD (save, findById, findAll, etc.)
    // You can define custom query methods here, e.g.,
    IdeaEntity findByName(String name);

    IdeaEntity findById(ObjectId id);
}

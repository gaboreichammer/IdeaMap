package org.ideamap.idea.repository;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<TagEntity, ObjectId> {
    TagEntity findByName(String name);
}

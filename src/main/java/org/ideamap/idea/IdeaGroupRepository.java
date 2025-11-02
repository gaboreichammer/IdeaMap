package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaGroupEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaGroupRepository extends MongoRepository<IdeaGroupEntity, String> {
    IdeaGroupEntity findByName(String name);

    List<IdeaGroupEntity> findByUserId(ObjectId userId);
}

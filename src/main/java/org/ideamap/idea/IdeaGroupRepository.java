package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaGroupRepository extends MongoRepository<IdeaGroup, String> {
    IdeaGroup findByName(String name);

    List<IdeaGroup> findByUserId(ObjectId userId);
}

package org.ideamap.idea;

import org.ideamap.idea.model.IdeaGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaGroupRepository extends MongoRepository<IdeaGroup, String> {
    IdeaGroup findByName(String name);
}

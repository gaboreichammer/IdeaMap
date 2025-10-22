package org.ideamap.idea;

import org.ideamap.idea.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    Tag findByName(String name);
}

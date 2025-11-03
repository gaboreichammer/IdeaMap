package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends MongoRepository<IdeaEntity, ObjectId> {
    IdeaEntity findByName(String name);

    /**
     * Finds a list of IdeaEntities by their IDs and projects the result
     * into the IdeaProjection interface (only returning 'id' and 'name').
     * @param ids A list of String IDs (hex strings) to search for.
     * @return A list of objects containing only the ID and name of the ideas.
     */
    List<IdeaProjection> findByIdIn(List<ObjectId> ids);
}

package org.ideamap.idea;


import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaGroup;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdeaGroupService {

    private final IdeaGroupRepository ideaGroupRepository;

    public IdeaGroupService(IdeaGroupRepository ideaGroupRepository) {
        this.ideaGroupRepository = ideaGroupRepository;
    }

    public List<IdeaGroup> getIdeaGroupForUser(String userId) {
       return ideaGroupRepository.findByUserId(new ObjectId(userId));
    }
}

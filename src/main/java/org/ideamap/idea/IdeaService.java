package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.Idea;
import org.springframework.stereotype.Service;

@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;

    public IdeaService(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    public Idea findById(String id) {
        return ideaRepository.findById(new ObjectId(id));
    }
}

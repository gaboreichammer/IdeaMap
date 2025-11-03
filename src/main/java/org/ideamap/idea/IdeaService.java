package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.dto.IdeaWithTagsLinksDto;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaProjection;
import org.ideamap.idea.model.TagEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final TagRepository tagRepository;

    public IdeaService(IdeaRepository ideaRepository, TagRepository tagRepository) {
        this.ideaRepository = ideaRepository;
        this.tagRepository = tagRepository;
    }

    public IdeaWithTagsLinksDto findIdeaWithTags(String id) {
        IdeaEntity idea = ideaRepository.findById(new ObjectId(id)).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Idea with ID " + id + " not found"
        ));

        List<ObjectId> tagObjectIds = idea.getTagIds().stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .toList();

        List<TagEntity> tags = tagRepository.findAllById(tagObjectIds);

        List<ObjectId> linkedIdeaObjectIds = idea.getLinkedIdeaIds().stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .toList();

        List<IdeaProjection> linkedIdeas = ideaRepository.findByIdIn(linkedIdeaObjectIds);

        return new IdeaWithTagsLinksDto(idea, tags, linkedIdeas);
    }
}

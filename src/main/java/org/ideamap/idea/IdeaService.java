package org.ideamap.idea;

import org.bson.types.ObjectId;
import org.ideamap.idea.dto.IdeaWithTagsLinksDto;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaProjection;
import org.ideamap.idea.model.TagEntity;
import org.ideamap.idea.repository.IdeaRepository;
import org.ideamap.idea.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        List<ObjectId> tagObjectIds = Optional.ofNullable(idea.getTagIds())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .toList();

        List<TagEntity> tags = tagRepository.findAllById(tagObjectIds);

        List<ObjectId> linkedIdeaObjectIds = Optional.ofNullable(idea.getLinkedIdeaIds())
                .orElseGet(Collections::emptyList).stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .toList();

        List<IdeaProjection> linkedIdeas = ideaRepository.findByIdIn(linkedIdeaObjectIds);

        return new IdeaWithTagsLinksDto(idea, tags, linkedIdeas);
    }
}

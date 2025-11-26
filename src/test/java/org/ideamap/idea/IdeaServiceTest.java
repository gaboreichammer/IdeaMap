package org.ideamap.idea;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit5.JMockitExtension;
import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaProjection;
import org.ideamap.idea.model.TagEntity;
import org.ideamap.idea.repository.IdeaRepository;
import org.ideamap.idea.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JMockitExtension.class)
class IdeaServiceTest {
    @Tested
    IdeaService ideaService;

    @Injectable
    IdeaRepository ideaRepository;

    @Injectable
    TagRepository tagRepository;

    @Injectable
    IdeaEntity idea; // returned from ideaRepository.findById(...)

    @Injectable
    TagEntity tag; // element returned from tagRepository.findAllById(...)

    @Injectable
    IdeaProjection linkedIdea; // element returned from ideaRepository.findByIdIn(...)

    @Test
    void findIdeaWithTags_returnsDtoAndCallsRepositories() {
        String ideaId = new ObjectId().toHexString();
        List<String> tagIds = List.of(new ObjectId().toHexString());
        List<String> linkedIds = List.of(new ObjectId().toHexString());

        new Expectations() {{
            ideaRepository.findById((ObjectId) any);
            result = Optional.of(idea);

            idea.getTagIds();
            result = tagIds;

            idea.getLinkedIdeaIds();
            result = linkedIds;

            tagRepository.findAllById((List<ObjectId>) any);
            result = List.of(tag);

            ideaRepository.findByIdIn((List<ObjectId>) any);
            result = List.of(linkedIdea);

            idea.getName();
            result = "expected-name";
        }};

        var dto = ideaService.findIdeaWithTags(ideaId);

        assertEquals("expected-name", dto.getName());
        assertEquals(List.of(tag), dto.getTags());
        assertEquals(List.of(linkedIdea), dto.getLinkedIdeas());

        // capture arguments passed to repositories
        List<List<ObjectId>> capturedTagArgs = new ArrayList<>();
        List<List<ObjectId>> capturedLinkArgs = new ArrayList<>();

        new Verifications() {{
            tagRepository.findAllById((List<ObjectId>) withCapture(capturedTagArgs));
            times = 1;

            ideaRepository.findByIdIn((List<ObjectId>) withCapture(capturedLinkArgs));
            times = 1;
        }};

        assertEquals(1, capturedTagArgs.size());
        assertEquals(1, capturedTagArgs.get(0).size());
        assertEquals(tagIds.get(0), capturedTagArgs.get(0).get(0).toHexString());

        assertEquals(1, capturedLinkArgs.size());
        assertEquals(1, capturedLinkArgs.get(0).size());
        assertEquals(linkedIds.get(0), capturedLinkArgs.get(0).get(0).toHexString());
    }

    @Test
    void findIdeaWithTags_nonExistingIdea_throwsNotFound() {
        String ideaId = new ObjectId().toHexString();

        new Expectations() {{
            ideaRepository.findById((ObjectId) any);
            result = Optional.empty();
        }};

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> ideaService.findIdeaWithTags(ideaId));
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}
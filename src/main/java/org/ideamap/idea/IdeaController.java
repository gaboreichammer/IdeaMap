package org.ideamap.idea;

import org.ideamap.idea.model.Idea;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/idea") // Re-use the /api/idea path, or create a new one like /api/ideas
public class IdeaController {

    private final IdeaService ideaService;

    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    /**
     * Retrieves a single Idea by its ID (which is stored in IdeaGroup.linkedIdeaId).
     * NOTE: Security check should be done in IdeaService to ensure the authenticated
     * user has access to this Idea, though for simplicity we omit the Jwt here.
     * * @param ideaId The ID of the Idea to fetch.
     * @return A ResponseEntity containing the Idea or 404 Not Found.
     */
    @GetMapping("/linked/{ideaId}")
    public ResponseEntity<Idea> getLinkedIdea(@PathVariable String ideaId) {
        Idea idea = ideaService.findById(ideaId);

        if (idea == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(idea);
    }
}
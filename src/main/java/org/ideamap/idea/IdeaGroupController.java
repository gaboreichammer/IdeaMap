package org.ideamap.idea;

import org.ideamap.idea.model.IdeaGroupEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/idea")
public class IdeaGroupController {

    private final IdeaGroupService ideaGroupService;

    public IdeaGroupController(IdeaGroupService ideaGroupService) {
        this.ideaGroupService = ideaGroupService;
    }

    /**
     * Retrieves all IdeaGroups belonging to the currently authenticated user.
     * The user ID is securely extracted from the JWT token in the Authorization header.
     * * @param jwt The authenticated JWT token provided by Spring Security's Resource Server.
     * @return A ResponseEntity containing a list of IdeaGroup entities (may be empty).
     */
    @GetMapping("/getIdeaGroup")
    public ResponseEntity<List<IdeaGroupEntity>> getIdeaGroup(@AuthenticationPrincipal Jwt jwt) {
        // Retrieve the custom 'userId' claim that was embedded during token generation.
        String userIdString = jwt.getClaimAsString("userId");

        // Error handling for missing claim (should not happen if token generation is correct)
        if (userIdString == null) {
            // In a secure application, this might return 403 Forbidden or 401 Unauthorized
            // but for simplicity, we return an empty list with a 400 Bad Request status.
            return ResponseEntity.badRequest().body(List.of());
        }

        // Use the extracted user ID to query the database
        List<IdeaGroupEntity> ideaGroupEntities = ideaGroupService.getIdeaGroupForUser(userIdString);

        // Best Practice: Return 200 OK even if the list is empty, indicating a successful query
        return ResponseEntity.ok(ideaGroupEntities);
    }
}

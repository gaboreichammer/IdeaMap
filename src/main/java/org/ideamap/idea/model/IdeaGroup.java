package org.ideamap.idea.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "ideaGroups") // Maps this class to a MongoDB collection named 'ideaGroups'
public class IdeaGroup {

    @Id // Corresponds to "idea group id"
    private String id;

    private String name;

    // This is the "linked idea id" property.
    private String linkedIdeaId;
    public IdeaGroup(String name) {
        this.name = name;
    }
    public IdeaGroup(String name, String linkedIdeaId) {
        this.name = name;
        this.linkedIdeaId = linkedIdeaId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkedIdeaId() {
        return linkedIdeaId;
    }

    public void setLinkedIdeaId(String linkedIdeaId) {
        this.linkedIdeaId = linkedIdeaId;
    }
}

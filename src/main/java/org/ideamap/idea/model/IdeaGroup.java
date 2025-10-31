package org.ideamap.idea.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "ideaGroups") // Maps this class to a MongoDB collection named 'ideaGroups'
public class IdeaGroup {

    @Id
    private ObjectId id;

    private String name;

    private ObjectId userId;

    private String linkedIdeaId;

    public IdeaGroup() {
    };

    public IdeaGroup(String name) {
        this.name = name;
    }

    public IdeaGroup(String name, String linkedIdeaId, ObjectId userId) {
        this.name = name;
        this.linkedIdeaId = linkedIdeaId;
        this.userId = userId;
    }

    public ObjectId getId() {
        return id;
    }

    public String getIdAsString() {
        return this.id != null ? this.id.toHexString() : null;
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

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}

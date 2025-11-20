package org.ideamap.idea.dto;

import org.bson.types.ObjectId;

public class IdeaGroupDto {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getLinkedIdeaId() {
        return linkedIdeaId;
    }

    public void setLinkedIdeaId(String linkedIdeaId) {
        this.linkedIdeaId = linkedIdeaId;
    }


    public IdeaGroupDto(String id, String name, ObjectId userId, String linkedIdeaId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.linkedIdeaId = linkedIdeaId;
    }

    private String id;
    private String name;

    private ObjectId userId;

    private String linkedIdeaId;

}

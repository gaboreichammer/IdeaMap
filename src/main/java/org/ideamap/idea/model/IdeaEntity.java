package org.ideamap.idea.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "ideas") // Maps this class to a MongoDB collection named 'ideas'
public class IdeaEntity {

    @Id // Corresponds to "idea id"
    private ObjectId id;

    private ObjectId userId;

    private String name;

    private String text;

    private String link;

    private String image;

    // List of linked ideas: You should store the IDs of the linked Idea documents.
    // This is the standard "referencing" approach in MongoDB.
    private List<String> linkedIdeaIds;

    // List of tags: You should also store the IDs of the Tag documents.
    private List<String> tagIds;

    public IdeaEntity(){};

    public IdeaEntity(String name) {
        this.name = name;
    }

    public IdeaEntity(String name, String text, String link, String image, List<String> linkedIdeaIds, List<String> tagIds, ObjectId userId) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.image = image;
        this.linkedIdeaIds = linkedIdeaIds;
        this.tagIds = tagIds;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getLinkedIdeaIds() {
        return linkedIdeaIds;
    }

    public void setLinkedIdeaIds(List<String> linkedIdeaIds) {
        this.linkedIdeaIds = linkedIdeaIds;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}

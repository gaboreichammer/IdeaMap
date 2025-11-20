package org.ideamap.idea.dto;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaProjection;
import org.ideamap.idea.model.TagEntity;

import java.util.List;

public class IdeaWithTagsLinksDto {
    private String id;

    private String ideaGroupId;
    private List<TagEntity> tags;

    private List<IdeaProjection> linkedIdeas;

    private ObjectId userId;

    private String name;

    private String text;

    private String link;

    private String image;

    public IdeaWithTagsLinksDto(IdeaEntity idea, List<TagEntity> tags, List<IdeaProjection> linkedIdeas) {
        this.id = idea.getIdAsString();
        this.image = idea.getImage();
        this.link = idea.getLink();
        this.name = idea.getName();
        this.text = idea.getText();
        this.userId = idea.getUserId();
        this.tags = tags;
        this.linkedIdeas = linkedIdeas;
        this.ideaGroupId = idea.getIdeaGroupId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
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

    public List<IdeaProjection> getLinkedIdeas() {
        return linkedIdeas;
    }

    public void setLinkedIdeas(List<IdeaProjection> linkedIdeas) {
        this.linkedIdeas = linkedIdeas;
    }

    public String getIdeaGroupId() {
        return ideaGroupId;
    }

    public void setIdeaGroupId(String ideaGroupId) {
        this.ideaGroupId = ideaGroupId;
    }
}

package org.ideamap.idea.dto;

import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.TagEntity;

import java.util.List;

public class IdeaWithTagsDto {
    private String ideaId;
    private List<TagEntity> tags;

    private ObjectId userId;

    private String name;

    private String text;

    private String link;

    private String image;

    public IdeaWithTagsDto(String ideaId, List<TagEntity> tags) {
        this.ideaId = ideaId;
        this.tags = tags;
    }

    public IdeaWithTagsDto(IdeaEntity idea, List<TagEntity> tags) {
        this.ideaId = idea.getIdAsString();
        this.image = idea.getImage();
        this.link = idea.getLink();
        this.name = idea.getName();
        this.text = idea.getText();
        this.userId = idea.getUserId();
        this.tags = tags;
    }

    public String getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(String ideaId) {
        this.ideaId = ideaId;
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
}

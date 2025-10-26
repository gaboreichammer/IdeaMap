package org.ideamap.idea.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags") // Maps this class to a MongoDB collection named 'tags'
public class Tag {

    @Id // Denotes the primary key, MongoDB will generate an ObjectId if not provided
    private ObjectId id;

    // This is the "Tag name" property
    private String name;


    public Tag(String name) {
        this.name = name;
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
}

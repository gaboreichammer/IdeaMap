package org.ideamap.idea.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags") // Maps this class to a MongoDB collection named 'tags'
public class Tag {

    @Id // Denotes the primary key, MongoDB will generate an ObjectId if not provided
    private String id;

    // This is the "Tag name" property
    private String name;


    public Tag(String name) {
        this.name = name;
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
}

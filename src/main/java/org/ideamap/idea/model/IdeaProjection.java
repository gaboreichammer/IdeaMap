package org.ideamap.idea.model;

public interface IdeaProjection {
    // Note: getId() is for the ObjectId field in the entity.
    // We need a specific getter that returns the ID as a String,
    // which corresponds to the type you want to return.

    // Spring Data automatically converts the ObjectId to String if needed,
    // but using the entity field name 'id' is standard.
    String getId();

    String getName();
}


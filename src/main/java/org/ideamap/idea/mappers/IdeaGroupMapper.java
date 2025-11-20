package org.ideamap.idea.mappers;

import org.ideamap.idea.dto.IdeaGroupDto;
import org.ideamap.idea.model.IdeaGroupEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Component responsible for mapping between IdeaGroupEntity and IdeaGroupDto.
 */
@Component
public class IdeaGroupMapper {

    /**
     * Maps a single IdeaGroupEntity to an IdeaGroupDto.
     */
    public IdeaGroupDto toDto(IdeaGroupEntity entity) {
        // Essential null check
        if (entity == null) {
            return null;
        }

        return new IdeaGroupDto(
                // Use the convenience method to get the String ID from ObjectId
                entity.getIdAsString(),
                entity.getName(),
                entity.getUserId(),
                entity.getLinkedIdeaId()
        );
    }

    /**
     * Maps a list of IdeaGroupEntity to a list of IdeaGroupDto.
     */
    public List<IdeaGroupDto> toDtoList(List<IdeaGroupEntity> entities) {
        // Use a Java Stream to efficiently map each entity in the list
        return entities.stream()
                .map(this::toDto) // Calls the single-entity mapping method for each item
                .collect(Collectors.toList());
    }
}

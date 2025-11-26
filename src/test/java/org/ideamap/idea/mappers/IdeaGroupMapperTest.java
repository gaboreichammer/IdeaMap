// java
package org.ideamap.idea.mappers;

import org.bson.types.ObjectId;
import org.ideamap.idea.dto.IdeaGroupDto;
import org.ideamap.idea.model.IdeaGroupEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IdeaGroupMapperTest {

    private final IdeaGroupMapper mapper = new IdeaGroupMapper();

    @Test
    void toDto_maps_fields_correctly() {
        ObjectId userId = new ObjectId("902f1f77bcf86cd799439011");

        IdeaGroupEntity entity = new IdeaGroupEntity() {
            @Override
            public String getIdAsString() { return "507f1f77bcf86cd799439011"; } // valid 24-char hex
            @Override
            public String getName() { return "GroupName"; }
            @Override
            public ObjectId getUserId() { return userId;}
            @Override
            public String getLinkedIdeaId() { return "linkedIdea"; }
        };

        IdeaGroupDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("507f1f77bcf86cd799439011", dto.getId());
        assertEquals("GroupName", dto.getName());
        assertEquals(userId, dto.getUserId());
        assertEquals("linkedIdea", dto.getLinkedIdeaId());
    }

    @Test
    void toDto_returns_null_for_null_input() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toDtoList_maps_list() {
        ObjectId userId1 = new ObjectId("104f1f771cf56cd799439011");
        ObjectId userId2 = new ObjectId("204f1f771cf56cd799439012");

        IdeaGroupEntity e1 = new IdeaGroupEntity() {
            @Override public String getIdAsString() { return "507f191e810c19729de860ea"; }
            @Override public String getName() { return "nameX"; }
            @Override public ObjectId getUserId() { return userId1; }
            @Override public String getLinkedIdeaId() { return "linkX"; }
        };
        IdeaGroupEntity e2 = new IdeaGroupEntity() {
            @Override public String getIdAsString() { return "507f191e810c19729de860eb"; }
            @Override public String getName() { return "nameY"; }
            @Override public ObjectId getUserId() { return userId2;}
            @Override public String getLinkedIdeaId() { return "linkY"; }
        };

        List<IdeaGroupDto> dtos = mapper.toDtoList(Arrays.asList(e1, e2));

        assertEquals(2, dtos.size());
        assertEquals("507f191e810c19729de860ea", dtos.get(0).getId());
        assertEquals("507f191e810c19729de860eb", dtos.get(1).getId());
        assertEquals("nameX", dtos.get(0).getName());
        assertEquals("nameY", dtos.get(1).getName());
        assertEquals(userId1, dtos.get(0).getUserId());
        assertEquals(userId2, dtos.get(1).getUserId());
        assertEquals("linkX", dtos.get(0).getLinkedIdeaId());
        assertEquals("linkY", dtos.get(1).getLinkedIdeaId());
    }
}
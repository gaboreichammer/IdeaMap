package org.ideamap.mock;
import org.bson.types.ObjectId;
import org.ideamap.idea.IdeaGroupRepository;
import org.ideamap.idea.IdeaRepository;
import org.ideamap.idea.TagRepository;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaGroupEntity;
import org.ideamap.idea.model.TagEntity;
import org.ideamap.user.MongoUser;
import org.ideamap.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 👈 Mark this as a Spring service
public class MockDataService {

    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final PasswordEncoder passwordEncoder;

    private  final TagRepository tagRepository;

    private final IdeaGroupRepository ideaGroupRepository;

    public MockDataService(UserRepository userRepository,
                           IdeaRepository ideaRepository, PasswordEncoder passwordEncoder1, TagRepository tagRepository, IdeaGroupRepository ideaGroupRepository) {
        this.userRepository = userRepository;
        this.ideaRepository = ideaRepository;
        // ... initialize others
        this.passwordEncoder = passwordEncoder1;
        this.tagRepository = tagRepository;
        this.ideaGroupRepository = ideaGroupRepository;
    }

    /**
     * Main method to run all data creation logic.
     */
    public void createMockData() {
        if (ideaRepository.count() == 0) {
            System.out.println("\n✅ MockDataService: Starting initial data insertion...");

            // 1. Create and persist users
            MongoUser admin = createMockUsers();

            // 2. Create and persist tags
            List<String> tags = createMockTags();

            // 3. Create and link ideas
            IdeaEntity idea = createMockIdeas(admin, tags);

            // 4. Create idea group using the ideas
            createMockIdeaGroup(admin, idea);

            System.out.println("   -> Successfully completed mock data insertion.");
        } else {
            System.out.println("\n🟡 MockDataService: MongoDB 'ideas' collection is not empty. Skipping data creation.");
        }
    }

    // --- Private Helper Methods for Cleanliness ---

    private MongoUser createMockUsers() {
        String adminHashedPassword = passwordEncoder.encode("secret");
        String userHashedPassword = passwordEncoder.encode("password456");

        userRepository.save(new MongoUser("admin", adminHashedPassword));
        userRepository.save(new MongoUser("user", userHashedPassword));

        return userRepository.findByUsername("admin");
    }

    private List<String> createMockTags() {
        // 1. Create and Save Tag
        TagEntity firstTagEntity = tagRepository.save(new TagEntity("Spring"));
        TagEntity secondTagEntity = tagRepository.save(new TagEntity("Security"));

        return List.of(firstTagEntity.getIdAsString(), secondTagEntity.getIdAsString());
    }

    private IdeaEntity createMockIdeas(MongoUser user, List<String> tagIds) {
        ObjectId userId = user != null ? user.getId() : null;

        IdeaEntity secondIdeaEntity = new IdeaEntity("Configuration Annotation");
        secondIdeaEntity.setUserId(userId);
        secondIdeaEntity.setText("The @Configuration annotation, applied at the <b>class level</b>, " +
                "serves as the blueprint for " +
                "the Spring IoC (Inversion of Control) Container, telling it how to create, " +
                "configure, and assemble the beans for your application.\n");
        IdeaEntity savedSecondIdeaEntity = ideaRepository.save(secondIdeaEntity);

        IdeaEntity firstIdeaEntity = getIdeaEntity(tagIds, userId, savedSecondIdeaEntity);
        return ideaRepository.save(firstIdeaEntity);
    }

    private static IdeaEntity getIdeaEntity(List<String> tagIds, ObjectId userId, IdeaEntity savedSecondIdeaEntity) {
        IdeaEntity firstIdeaEntity = new IdeaEntity("Main Method");
        firstIdeaEntity.setText("In the Java ecosystem, every standalone executable application " +
                "<b>must have</b> a class with a public static void main(String[] args) method. " +
                "This method serves as the <b>entry point—the</b> starting line for the " +
                "Java Virtual Machine (JVM) when you execute a JAR file " +
                "(which is what Spring Boot produces).\n");
        firstIdeaEntity.setTagIds(tagIds);
        firstIdeaEntity.setUserId(userId);
        firstIdeaEntity.setLinkedIdeaIds(List.of(savedSecondIdeaEntity.getId().toString()));
        return firstIdeaEntity;
    }

    private void createMockIdeaGroup(MongoUser user, IdeaEntity idea) {
        IdeaGroupEntity firstGroup = new IdeaGroupEntity("Java", idea.getIdAsString(), user.getId());
        ideaGroupRepository.save(firstGroup);
    }
}
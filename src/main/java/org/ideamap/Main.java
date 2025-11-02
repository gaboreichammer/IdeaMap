package org.ideamap;

import org.bson.types.ObjectId;
import org.ideamap.idea.IdeaGroupRepository;
import org.ideamap.idea.IdeaRepository;
import org.ideamap.idea.TagRepository;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaGroupEntity;
import org.ideamap.idea.model.TagEntity;
import org.ideamap.user.MongoUser;
import org.ideamap.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository,
                                      IdeaGroupRepository ideaGroupRepository,
                                      IdeaRepository ideaRepository,
                                      TagRepository tagRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // --- MongoDB Logic (IdeaGroup, Idea, Tag) ---
            // Check if the MongoDB Ideas collection is empty before inserting
            if (ideaRepository.count() == 0) {
                System.out.println("\nMongoDB collections are empty. Inserting initial data...");

                // 1. Hash the plain-text passwords
               String adminHashedPassword = passwordEncoder.encode("password123");
               String userHashedPassword = passwordEncoder.encode("password456");

                userRepository.save(new MongoUser("admin", adminHashedPassword));
                userRepository.save(new MongoUser("user", userHashedPassword));
                System.out.println("Saved 2 users to the MongoDB.");

                MongoUser admin = userRepository.findByUsername("admin");

                if(admin != null) {
                    System.out.println("Found user by username 'admin': " + admin.getUsername());
                }

                ObjectId adminId = admin != null ? admin.getId() : null;

                // 1. Create and Save Tag
                TagEntity firstTagEntity = tagRepository.save(new TagEntity("First Tag"));
                TagEntity secondTagEntity = tagRepository.save(new TagEntity("Planning"));

                List<String> tagIds = Arrays.asList(firstTagEntity.getIdAsString(), secondTagEntity.getIdAsString());

                IdeaEntity secondIdeaEntity = new IdeaEntity("Second Idea");
                secondIdeaEntity.setUserId(adminId);
                secondIdeaEntity.setText("A detailed plan for implementation.");

                IdeaEntity savedSecondIdeaEntity = ideaRepository.save(secondIdeaEntity);

                // 2. Create and Save Ideas
                IdeaEntity firstIdeaEntity = new IdeaEntity("First Idea");
                firstIdeaEntity.setText("Great idea man!");
                firstIdeaEntity.setTagIds(tagIds);
                firstIdeaEntity.setUserId(adminId);
                firstIdeaEntity.setLinkedIdeaIds(List.of(savedSecondIdeaEntity.getId().toString()));

                IdeaEntity savedFirstIdeaEntity = ideaRepository.save(firstIdeaEntity);

                // 3. Create and Save IdeaGroup
                IdeaGroupEntity firstGroup = new IdeaGroupEntity("First Idea Group", savedFirstIdeaEntity.getIdAsString(), adminId);

                ideaGroupRepository.save(firstGroup);

                System.out.println("Successfully inserted 1 IdeaGroup, 2 Ideas, and 2 Tags into MongoDB.");
            } else {
                System.out.println("\nMongoDB 'ideas' collection is not empty. Skipping initial data creation.");
            }
        };
    }
}
package org.ideamap;

import org.ideamap.idea.IdeaGroupRepository;
import org.ideamap.idea.IdeaRepository;
import org.ideamap.idea.TagRepository;
import org.ideamap.idea.model.Idea;
import org.ideamap.idea.model.IdeaGroup;
import org.ideamap.idea.model.Tag;
import org.ideamap.user.User;
import org.ideamap.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
                                      TagRepository tagRepository) {
        return args -> {

            // --- H2 Database (Users) Logic ---
            // Assuming UserRepository uses a relational database like H2 (since you mentioned "H2 database")
            if (userRepository.count() == 0) {
                userRepository.save(new User("admin", "password123"));
                userRepository.save(new User("user", "password456"));
                System.out.println("Saved 2 users to the H2 database.");
            } else {
                System.out.println("H2 Users already exist. Skipping initial user creation.");
            }
            userRepository.findByUsername("admin").ifPresent(user -> {
                System.out.println("Found user by username 'admin': " + user.getUsername());
            });

            // -------------------------------------

            // --- MongoDB Logic (IdeaGroup, Idea, Tag) ---
            // Check if the MongoDB Ideas collection is empty before inserting
            if (ideaRepository.count() == 0) {
                System.out.println("\nMongoDB collections are empty. Inserting initial data...");

                // 1. Create and Save Tag
                Tag firstTag = tagRepository.save(new Tag("First Tag"));
                Tag secondTag = tagRepository.save(new Tag("Planning"));

                List<String> tagIds = Arrays.asList(firstTag.getId(), secondTag.getId());

                // 2. Create and Save Ideas
                Idea firstIdea = new Idea("First Idea");
                firstIdea.setText("Great idea man!");
                firstIdea.setTagIds(tagIds); // Link the tags by ID

                Idea savedFirstIdea = ideaRepository.save(firstIdea);

                Idea secondIdea = new Idea("Second Idea");
                secondIdea.setText("A detailed plan for implementation.");
                // Optionally link tags or other ideas here

                Idea savedSecondIdea = ideaRepository.save(secondIdea);

                // 3. Create and Save IdeaGroup
                IdeaGroup firstGroup = new IdeaGroup("First Idea Group");

                // Link the ideas to the group using their generated IDs
                firstGroup.setLinkedIdeaId(savedFirstIdea.getId());

                ideaGroupRepository.save(firstGroup);

                System.out.println("Successfully inserted 1 IdeaGroup, 2 Ideas, and 2 Tags into MongoDB.");
            } else {
                System.out.println("\nMongoDB 'ideas' collection is not empty. Skipping initial data creation.");
            }
        };
    }
}
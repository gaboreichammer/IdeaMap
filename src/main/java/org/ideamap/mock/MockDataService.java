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

import java.util.ArrayList;
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
            List<String> javaTags = createMockJavaTags();
            List<String> dotNetTags = createMockDotNetTags(); // New .NET tags

            // 3. Create and link ideas (Java and .NET)
            IdeaEntity mainJavaIdea = createMockJavaIdeas(admin, javaTags);
            IdeaEntity mainDotNetIdea = createMockDotNetIdeas(admin, dotNetTags); // New .NET ideas

            // 4. Create idea groups using the ideas
            createMockIdeaGroup("Java", admin, mainJavaIdea);
            createMockIdeaGroup(".NET", admin, mainDotNetIdea); // New .NET group

            System.out.println("   -> Successfully completed mock data insertion.");
        } else {
            System.out.println("\n🟡 MockDataService: MongoDB 'ideas' collection is not empty. Skipping data creation.");
        }
    }

    // --- Private Helper Methods for Cleanliness ---

    private MongoUser createMockUsers() {
        String adminHashedPassword = passwordEncoder.encode("szuperkod");
        String userHashedPassword = passwordEncoder.encode("password456");

        userRepository.save(new MongoUser("admin", adminHashedPassword));
        userRepository.save(new MongoUser("user", userHashedPassword));

        return userRepository.findByUsername("admin");
    }

    private List<String> createMockJavaTags() {
        // Create and Save Java Tags
        TagEntity springTag = tagRepository.save(new TagEntity("Spring"));
        TagEntity securityTag = tagRepository.save(new TagEntity("Security"));

        return List.of(springTag.getIdAsString(), securityTag.getIdAsString());
    }

    // New method for .NET tags
    private List<String> createMockDotNetTags() {
        // Create and Save .NET Tags
        TagEntity dotNetTag = tagRepository.save(new TagEntity(".NET"));
        TagEntity cSharpTag = tagRepository.save(new TagEntity("CSharp"));

        return List.of(dotNetTag.getIdAsString(), cSharpTag.getIdAsString());
    }

    // --- JAVA IDEAS ---

    private IdeaEntity createMockJavaIdeas(MongoUser user, List<String> tagIds) {
        ObjectId userId = user != null ? user.getId() : null;
        List<String> linkedIds = new ArrayList<>();

        // 1. Linked Idea: Configuration Annotation (Existing)
        IdeaEntity configAnnotation = new IdeaEntity("Configuration Annotation");
        configAnnotation.setUserId(userId);
        configAnnotation.setText("The @Configuration annotation, applied at the <b>class level</b>, " +
                "serves as the blueprint for " +
                "the Spring IoC (Inversion of Control) Container, telling it how to create, " +
                "configure, and assemble the beans for your application.\n");
        IdeaEntity savedConfigAnnotation = ideaRepository.save(configAnnotation);
        linkedIds.add(savedConfigAnnotation.getIdAsString());

        // 2. Linked Idea: Bean Annotation (New)
        IdeaEntity beanAnnotation = new IdeaEntity("Bean Annotation");
        beanAnnotation.setUserId(userId);
        beanAnnotation.setText("The @Bean annotation is used on methods in a @Configuration class. " +
                "It tells Spring to execute this method and register the returned object as a bean in the application context.");
        IdeaEntity savedBeanAnnotation = ideaRepository.save(beanAnnotation);
        linkedIds.add(savedBeanAnnotation.getIdAsString());

        // 3. Linked Idea: Autowired Annotation (New)
        IdeaEntity autowiredAnnotation = new IdeaEntity("Autowired Annotation");
        autowiredAnnotation.setUserId(userId);
        autowiredAnnotation.setText("The @Autowired annotation is used for automatic dependency injection. " +
                "It can be used on fields, constructors, or setter methods.");
        IdeaEntity savedAutowiredAnnotation = ideaRepository.save(autowiredAnnotation);
        linkedIds.add(savedAutowiredAnnotation.getIdAsString());

        // 4. Main Idea: Main Method (Existing logic but with more links)
        IdeaEntity mainMethod = getIdeaEntity(tagIds, userId, linkedIds);
        return ideaRepository.save(mainMethod);
    }

    private IdeaEntity getIdeaEntity(List<String> tagIds, ObjectId userId, List<String> linkedIdeaIds) {
        IdeaEntity firstIdeaEntity = new IdeaEntity("Main Method");
        firstIdeaEntity.setText("In the Java ecosystem, every standalone executable application " +
                "<b>must have</b> a class with a public static void main(String[] args) method. " +
                "This method serves as the <b>entry point—the</b> starting line for the " +
                "Java Virtual Machine (JVM) when you execute a JAR file " +
                "(which is what Spring Boot produces).\n");
        firstIdeaEntity.setTagIds(tagIds);
        firstIdeaEntity.setUserId(userId);
        firstIdeaEntity.setLinkedIdeaIds(linkedIdeaIds); // Use the list of all linked IDs
        return firstIdeaEntity;
    }

    // --- .NET IDEAS (NEW SECTION) ---

    private IdeaEntity createMockDotNetIdeas(MongoUser user, List<String> tagIds) {
        ObjectId userId = user != null ? user.getId() : null;
        List<String> linkedIds = new ArrayList<>();

        // 1. Linked Idea: Core CLI (New .NET linked idea)
        IdeaEntity cliIdea = new IdeaEntity(".NET Core CLI");
        cliIdea.setUserId(userId);
        cliIdea.setText("The .NET Core Command-Line Interface (CLI) is a cross-platform tool " +
                "for developing, building, running, and managing .NET projects. " +
                "Commands include <b>'dotnet new'</b> and <b>'dotnet run'</b>.");
        IdeaEntity savedCliIdea = ideaRepository.save(cliIdea);
        linkedIds.add(savedCliIdea.getIdAsString());

        // 2. Linked Idea: ASP.NET Core MVC (New .NET linked idea)
        IdeaEntity mvcIdea = new IdeaEntity("ASP.NET Core MVC");
        mvcIdea.setUserId(userId);
        mvcIdea.setText("ASP.NET Core MVC is a framework for building web apps and APIs using the Model-View-Controller pattern. " +
                "It separates the application into three main components.");
        IdeaEntity savedMvcIdea = ideaRepository.save(mvcIdea);
        linkedIds.add(savedMvcIdea.getIdAsString());

        // 3. Linked Idea: Entity Framework (New .NET linked idea)
        IdeaEntity efIdea = new IdeaEntity("Entity Framework Core");
        efIdea.setUserId(userId);
        efIdea.setText("EF Core is an object-relational mapper (ORM) for .NET. " +
                "It enables developers to work with a database using .NET objects, eliminating the need for most data-access code.");
        IdeaEntity savedEfIdea = ideaRepository.save(efIdea);
        linkedIds.add(savedEfIdea.getIdAsString());

        // 4. Main .NET Idea
        IdeaEntity mainDotNetIdea = new IdeaEntity("Program.cs Entry Point");
        mainDotNetIdea.setText("In modern .NET applications, the <b>Program.cs</b> file contains the entry point for the application. " +
                "It typically uses a top-level statement structure to configure the host, services, and middleware.");
        mainDotNetIdea.setTagIds(tagIds);
        mainDotNetIdea.setUserId(userId);
        mainDotNetIdea.setLinkedIdeaIds(linkedIds);
        return ideaRepository.save(mainDotNetIdea);
    }


    // --- IDEA GROUP CREATION ---

    private void createMockIdeaGroup(String groupName, MongoUser user, IdeaEntity idea) {
        IdeaGroupEntity newGroup = new IdeaGroupEntity(groupName, idea.getIdAsString(), user.getId());
        ideaGroupRepository.save(newGroup);
    }
}
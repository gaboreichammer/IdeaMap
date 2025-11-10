package org.ideamap.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.types.ObjectId;
import org.ideamap.idea.model.IdeaEntity;
import org.ideamap.idea.model.IdeaGroupEntity;
import org.ideamap.idea.model.TagEntity;
import org.ideamap.idea.repository.IdeaGroupRepository;
import org.ideamap.idea.repository.IdeaRepository; // Import your repository interfaces
import org.ideamap.idea.repository.TagRepository;
import org.ideamap.user.MongoUser;
import org.ideamap.user.repository.UserRepository;   // Assuming your user repository is here
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;

@Configuration
// Note: @EnableMongoRepositories is NOT used here, as we manually define the factories below.
public class MongoConfig {

    // ----------------------------------------------------------------------------------
    // I. app-user-db Configuration (Secondary)
    // ----------------------------------------------------------------------------------

    // 1. User Properties
    @Bean("userMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.user")
    public MongoProperties userMongoProperties() {
        return new MongoProperties();
    }

    // 2. User Database Factory
    @Bean("userMongoFactory")
    public MongoDatabaseFactory userMongoFactory(
            @Qualifier("userMongoProperties") MongoProperties mongoProperties) {
        MongoClient mongoClient = MongoClients.create(mongoProperties.getUri());
        return new SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.getDatabase());
    }

    // 3. User MongoTemplate
    @Bean("userMongoTemplate") // Used by the repository factory
    public MongoTemplate userMongoTemplate(
            @Qualifier("userMongoFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

    @Bean
    public MongoRepositoryFactoryBean<UserRepository, MongoUser, Long> userRepositoryFactoryBean(
            @Qualifier("userMongoTemplate") MongoTemplate userMongoTemplate) {

        // 1. Pass the repository interface class to the constructor.
        MongoRepositoryFactoryBean<UserRepository, MongoUser, Long> factory = new MongoRepositoryFactoryBean<>(UserRepository.class);

        // 2. ONLY set the MongoTemplate (the MongoOperations dependency).
        factory.setMongoOperations(userMongoTemplate);

        // REMOVED: factory.setRepositoryBaseClass(UserRepository.class);

        return factory;
    }

    // ----------------------------------------------------------------------------------
    // II. app-idea-db Configuration (Primary)
    // ----------------------------------------------------------------------------------

    // 1. Idea Properties
    @Primary
    @Bean("ideaMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.idea")
    public MongoProperties ideaMongoProperties() {
        return new MongoProperties();
    }

    // 2. Idea Database Factory
    @Primary
    @Bean("ideaMongoFactory")
    public MongoDatabaseFactory ideaMongoFactory(
            @Qualifier("ideaMongoProperties") MongoProperties mongoProperties) {
        MongoClient mongoClient = MongoClients.create(mongoProperties.getUri());
        return new SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.getDatabase());
    }

    // 3. Idea MongoTemplate
    @Primary
    @Bean("ideaMongoTemplate") // Used by the repository factory
    public MongoTemplate ideaMongoTemplate(
            @Qualifier("ideaMongoFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

    @Bean
    public MongoRepositoryFactoryBean<IdeaRepository, IdeaEntity, ObjectId> ideaRepositoryFactoryBean(
            @Qualifier("ideaMongoTemplate") MongoTemplate ideaMongoTemplate) {

        MongoRepositoryFactoryBean<IdeaRepository, IdeaEntity, ObjectId> factory = new MongoRepositoryFactoryBean<>(IdeaRepository.class);

        factory.setMongoOperations(ideaMongoTemplate);
        return factory;
    }

    @Bean
    public MongoRepositoryFactoryBean<TagRepository, TagEntity, ObjectId> tagRepositoryFactoryBean(
            @Qualifier("ideaMongoTemplate") MongoTemplate ideaMongoTemplate) {

        MongoRepositoryFactoryBean<TagRepository, TagEntity, ObjectId> factory = new MongoRepositoryFactoryBean<>(TagRepository.class);

        factory.setMongoOperations(ideaMongoTemplate);
        return factory;
    }

    @Bean
    public MongoRepositoryFactoryBean<IdeaGroupRepository, IdeaGroupEntity, String> ideaGroupRepositoryFactoryBean(
            @Qualifier("ideaMongoTemplate") MongoTemplate ideaMongoTemplate) {

        MongoRepositoryFactoryBean<IdeaGroupRepository, IdeaGroupEntity, String> factory = new MongoRepositoryFactoryBean<>(IdeaGroupRepository.class);

        factory.setMongoOperations(ideaMongoTemplate);
        return factory;
    }
}
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
}
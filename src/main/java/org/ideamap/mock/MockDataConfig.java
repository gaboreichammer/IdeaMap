package org.ideamap.mock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mock-data")
public class MockDataConfig {

    private final MockDataService mockDataService;

    public MockDataConfig(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            mockDataService.createMockData();
        };
    }
}
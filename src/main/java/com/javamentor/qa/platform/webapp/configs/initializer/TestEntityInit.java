package com.javamentor.qa.platform.webapp.configs.initializer;

import com.javamentor.qa.platform.service.impl.TestDataInitService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingClass({"org.springframework.boot.test.context.SpringBootTest"})
public class TestEntityInit implements CommandLineRunner {

    private final TestDataInitService testDataInitService;

    @Autowired
    public TestEntityInit(TestDataInitService testDataInitService) {
        this.testDataInitService = testDataInitService;
    }

//    @Bean
////    @Before
//    public FlywayMigrationStrategy clean() {
//        System.out.println("TestEntityInit - FlywayMigrationStrategy");
//        return flyway -> {
//            flyway.clean();
//            flyway.migrate();
//            flyway.clean();
//        };
//    }

    @Override
    public void run(String... args) {

    }
}

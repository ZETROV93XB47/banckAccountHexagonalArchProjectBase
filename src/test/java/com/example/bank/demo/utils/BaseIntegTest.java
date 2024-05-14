package com.example.bank.demo.utils;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(initializers = BaseIntegTest.DataSourceInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseIntegTest {

    private final String cleanUpRequest = """
                    SET FOREIGN_KEY_CHECKS = 0;
                    DELETE FROM operation;
                    DELETE FROM bank_account;
                    DELETE FROM saving_account;
                    DELETE FROM bank;
                    SET FOREIGN_KEY_CHECKS = 1;
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute(" SET FOREIGN_KEY_CHECKS = 0;");
        jdbcTemplate.execute(" DELETE FROM operation;");
        jdbcTemplate.execute(" DELETE FROM bank_account;");
        jdbcTemplate.execute(" DELETE FROM saving_account;");
        jdbcTemplate.execute(" DELETE FROM bank;");
        jdbcTemplate.execute(" SET FOREIGN_KEY_CHECKS = 1;");
    }

    @Container
    private static final MySQLContainer<?> database = new MySQLContainer<>("mysql:8.1.0");

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.test.database.replace=none",
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword()
            );
        }
    }
}
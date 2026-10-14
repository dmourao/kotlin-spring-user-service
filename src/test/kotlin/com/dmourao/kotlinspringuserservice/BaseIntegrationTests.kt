package com.dmourao.kotlinspringuserservice

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer


@SpringBootTest(
    classes = [KotlinSpringUserServiceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureRestTestClient
abstract class BaseIntegrationTests {
    companion object {
        // shared across all subclasses so the container starts once for the whole suite
        protected val postgresContainer =
                PostgreSQLContainer("postgres:17-alpine3.20")
                    .withDatabaseName("userservice")
                    .withUsername("userservice")
                    .withPassword("password")
                    .withExposedPorts(5432);

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            postgresContainer.start();
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgresContainer.stop();
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
        }
    }
}
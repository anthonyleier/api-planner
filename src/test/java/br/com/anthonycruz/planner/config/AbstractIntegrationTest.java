package br.com.anthonycruz.planner.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration()
public class AbstractIntegrationTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine");

        private static void startContainers() {
            Startables.deepStart(Stream.of(container)).join();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", container.getJdbcUrl(),
                    "spring.datasource.username", container.getUsername(),
                    "spring.datasource.password", container.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource("testcontainers", createConnectionConfiguration());
            environment.getPropertySources().addFirst(testContainers);
        }
    }
}

package ee.danych.nutrimatch.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "ee.danych.nutrimatch")
@EnableJpaRepositories(basePackages = "ee.danych.nutrimatch.repository")
public class ApplicationConfig {
}

package ee.danych.nutrimatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${nutrimatch.frontend.url}")
    private String FRONTEND_URL;

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(FRONTEND_URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("X-Available-Pages", "*")
                .allowCredentials(true)
                .exposedHeaders("X-Available-Pages");
    }
}

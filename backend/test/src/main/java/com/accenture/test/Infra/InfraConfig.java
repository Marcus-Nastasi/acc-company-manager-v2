package com.accenture.test.Infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

@Configuration
public class InfraConfig extends DelegatingWebMvcConfiguration {

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedHeaders("Content-Type", "Authorization")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedOrigins("http://localhost:3000");
    }
}

package com.accenture.test.infrastructure.configuration.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Company Manager",
        description = "An application to relate companies and suppliers",
        version = "1.0.0"
    )
)
public class SwaggerConfig {}

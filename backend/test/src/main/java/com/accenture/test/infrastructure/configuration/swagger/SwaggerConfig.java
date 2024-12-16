package com.accenture.test.infrastructure.configuration.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan
@RestController
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Company Manager",
        description = "Uma aplicação para relacionar empresas e fornecedores",
        version = "1.0.0"
    )
)
public class SwaggerConfig {}

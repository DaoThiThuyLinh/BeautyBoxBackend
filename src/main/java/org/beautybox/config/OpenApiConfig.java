package org.beautybox.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("bearerAuth");
        return new OpenAPI()
                .info(new Info()
                        .title("Beauty Box")
                        .version("1.0")
                        .description("API")
                )
                .schemaRequirement("bearerAuth", securityScheme);
    }
}

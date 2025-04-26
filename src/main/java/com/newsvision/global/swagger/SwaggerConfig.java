package com.newsvision.global.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

// http://localhost:8080/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NEWSION")
                        .description("NEWSION 입니당.")
                        .version("v1.0.0"));
    }
}

package com.example.is_coursework.configurations;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.OAuthFlow;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Value("${spring.security.oauth2.openIdClientUrl}")
    private String openIdClientUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "openIdConnect";
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OPENIDCONNECT)
                                                .openIdConnectUrl(openIdClientUrl)
                                )
                )
                .servers(List.of(new Server().url("http://localhost:8080")))
                .security(List.of(new SecurityRequirement().addList(securitySchemeName)))
                .info(new Info().title("Bunker game").version("1.0"));
    }
}

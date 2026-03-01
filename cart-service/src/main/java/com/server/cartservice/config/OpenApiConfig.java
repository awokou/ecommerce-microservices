package com.server.cartservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Cart Service",
                description = "OpenAPI documentation for the Cart Service."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8082",
                        description = "Local DEV"
                )
        }
)
public class OpenApiConfig {
}

package com.server.productservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Product Service",
                description = "OpenAPI documentation for the Product Service."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8081",
                        description = "Local DEV"
                )
        }
)
public class OpenApiConfig {
}

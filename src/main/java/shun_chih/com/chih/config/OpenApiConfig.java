package shun_chih.com.chih.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "OpenApi 3 & Spring Boot",
                description = "springboot-openapi"
        ),
        servers = {
                @Server(
                        url = "{schema}://localhost:8080",
                        variables = @ServerVariable(
                                name = "schema",
                                allowableValues = {"http"},
                                defaultValue = "http"
                        )
                )
        }
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "Authentication",
        description = "JWT",
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}

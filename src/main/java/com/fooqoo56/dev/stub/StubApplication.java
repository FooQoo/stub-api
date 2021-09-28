package com.fooqoo56.dev.stub;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@OpenAPIDefinition(info = @Info(
        title = "Stub REST API",
        description = "テスト用のスタブAPI",
        version = "v1"),
        servers = {
                @Server(url = "https://stub-api.com/", description = "demo"),
        }
)
public class StubApplication {

    public static void main(final String[] args) {
        SpringApplication.run(StubApplication.class, args);
    }

}

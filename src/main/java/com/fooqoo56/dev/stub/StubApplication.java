package com.fooqoo56.dev.stub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class StubApplication {

    public static void main(final String[] args) {
        SpringApplication.run(StubApplication.class, args);
    }

}

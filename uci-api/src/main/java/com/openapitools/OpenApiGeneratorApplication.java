package com.openapitools;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import com.fasterxml.jackson.databind.Module;

@SpringBootApplication(
    nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)

@ComponentScan(
    basePackages = {"com.openapitools", "com.uci.chess.handler" , "com.uci.chess.handler.impl" , "com.openapitools.configuration"},
    nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)

public class OpenApiGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiGeneratorApplication.class, args);
    }

    @Bean(name = "org.openapitools.OpenApiGeneratorApplication.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}
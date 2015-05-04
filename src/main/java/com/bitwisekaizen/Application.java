package com.bitwisekaizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }
}

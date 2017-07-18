package com.epam.testsystem.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * github_test
 * Created on 05.07.17.
 */

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.epam.testsystem.*")
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(new Class<?>[]{
                Application.class
        }, args);
    }
}

package com.epam.testsystem;

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
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.epam.testsystem.*")
public class TestApplication {
    public static void main(final String[] args) {
        SpringApplication.run(new Class<?>[]{
                TestApplication.class
        }, args);
    }
}

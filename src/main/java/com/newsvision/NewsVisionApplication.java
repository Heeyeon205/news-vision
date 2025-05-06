package com.newsvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NewsVisionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsVisionApplication.class, args);
    }

}

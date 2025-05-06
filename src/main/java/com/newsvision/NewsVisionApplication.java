package com.newsvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableElasticsearchRepositories(basePackages = "com.newsvision.elasticsearch.repository")
public class NewsVisionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsVisionApplication.class, args);
    }

}

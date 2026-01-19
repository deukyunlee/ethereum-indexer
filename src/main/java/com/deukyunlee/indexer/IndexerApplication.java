package com.deukyunlee.indexer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
public class IndexerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(IndexerApplication.class);
        app.addListeners(new ApplicationPidFileWriter("./bin/indexer.pid"));
        app.run(args);
    }
}

package com.epam.gym_app_main_mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@Slf4j
@SpringBootApplication
@EnableJms
public class GymAppMainMqApplication {
    public static void main(String[] args) {

        SpringApplication.run(GymAppMainMqApplication.class, args);

        log.info("\n\n swagger at: 'http://localhost:8080/swagger-ui/index.html'\n\n");
        log.info("\n\n api-docs at: 'http://localhost:8080/v3/api-docs'");

        log.info("\n\n------------- gym-app-main-mq started -------------\n\n");
    }
}

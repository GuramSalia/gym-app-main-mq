package com.epam.gym_app_main_mq;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = GymAppMainMqApplication.class)
public class CucumberSpringConfiguration {
}

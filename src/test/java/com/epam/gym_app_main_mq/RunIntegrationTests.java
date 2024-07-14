package com.epam.gym_app_main_mq;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@SpringBootTest
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com/epam/gym_app_main_mq",
        plugin = {"pretty", "html:target/cucumber-reports"},
        monochrome = true)
public class RunIntegrationTests {
}

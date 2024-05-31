package com.epam.gym_app_main_mq;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com/epam/gym_app_main_mq")
public class RunComponentTests {
}

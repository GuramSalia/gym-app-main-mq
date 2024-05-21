package com.epam.gym_app_main_mq.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JsonDataContainer {
    @JsonProperty("Trainer")
    public List<Trainer> trainer;
    @JsonProperty("Trainee")
    public List<Trainee> trainee;
    @JsonProperty("Training")
    public List<Training> training;
}

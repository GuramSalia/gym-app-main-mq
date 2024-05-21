package com.epam.gym_app_main_mq.api;

import com.epam.gym_app_main_mq.model.Training;
import com.epam.gym_app_main_mq.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TrainingDTO {
    @JsonProperty(value = "training_name")
    private String trainingName;
    @JsonProperty(value = "training_date")
    private Date trainingDate;
    @JsonProperty(value = "training_type")
    private TrainingType trainingType;
    @JsonProperty(value = "training_duration_in_minutes")
    private int trainingDurationInMinutes;
    @JsonProperty(value = "trainer_username")
    private String trainerName;

    public TrainingDTO(Training training) {
        this.trainingName = training.getTrainingName();
        this.trainingDate = training.getTrainingDate();
        this.trainingType = training.getTrainingType();
        this.trainingDurationInMinutes = training.getTrainingDurationInMinutes();
        this.trainerName = training.getTrainer().getUsername();
    }
}

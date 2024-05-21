package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Getter
@Slf4j
public class TrainingRegistrationRequest extends UsernamePassword {

    @NotBlank(message = "trainee username must not be empty")
    String traineeUsername;
    @NotBlank(message = "trainer username must not be empty")
    String trainerUsername;
    @NotBlank(message = "training name must not be empty")
    String trainingName;
    @NotNull(message = "training date must not be empty")
    Date trainingDate;
    @PositiveOrZero(message = "training duration must not be empty or negative")
    int trainingDuration;

    public TrainingRegistrationRequest(
            String username,
            String password,
            String traineeUsername,
            String trainerUsername,
            String trainingName,
            Date trainingDate,
            int trainingDuration
    ) {
        super(username, password);
        this.traineeUsername = traineeUsername;
        this.trainerUsername = trainerUsername;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    @Override
    public String toString() {
        return "TrainingRegistrationRequest{" +
                "username='" + username + '\'' +
                ", password='*****', trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}

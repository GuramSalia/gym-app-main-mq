package com.epam.gym_app_main_mq.api;

import com.epam.gym_app_main_mq.model.TrainingType;
import lombok.Getter;

import java.sql.Date;

@Getter
public class TrainingsByTraineeRequest extends UsernamePassword {

    Date periodFrom;
    Date periodTo;
    String trainerUsername;
    TrainingType trainingType;

    public TrainingsByTraineeRequest(
            String username,
            String password,
            Date periodFrom,
            Date periodTo,
            String trainerUsername,
            TrainingType trainingType
    ) {
        super(username, password);
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.trainerUsername = trainerUsername;
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "TrainingsByTrainee{" +
                "periodFrom=" + periodFrom +
                ", periodTo=" + periodTo +
                ", trainerUsername='" + trainerUsername + '\'' +
                ", trainingType=" + trainingType +
                ", username='" + username + '\'' +
                '}';
    }
}

package com.epam.gym_app_main_mq.api;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class TrainingsByTrainerRequest extends UsernamePassword {

    Date periodFrom;
    Date periodTo;
    String traineeUsername;

    public TrainingsByTrainerRequest(
            String username,
            String password,
            Date periodFrom,
            Date periodTo,
            String traineeUsername
    ) {
        super(username, password);
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.traineeUsername = traineeUsername;
    }

    @Override
    public String toString() {
        return "TrainingsByTrainerRequest{" +
                "periodFrom=" + periodFrom +
                ", periodTo=" + periodTo +
                ", traineeUsername='" + traineeUsername + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

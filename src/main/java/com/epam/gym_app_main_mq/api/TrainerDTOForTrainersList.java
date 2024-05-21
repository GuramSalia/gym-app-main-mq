package com.epam.gym_app_main_mq.api;

import com.epam.gym_app_main_mq.model.TrainingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerDTOForTrainersList {

    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;

    public TrainerDTOForTrainersList(String username, String firstName, String lastName, TrainingType specialization) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
    }

    public TrainerDTOForTrainersList() {
    }

    @Override
    public String toString() {
        return "TrainerDTOForTrainersList{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialization=" + specialization +
                '}';
    }
}

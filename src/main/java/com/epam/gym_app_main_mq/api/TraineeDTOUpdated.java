package com.epam.gym_app_main_mq.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class TraineeDTOUpdated {

    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private Boolean isActive;
    private List<TrainerDTOForTrainersList> trainers;

    public TraineeDTOUpdated(
            String username,
            String firstName,
            String lastName,
            Date dateOfBirth,
            String address,
            boolean isActive,
            List<TrainerDTOForTrainersList> trainers) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
        this.trainers = trainers;
    }

    public TraineeDTOUpdated() {
    }
}

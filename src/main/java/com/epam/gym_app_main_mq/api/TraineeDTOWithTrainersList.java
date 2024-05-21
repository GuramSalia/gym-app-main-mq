package com.epam.gym_app_main_mq.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TraineeDTOWithTrainersList {

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDTOForTrainersList> trainers;

    public TraineeDTOWithTrainersList(
            String firstName,
            String lastName,
            Date dateOfBirth,
            String address,
            boolean isActive,
            List<TrainerDTOForTrainersList> trainers
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
        this.trainers = trainers;
    }

    public TraineeDTOWithTrainersList() {
    }

    @Override
    public String toString() {
        return "TraineeDTOWithTrainersList{" +
                "trainers=" + trainers +
                "} " + super.toString();
    }
}

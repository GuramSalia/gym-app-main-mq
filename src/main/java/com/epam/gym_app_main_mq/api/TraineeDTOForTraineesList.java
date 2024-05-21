package com.epam.gym_app_main_mq.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraineeDTOForTraineesList {
    private String username;
    private String firstName;
    private String lastName;

    public TraineeDTOForTraineesList(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public TraineeDTOForTraineesList() {
    }
}

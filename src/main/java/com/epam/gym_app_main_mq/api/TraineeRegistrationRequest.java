package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TraineeRegistrationRequest {

    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    Date dateOfBirth;
    String address;

    public TraineeRegistrationRequest(String firstName, String lastName, Date dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}

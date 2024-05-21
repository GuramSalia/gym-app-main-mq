package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerRegistrationRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private String specialization;

    public TrainerRegistrationRequest(
            String firstName,
            String lastName,
            String specialization
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
    }
}

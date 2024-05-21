package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TraineeUpdateRequest extends UsernamePassword {

    @NotBlank(message = "first name must not be blank")
    private String firstName;
    @NotBlank(message = "last name must not be blank")
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private Boolean isActive;

    public TraineeUpdateRequest(
            String username,
            String password,
            String firstName,
            String lastName,
            Date dateOfBirth,
            String address,
            boolean isActive
    ) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
    }
}

package com.epam.gym_app_main_mq.api;

import com.epam.gym_app_main_mq.model.TrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerUpdateRequest extends UsernamePassword {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private TrainingType specialization;
    @NotNull
    private Boolean isActive;

    public TrainerUpdateRequest(
            String username,
            String password,
            String firstName,
            String lastName,
            TrainingType specialization,
            Boolean isActive
    ) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.isActive = isActive;
    }
}

package com.epam.gym_app_main_mq.api;

import com.epam.gym_app_main_mq.model.TrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TrainerDTOUpdated {

    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private TrainingType specialization;
    @NotNull
    private Boolean isActive;
    Set<TraineeDTOForTraineesList> trainees;

    public TrainerDTOUpdated(
            String username,
            String firstName,
            String lastName,
            TrainingType specialization,
            Boolean isActive,
            Set<TraineeDTOForTraineesList> trainees
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.isActive = isActive;
        this.trainees = trainees;
    }

    public TrainerDTOUpdated() {
    }
}

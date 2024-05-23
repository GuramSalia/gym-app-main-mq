package com.epam.gym_app_main_mq.api.stat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteTrainingRequest {
    @NotNull
    Integer trainingId;
}

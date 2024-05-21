package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FullStatRequest {
    @NotNull
    private Integer trainerId;
    @NotNull
    private String token;
}

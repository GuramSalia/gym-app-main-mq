package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TraineeUpdateTrainersListRequest extends UsernamePassword {
    @NotNull
    private List<String> trainerUsernames;

    public TraineeUpdateTrainersListRequest(String username, String password, List<String> trainerUsernames) {

        super(username, password);
        this.trainerUsernames = trainerUsernames;
    }
}

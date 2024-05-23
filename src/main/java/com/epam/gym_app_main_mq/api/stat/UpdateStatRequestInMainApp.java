package com.epam.gym_app_main_mq.api.stat;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class UpdateStatRequestInMainApp implements Serializable {
    @NotNull
    private Integer trainerId;
    @NotNull
    private Integer year;
    @NotNull
    private Integer month;
    @NotNull
    private Integer duration;
    @NotNull
    private ActionType actionType;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("trainerId", trainerId.toString());
        map.put("year", year.toString());
        map.put("month", month.toString());
        map.put("duration", duration.toString());
        map.put("actionType", actionType.toString());
        return map;
    }
}

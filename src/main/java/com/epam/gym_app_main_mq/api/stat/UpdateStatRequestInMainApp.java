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
    @NotNull
    private String userName;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Boolean status;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("trainerId", trainerId.toString());
        map.put("year", year.toString());
        map.put("month", month.toString());
        map.put("duration", duration.toString());
        map.put("actionType", actionType.toString());
        map.put("userName", userName);
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("status", status.toString());
        return map;
    }
}

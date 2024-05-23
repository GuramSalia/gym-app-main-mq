package com.epam.gym_app_main_mq.api.stat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class MonthlyStatRequestInMainApp implements Serializable {
    @NotNull
    private Integer trainerId;
    @NotNull
    private Integer year;
    @NotNull
    private Integer month;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("trainerId", trainerId.toString());
        map.put("year", year.toString());
        map.put("month", month.toString());
        return map;
    }
}


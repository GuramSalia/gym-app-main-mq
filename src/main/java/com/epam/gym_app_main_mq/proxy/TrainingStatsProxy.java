package com.epam.gym_app_main_mq.proxy;

import com.epam.gym_app_main_mq.api.stat.FullStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.MonthlyStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.UpdateStatRequestInMainApp;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface TrainingStatsProxy {

    @GetMapping("/stats-api/v1/trainer-full-stats")
//    @CircuitBreaker(name = "trainingStatsService", fallbackMethod = "fallbackFullStats")
    public ResponseEntity<Map<Integer, List<Map<String, Integer>>>> getTrainerFullStats(
            @RequestBody FullStatRequestInMainApp fullStatRequestInMainApp,
            @RequestHeader(name = "gym_app_correlation_id", required = false, defaultValue = "no-correlation-id") String correlationId
    );

    @GetMapping("/stats-api/v1/trainer-monthly-stats")
//    @CircuitBreaker(name = "trainingStatsService", fallbackMethod = "fallbackMonthlyStats")
    public ResponseEntity<Map<String, Integer>> getTrainerMonthlyStats(
            @RequestBody MonthlyStatRequestInMainApp monthlyStatRequestInMainApp,
            @RequestHeader(name = "gym_app_correlation_id", required = false, defaultValue = "no-correlation-id") String correlationId
    );

    @PostMapping("/stats-api/v1/trainer-stats-update")
//    @CircuitBreaker(name = "trainingStatsService", fallbackMethod = "fallbackUpdateTrainerStats")
    public ResponseEntity<Map<String, Integer>> updateTrainerStats(
            @RequestBody UpdateStatRequestInMainApp updateStatRequestInMainApp,
            @RequestHeader(name = "gym_app_correlation_id", required = false, defaultValue = "no-correlation-id") String correlationId
    );

    private ResponseEntity<Map<Integer, List<Map<String, Integer>>>> fallbackFullStats(
            FullStatRequestInMainApp fullStatRequestInMainApp,
            String correlationId
    ) {
        Map<Integer, List<Map<String, Integer>>> emptyData = Collections.emptyMap();
        return ResponseEntity.ok(emptyData);
    }

    private ResponseEntity<Map<String, Integer>> fallbackMonthlyStats(
            MonthlyStatRequestInMainApp monthlyStatRequestInMainApp,
            String correlationId
    ) {
        Map<String, Integer> emptyData = Collections.emptyMap();
        return ResponseEntity.ok(emptyData);
    }

    private ResponseEntity<Map<String, Integer>> fallbackUpdateTrainerStats(
            UpdateStatRequestInMainApp updateStatRequestInMainApp,
            String correlationId
    ) {
        Map<String, Integer> emptyData = Collections.emptyMap();
        return ResponseEntity.ok(emptyData);
    }
}

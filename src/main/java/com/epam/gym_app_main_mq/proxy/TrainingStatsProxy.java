package com.epam.gym_app_main_mq.proxy;

import com.epam.gym_app_main_mq.api.FullStatRequest;
import com.epam.gym_app_main_mq.api.MonthlyStatRequest;
import com.epam.gym_app_main_mq.api.UpdateStatRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@FeignClient(name = "training-stats-service")
public interface TrainingStatsProxy {

    @GetMapping("/stats-api/v1/trainer-full-stats")
    @CircuitBreaker(name = "trainingStatsService", fallbackMethod = "fallbackFullStats")
    public ResponseEntity<Map<Integer, List<Map<String, Integer>>>> getTrainerFullStats(
            @RequestBody FullStatRequest fullStatRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    );

    @GetMapping("/stats-api/v1/trainer-monthly-stats")
    @CircuitBreaker(name = "trainingStatsService", fallbackMethod = "fallbackMonthlyStats")
    public ResponseEntity<Map<String, Integer>> getTrainerMonthlyStats(
            @RequestBody MonthlyStatRequest monthlyStatRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    );

    @PostMapping("/stats-api/v1/trainer-stats-update")
    @CircuitBreaker(name = "trainingStatsService", fallbackMethod = "fallbackUpdateTrainerStats")
    public ResponseEntity<Map<String, Integer>> updateTrainerStats(
            @RequestBody UpdateStatRequest updateStatRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    );

    private ResponseEntity<Map<Integer, List<Map<String, Integer>>>> fallbackFullStats(FullStatRequest fullStatRequest, String correlationId){
        Map<Integer, List<Map<String, Integer>>> emptyData = Collections.emptyMap();
        return ResponseEntity.ok(emptyData);
    }

    private ResponseEntity<Map<String, Integer>> fallbackMonthlyStats(MonthlyStatRequest monthlyStatRequest, String correlationId){
        Map<String, Integer> emptyData = Collections.emptyMap();
        return ResponseEntity.ok(emptyData);
    }

    private ResponseEntity<Map<String, Integer>> fallbackUpdateTrainerStats(UpdateStatRequest updateStatRequest, String correlationId){
        Map<String, Integer> emptyData = Collections.emptyMap();
        return ResponseEntity.ok(emptyData);
    }

}

package com.epam.gym_app_main_mq.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MaintenanceHoursHealth implements HealthIndicator {
    @Override
    public Health health() {
        int maintenanceStartHour = 10;
        int maintenanceEndHour = 18;
        LocalDateTime now = LocalDateTime.now();
        int currentHour = now.getHour();
        log.info("\n\ncurrentHour: " + currentHour + "\n");
        String message_key = "maintenance hours";
        if (currentHour >= maintenanceStartHour && currentHour <= maintenanceEndHour) {
            return Health.outOfService().withDetail(message_key, true).build();
        }
        return Health.up().withDetail(message_key, false).build();
    }
}

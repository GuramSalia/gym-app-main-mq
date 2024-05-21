package com.epam.gym_app_main_mq.global;

import com.epam.gym_app_main_mq.service.TraineeService;
import com.epam.gym_app_main_mq.service.TrainerService;
import com.epam.gym_app_main_mq.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyDbHealth implements HealthIndicator {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public MyDbHealth(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @Override
    public Health health() {
        int traineesCountInDb = traineeService.getTrainees().size();
        int trainersCountInDb = trainerService.getTrainers().size();
        int trainingsCountInDb = trainingService.getTrainingList().size();

        boolean areTraineesInDb = traineesCountInDb > 1;
        boolean areTrainersInDb = trainersCountInDb > 1;
        boolean areTrainingsInDb = trainingsCountInDb > 1;

        log.info("\n\nDB health check: \n");
        String message_key = "DB initialized with values";
        if (areTraineesInDb && areTrainersInDb && areTrainingsInDb) {
            return Health.up().withDetail(message_key, true).build();
        }
        return Health.down().withDetail(message_key, false).build();
    }
}

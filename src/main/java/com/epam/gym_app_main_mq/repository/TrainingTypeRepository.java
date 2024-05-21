package com.epam.gym_app_main_mq.repository;

import com.epam.gym_app_main_mq.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
        TrainingType findByTrainingType(TrainingType.TrainingTypeEnum trainingType);

}

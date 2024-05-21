package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.model.TrainingType;
import com.epam.gym_app_main_mq.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public TrainingType getTrainingType(TrainingType.TrainingTypeEnum trainingType) {
        return trainingTypeRepository.findByTrainingType(trainingType);
    }

    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

    public TrainingType getTrainingTypeByString(String trainingTypeString) {
        TrainingType.TrainingTypeEnum enumValue =
                TrainingType.TrainingTypeEnum.valueOf(trainingTypeString.toUpperCase());
        return trainingTypeRepository.findByTrainingType(enumValue);
    }
}

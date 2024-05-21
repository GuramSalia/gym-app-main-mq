package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.model.Training;
import com.epam.gym_app_main_mq.repository.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("TRAINING_DATABASE")
@Profile("!dev_in_memory")
public class TrainingDAODatabaseImpl implements TrainingDAO {

    TrainingRepository trainingRepository;

    public TrainingDAODatabaseImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Optional<Training> create(Training training) {
        return Optional.of(trainingRepository.save(training));
    }

    @Override
    public Optional<Training> getById(int id) {
        return trainingRepository.findById(id);
    }

    @Override
    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getTrainingsByTraineeAndOtherFilters(
            String traineeUsername,
            Date startDate,
            Date endDate,
            String trainerUsername,
            String trainingTypeName) {
        List<Integer> trainingIds = new ArrayList<>();
        trainingIds = trainingRepository.findIdsByTraineeAndTrainerAndType(
                traineeUsername, startDate, endDate,
                trainerUsername, trainingTypeName);

        log.info("\n\n-----------------trainingIds by Trainee: "+trainingIds.toString());

        List<Training> trainings = new ArrayList<>();
        for (Integer trainingId : trainingIds) {
            trainings.add(trainingRepository.findById(trainingId).get());
        }

        return trainings;
    }

    @Override
    public List<Training> getTrainingsByTrainerAndOtherFilters(
            String traineeUsername,
            Date startDate,
            Date endDate,
            String trainerUsername) {

        List<Integer> trainingIds = new ArrayList<>();
        trainingIds = trainingRepository.findIdsByTrainerAndTrainerAndType(
                traineeUsername,
                startDate,
                endDate,
                trainerUsername);

        log.info("\n\n-----------------trainingIds: "+trainingIds.toString());

        List<Training> trainings = new ArrayList<>();

        for (Integer trainingId : trainingIds) {
            trainings.add(trainingRepository.findById(trainingId).get());
        }

        return trainings;
    }

    @Override
    public void delete(Training training) {
        trainingRepository.delete(training);
    }
}

package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.config.storage.InMemoryStorage;
import com.epam.gym_app_main_mq.config.storage.idgeneration.TrainingIdForInMemoryStorage;
import com.epam.gym_app_main_mq.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Profile("dev_in_memory")
@Repository("TRAINING_IN_MEMORY")
public class TrainingDAOInMemoryStorageImpl implements TrainingDAO {
    //    private final Map<Integer, Training> trainings;
    private final InMemoryStorage storage;

    public TrainingDAOInMemoryStorageImpl(InMemoryStorage storage) {
        //        this.trainings = storage.getTrainings();
        this.storage = storage;
    }

    @Override
    public Optional<Training> create(Training training) {
//        int id = training.getTrainingId();
        int id = TrainingIdForInMemoryStorage.getNewId();
        training.setTrainingId(id);
        if (storage.getTrainings().containsKey(id)) {
            log.error("Training with id {} already exists", id);
            return Optional.empty();
        }
        storage.getTrainings().put(id, training);
        return Optional.of(storage.getTrainings().get(id));
    }

    @Override
    public Optional<Training> getById(int id) {
        return Optional.ofNullable(storage.getTrainings().get(id));
    }

    @Override
    public List<Training> getTrainings() {return new ArrayList<>(storage.getTrainings().values());}

    @Override
    public List<Training> getTrainingsByTraineeAndOtherFilters(
            String traineeUsername,
            Date startDate,
            Date endDate,
            String trainerUsername,
            String trainingTypeName) {
        log.info("getTrainingsByTraineeAndOtherFilters In Memory Storage");
        return storage.getTrainings()
                      .values()
                      .stream()
                      .filter(training ->
                                      training.getTrainee().getUsername().equals(traineeUsername)
                                              && training.getTrainingDate().compareTo(startDate) > 0
                                              && training.getTrainingDate().compareTo(endDate) < 0
                                              && training.getTrainer().getUsername().equals(trainerUsername)
                                              && training.getTrainingType()
                                                         .getTrainingType()
                                                         .toString()
                                                         .equals(trainingTypeName))
                      .collect(Collectors.toList());
    }

    @Override
    public List<Training> getTrainingsByTrainerAndOtherFilters(
            String trainerUsername,
            Date startDate,
            Date endDate,
            String traineeUsername) {
        log.info("getTrainingsByTrainerAndOtherFilters In Memory Storage");
        return storage.getTrainings()
                      .values()
                      .stream()
                      .filter(training ->
                                      training.getTrainer().getUsername().equals(trainerUsername)
                                              && training.getTrainingDate().compareTo(startDate) > 0
                                              && training.getTrainingDate().compareTo(endDate) < 0
                                              && training.getTrainee().getUsername().equals(traineeUsername)
                             )
                      .collect(Collectors.toList());
    }

    @Override
    public void delete(Training training) {
        log.info("delete training not implemented here");
    }
}

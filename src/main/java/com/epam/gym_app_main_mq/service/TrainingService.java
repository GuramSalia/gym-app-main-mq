package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.dao.TrainerDAO;
import com.epam.gym_app_main_mq.dao.TrainingDAO;
import com.epam.gym_app_main_mq.exception.TrainingNotCreatedException;
import com.epam.gym_app_main_mq.exception.TrainingNotFoundException;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.model.Training;
import com.epam.gym_app_main_mq.model.TrainingType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainingService(
            TrainingDAO trainingDAO,
            TrainerDAO trainerDAO
    ) {
        this.trainingDAO = trainingDAO;
        this.trainerDAO = trainerDAO;
    }

    public Training getById(int id) {
        log.info(">>>> Getting training with id: {}", id);

        Optional<Training> trainingOptional = trainingDAO.getById(id);
        if (trainingOptional.isEmpty()) {
            log.error("Cannot get training with id: {}", id);
            throw new TrainingNotFoundException("training not found");
        }
        return trainingOptional.get();
    }

    public List<Training> getTrainingList() {return trainingDAO.getTrainings();}

    @Transactional
    public Training create(Training training) {

        TrainingType trainingType = training.getTrainingType();
        TrainingType trainerSpecialization;
        Optional<Trainer> optionalTrainer = Optional.ofNullable(training.getTrainer());

        if (optionalTrainer.isEmpty()) {
            log.info("There is no such trainer as indicated by training");
            throw new TrainingNotCreatedException("no trainer indicated by training");
        }

        Optional<Trainer> trainerFromDb = trainerDAO.findByUsername(optionalTrainer.get().getUsername());
        if (trainerFromDb.isEmpty()) {
            log.error("Cannot create training, because the trainer does not exist");
            throw new TrainingNotCreatedException("There is no such trainer as indicated by training");
        }

        trainerSpecialization = optionalTrainer.get().getSpecialization();

        if (areMismatchingTrainingTypes(trainingType, trainerSpecialization)) {
            log.error("cannot create training, because the trainer has a different specialization");
            throw new TrainingNotCreatedException("cannot create training, because the trainer has a different " +
                                                          "specialization");
        }

        Optional<Training> trainingOptional = trainingDAO.create(training);
        if (trainingOptional.isEmpty()) {
            log.error("Cannot create training");
            throw new TrainingNotCreatedException("Cannot create training");
        }

        log.info(">>>> Creating training: " + training.getTrainingName());
        return trainingOptional.get();
    }

    public void delete(Integer trainingId) {
        log.info(">>>> TRAINING SERVICE > start deleting training with id: {}", trainingId);

        Optional<Training> trainingOptional = trainingDAO.getById(trainingId);
        if (trainingOptional.isEmpty()) {
            log.error(">>>> TRAINING SERVICE > Cannot delete (because not found) training with id: {}", trainingId);
            throw new TrainingNotFoundException("TRAINING SERVICE > Cannot delete training with id: " + trainingId);
        }

        trainingDAO.delete(trainingOptional.get());
        log.info(">>>>TRAINING SERVICE > Deleting training with id: {}", trainingId);
    }

    public List<Training> getTrainingsByTraineeAndOtherFilters(
            String traineeUsername,
            Date startDate,
            Date endDate,
            String trainerUsername,
            String trainingTypeName
    ) {
        List<Training> trainingsByTrainee = trainingDAO.getTrainingsByTraineeAndOtherFilters(
                traineeUsername, startDate, endDate, trainerUsername, trainingTypeName);
        log.info("\n\n>>>> Getting trainings by trainee: {}\n", trainingsByTrainee.toString());
        return trainingsByTrainee;
    }

    public List<Training> getTrainingsByTrainerAndOtherFilters(
            String trainerUsername,
            Date startDate,
            Date endDate,
            String traineeUsername
    ) {
        List<Training> trainingsByTrainer = trainingDAO.getTrainingsByTrainerAndOtherFilters(
                trainerUsername, startDate, endDate, traineeUsername);
        log.info("\n\n>>>> Getting trainings by trainer: {}\n", trainingsByTrainer.toString());
        return trainingsByTrainer;
    }

    private boolean areMismatchingTrainingTypes(TrainingType type1, TrainingType type2) {
        boolean matching = false;

        if (type1 != null && type2 != null) {
            matching = type1.equals(type2);
        }

        if (type1 == null && type2 == null) {
            matching = true;
        }
        return !matching;
    }
}

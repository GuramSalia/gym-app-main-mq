package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.config.storage.InMemoryStorage;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.model.Training;
import com.epam.gym_app_main_mq.model.TrainingType;
import com.epam.gym_app_main_mq.service.TrainerService;
import com.epam.gym_app_main_mq.service.TrainingService;
import com.epam.gym_app_main_mq.utils.CheckIfUserHasRequiredFields;
import com.epam.gym_app_main_mq.utils.CheckIfUsernameExists;
import com.epam.gym_app_main_mq.utils.UsernameGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingDAOInMemoryStorageImplTest {
    InMemoryStorage storage;
    TraineeDAO traineeDAO;
    TrainerDAO trainerDAO;
    TrainingDAO trainingDAO;
    CheckIfUsernameExists checkIfUsernameExists;
    CheckIfUserHasRequiredFields checkIfUserHasRequiredFields;
    TrainerService trainerService;
    TrainingService trainingService;
    UsernameGenerator usernameGenerator;

    Trainer trainer1;
    Trainer trainer2;

    Training training1;
    Training training2;
    Training training3;

    @BeforeEach
    void setUpBeforeEach() {
        storage = new InMemoryStorage();
        traineeDAO = new TraineeDAOInMemoryStorageImpl(storage);
        trainerDAO = new TrainerDAOInMemoryStorageImpl(storage);
        trainingDAO = new TrainingDAOInMemoryStorageImpl(storage);
        usernameGenerator = new UsernameGenerator(trainerDAO, traineeDAO);
        checkIfUsernameExists = new CheckIfUsernameExists();
        checkIfUserHasRequiredFields = new CheckIfUserHasRequiredFields();

        // new way of creating trainerService
        Map<String, TrainerDAO> trainerDAOMap = new HashMap<>();
        trainerDAOMap.put("TRAINER_IN_MEMORY", trainerDAO);
        Map<String, TraineeDAO> traineeDAOMap = new HashMap<>();
        traineeDAOMap.put("TRAINEE_IN_MEMORY", traineeDAO);
        trainerService = new TrainerService(
                trainerDAO, traineeDAO, checkIfUsernameExists,
                checkIfUserHasRequiredFields, usernameGenerator);
//        trainerService.setTrainerDAO(trainerDAO);

        // new way of creating trainingService
        Map<String, TrainingDAO> trainingDAOMap = new HashMap<>();
        trainingDAOMap.put("TRAINING_IN_MEMORY", trainingDAO);
        trainingService = new TrainingService(trainingDAO, trainerDAO);
//        trainingService.setTrainingDAO(trainingDAO);
//        trainingService.setTrainerDAO(trainerDAO);

        TrainingType YOGA = new TrainingType();
        YOGA.setTrainingType(TrainingType.TrainingTypeEnum.YOGA);
        TrainingType CARDIO = new TrainingType();
        CARDIO.setTrainingType(TrainingType.TrainingTypeEnum.CARDIO);

        trainer1 = new Trainer();
        trainer1.setUserId(1);
        trainer1.setFirstName("John");
        trainer1.setLastName("Doe");
        trainer1.setSpecialization(YOGA);
        trainerService.create(trainer1);

        trainer2 = new Trainer();
        trainer2.setUserId(2);
        trainer2.setFirstName("Bob");
        trainer2.setLastName("Brown");
        trainer2.setSpecialization(CARDIO);
        trainerService.create(trainer2);

        training1 = new Training();
        training1.setTrainingId(1);
        training1.setTrainingName("training1");
        training1.setTrainingType(YOGA);
        training1.setTrainingDurationInMinutes(25);
        //        training1.setTrainee(1);
        training1.setTrainer(trainer1);
        trainingService.create(training1);

        training2 = new Training();
        training2.setTrainingId(2);
        training3 = new Training();
        training3.setTrainingId(3);
    }

    @AfterEach
    void tearDownAfterEach() {
        storage.clearStorage();
    }

//    @Test
//    void testCreateInTrainingDAO() {
//        training2.setTrainingId(10);
//        assertEquals(trainingDAO.create(training2), Optional.of(training2));
//        training1.setTrainingId(10);
//        assertEquals(trainingDAO.create(training1), Optional.empty());
//    }
//
//    @Test
//        //    @Disabled
//    void testGetByIdInTrainingDAO() {
//        trainingDAO.create(training1);
//        trainingDAO.create(training2);
//        assertEquals(trainingDAO.getTrainings(), List.of(training1, training2));
//    }
}
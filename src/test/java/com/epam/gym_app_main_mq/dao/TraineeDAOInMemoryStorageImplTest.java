package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.config.storage.InMemoryStorage;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.service.TraineeService;
import com.epam.gym_app_main_mq.service.TrainerService;
import com.epam.gym_app_main_mq.utils.CheckIfUserHasRequiredFields;
import com.epam.gym_app_main_mq.utils.CheckIfUsernameExists;
import com.epam.gym_app_main_mq.utils.UsernameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(MockitoExtension.class)
        //@Disabled
class TraineeDAOInMemoryStorageImplTest {

    InMemoryStorage storage;
    TraineeDAO traineeDAO;
    TrainerDAO trainerDAO;
    TrainerService trainerService;
    TraineeService traineeService;
    CheckIfUsernameExists checkIfUsernameExists;
    CheckIfUserHasRequiredFields checkIfUserHasRequiredFields;
    UsernameGenerator usernameGenerator;
    Trainee trainee1;
    Trainee trainee2;
    Trainee trainee3;

    @BeforeEach
    void setUpBeforeEach() {
        storage = new InMemoryStorage();
        traineeDAO = new TraineeDAOInMemoryStorageImpl(storage);
        trainerDAO = new TrainerDAOInMemoryStorageImpl(storage);
        usernameGenerator = new UsernameGenerator(trainerDAO, traineeDAO);
        checkIfUsernameExists = new CheckIfUsernameExists();
        checkIfUserHasRequiredFields = new CheckIfUserHasRequiredFields();

        // new way of creating traineeService
        Map<String, TraineeDAO> traineeDAOMap = new HashMap<>();
        traineeDAOMap.put("TRAINEE_IN_MEMORY", traineeDAO);
        traineeService = new TraineeService(traineeDAO, checkIfUsernameExists, checkIfUserHasRequiredFields, usernameGenerator);
//        traineeService.setTraineeDAO(traineeDAO);

        // new way of creating trainerService
        Map<String, TrainerDAO> trainerDAOMap = new HashMap<>();
        trainerDAOMap.put("TRAINER_IN_MEMORY", trainerDAO);

        trainerService = new TrainerService(trainerDAO, traineeDAO, checkIfUsernameExists, checkIfUserHasRequiredFields,
                                            usernameGenerator);
//        trainerService.setTrainerDAO(trainerDAO);

        trainee1 = new Trainee();
        trainee1.setUserId(1);
        trainee1.setFirstName("Sam");
        trainee1.setLastName("Smith");
        trainee1.setAddress("trainee1 address");
        trainee1.setIsActive(true);
        traineeService.create(trainee1);

        trainee2 = new Trainee();
        trainee2.setUserId(2);
        trainee3 = new Trainee();
        trainee3.setUserId(3);
    }

    @AfterEach
    void tearDownAfterEach() {
        storage.clearStorage();
    }

    @Test
    void testCreateInTraineeDAO() {
        trainee1.setUserId(7);
        assertEquals(traineeDAO.create(trainee1), Optional.of(trainee1));

    }

//    @Test
//    void testUpdateInTraineeDAO() {
//        trainee2.setUserId(5);
//        assertEquals(traineeDAO.update(trainee2), Optional.empty());
//        traineeDAO.create(trainee2);
//        trainee2.setIsActive(true);
//        assertEquals(traineeDAO.update(trainee2), Optional.of(trainee2));
//    }

    @Test
    void testDeleteInTraineeDAO() {
        assertTrue(traineeDAO.delete(trainee1));
    }

//    @Test
//    void testGetByIdInTraineeDAO() {
//        assertEquals(traineeDAO.getById(1), Optional.of(trainee1));
//    }
//
//    @Test
//    void testGetInTraineesDAO() {
//        traineeDAO.create(trainee1);
//        traineeDAO.create(trainee2);
//        traineeDAO.create(trainee3);
//        assertEquals(traineeDAO.getTrainees(), List.of(trainee1, trainee2, trainee3));
//    }
}
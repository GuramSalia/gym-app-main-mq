package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.dao.TraineeDAO;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.utils.CheckIfUsernameExists;
import com.epam.gym_app_main_mq.utils.CheckIfUserHasRequiredFields;
import com.epam.gym_app_main_mq.utils.UsernameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private CheckIfUserHasRequiredFields checkIfUserHasRequiredFields;

    @Mock
    private CheckIfUsernameExists checkIfUsernameExists;

    TraineeService traineeService;

    private Trainee trainee1;
    private Trainee trainee2;

    @BeforeEach
    void setUpBeforeEach() {
        trainee1 = new Trainee();
        trainee1.setUserId(10);
        trainee1.setFirstName("John");
        trainee1.setLastName("Doe");
        trainee1.setPassword("123");
        trainee1.setIsActive(true);

        trainee2 = new Trainee();
        trainee2.setUserId(20);
        trainee2.setFirstName("Tommy");
        trainee2.setLastName("Smith");
        trainee2.setPassword("123");
        trainee2.setIsActive(true);

        Map<String, TraineeDAO> traineeDAOMap = new HashMap<>();
        traineeDAOMap.put("IN_MEMORY", traineeDAO);

        traineeService = new TraineeService(traineeDAOMap, checkIfUsernameExists, checkIfUserHasRequiredFields,
                                            usernameGenerator);

        traineeService.setTraineeDAO(traineeDAO);
    }

    @Test
    void testCreateInTraineeService() {

        log.info("1....");
        when(usernameGenerator.generateUsername(trainee1)).thenReturn("John.Doe");
        when(traineeDAO.create(trainee1)).thenReturn(Optional.of(trainee1));
        when(traineeDAO.getById(10)).thenReturn(Optional.of(trainee1));
        log.info("2....");
        Optional<Trainee> result = Optional.ofNullable(traineeService.create(trainee1));
        log.info("3....");
        assertEquals(
                result,
                traineeDAO.getById(10),
                "created trainee should be in trainers list");
    }

    @Test
    void testUpdateInTraineeService() {
        lenient().when(usernameGenerator.generateUsername(trainee1)).thenReturn("John.Doe");
        lenient().when(traineeDAO.update(trainee1)).thenReturn(Optional.of(trainee1));
        lenient().when(traineeDAO.getById(10)).thenReturn(Optional.of(trainee1));
        Optional<Trainee> result = Optional.ofNullable(traineeService.update(trainee1));
        assertEquals(
                result,
                traineeDAO.getById(10),
                "updated trainee should be in trainees list");
    }

    @Test
    void testUpdateWithDbInTraineeService() {

        Optional<Trainee> trainee = Optional.of(trainee1);
        trainee1.setUsername("John.Doe");

        lenient().when(checkIfUsernameExists.usernameExists(traineeDAO, "John.Doe", "123")).thenReturn(true);

        lenient().when(usernameGenerator.generateUsername(trainee1)).thenReturn("John.Doe");
        lenient().when(traineeDAO.update(trainee1)).thenReturn(Optional.of(trainee1));
        lenient().when(traineeDAO.getById(10)).thenReturn(Optional.of(trainee1));
        Optional<Trainee> result = Optional.ofNullable(traineeService.update(trainee1));

        traineeService.update(trainee1, "John.Doe", "123");
        assertEquals(
                result,
                traineeDAO.getById(10),
                "updated trainee should be in trainees list");
    }

    @Test
    void testDeleteInTraineeService() {

        when(traineeDAO.delete(trainee1)).thenReturn(true);
        boolean result = traineeService.delete(trainee1);
        assertTrue(result, "delete should return true");
    }

    @Test
    void testDeleteWithDbInTraineeService() {

        trainee1.setUsername("John.Doe");
        when(checkIfUsernameExists.usernameExists(traineeDAO, "John.Doe", "123")).thenReturn(true);
        when(traineeDAO.findByUsername("John.Doe")).thenReturn(Optional.of(trainee1));
        when(traineeDAO.delete(trainee1)).thenReturn(true);
        boolean result = traineeService.delete("John.Doe", "123");
        assertTrue(result, "delete should return true");
    }

    @Test
    void TestGetByIdInTraineeService() {
        trainee1.setUsername("John.Doe");
        trainee2.setUsername("Tommy.Smith");

        lenient().when(traineeDAO.getById(10)).thenReturn(Optional.of(trainee1));
        lenient().when(checkIfUsernameExists.usernameExists(traineeDAO, "John.Doe", "123")).thenReturn(true);
        lenient().when(traineeDAO.getById(20)).thenReturn(Optional.of(trainee2));
        lenient().when(checkIfUsernameExists.usernameExists(traineeDAO, "Tommy.Smith", "123")).thenReturn(true);
        assertAll(
                () -> assertEquals(
                        trainee1,
                        traineeService.getById(10, "John.Doe", "123"),
                        "trainee should be returned"),
                () -> assertEquals(
                        trainee2,
                        traineeService.getById(20, "Tommy.Smith", "123"),
                        "trainee should be returned")
                 );
    }

    @Test
    void TestGetTraineesInTrainerService() {
        when(traineeDAO.getTrainees()).thenReturn(List.of(trainee1, trainee2));
        assertEquals(2, traineeService.getTrainees().size(), "trainees should be returned");
        assertEquals(trainee1, traineeService.getTrainees().get(0), "trainees should be returned");
        assertEquals(trainee2, traineeService.getTrainees().get(1), "trainees should be returned");
    }

//    @Test
//    void TestGetByUsernameInTraineeService() {
//        when(authentication.isAuthenticated(traineeDAO, "John.Doe", "123")).thenReturn(true);
//        when(traineeDAO.findByUsername("John.Doe")).thenReturn(Optional.of(trainee1));
//        Optional<Trainee> result = Optional.ofNullable(traineeService.findByUsernameAndPassword("John.Doe", "123"));
//        assertEquals(result, Optional.of(trainee1), "trainees should be returned");
//    }

    @Test
    void TestUpdatePasswordInTraineeService() {
        trainee1.setUsername("John.Doe");
        when(checkIfUsernameExists.usernameExists(traineeDAO, "John.Doe", "123")).thenReturn(true);
        when(traineeDAO.update(trainee1)).thenReturn(Optional.of(trainee1));
        Optional<Trainee> result = traineeService.updatePassword(trainee1, "John.Doe", "123", "321");
        assertEquals(result, Optional.of(trainee1), "trainees should be returned");
    }

    @Test
    void TestActivateTraineeInTraineeService() {
        trainee1.setUsername("John.Doe");
        when(checkIfUsernameExists.usernameExists(traineeDAO, "John.Doe", "123")).thenReturn(true);
        boolean result = traineeService.activateTrainee(trainee1, "John.Doe", "123");
        assertFalse(result, "should be false because trainee is already activated");
        trainee1.setIsActive(false);
        result = traineeService.activateTrainee(trainee1, "John.Doe", "123");
        assertTrue(result, "should be true because trainee is already activated");
    }

    @Test
    void TestDeactivateTraineeInTraineeService() {
        trainee1.setUsername("John.Doe");
        when(checkIfUsernameExists.usernameExists(traineeDAO, "John.Doe", "123")).thenReturn(true);
        boolean result = traineeService.deactivateTrainee(trainee1, "John.Doe", "123");
        assertTrue(result, "should be true because trainee is already activated");
        result = traineeService.deactivateTrainee(trainee1, "John.Doe", "123");
        assertFalse(result, "should be false because trainee is already activated");
    }
}
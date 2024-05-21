package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.dao.TrainerDAO;
import com.epam.gym_app_main_mq.dao.TrainingDAO;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.model.Training;
import com.epam.gym_app_main_mq.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    TrainingService trainingService;

    @Mock
    private TrainingDAO trainingDAO;
    @Mock
    private TrainerDAO trainerDAO;

    private Training training1;
    private Training training2;
    private Trainer trainer1;
    private Trainer trainer2;
    @Mock
    private TrainingType yoga;
    @Mock
    private TrainingType cardio;

    @BeforeEach
    void setUp() {
        Map<String, TrainingDAO> trainingDAOMap = new HashMap<>();
        Map<String, TrainerDAO> trainerDAOMap = new HashMap<>();
        trainingDAOMap.put("TRAINING_DATABASE", trainingDAO);
        trainerDAOMap.put("TRAINER_DATABASE", trainerDAO);

        trainingService = new TrainingService(trainingDAO, trainerDAO);
//        trainingService.setTrainingDAO(trainingDAO);
//        trainingService.setTrainerDAO(trainerDAO);

        yoga = new TrainingType();
        cardio = new TrainingType();
        yoga.setTrainingType(TrainingType.TrainingTypeEnum.YOGA);
        cardio.setTrainingType(TrainingType.TrainingTypeEnum.CARDIO);

        trainer1 = new Trainer();
        trainer1.setUserId(1);
        trainer1.setSpecialization(yoga);
        trainer1.setFirstName("John");
        trainer1.setLastName("Doe");
        trainer1.setUsername("John.Doe");
        trainer1.setPassword("123");
        trainer1.setIsActive(true);

        trainer2 = new Trainer();
        trainer2.setUserId(2);
        trainer2.setSpecialization(cardio);
        trainer2.setFirstName("Sam");
        trainer2.setLastName("Smith");
        trainer2.setUsername("Sam.Smith");
        trainer2.setPassword("123");
        trainer2.setIsActive(true);

        Date date1;
        Date date2;
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.MARCH, 8);
        date1 = cal.getTime();
        cal.set(2024, Calendar.MARCH, 12);
        date2 = cal.getTime();

        training1 = new Training();
        training1.setTrainingId(1);
        training1.setTrainingName("training1");
        training1.setTrainingType(yoga);
        training1.setTrainingDate(date1);
        training1.setTrainer(trainer1);

        training2 = new Training();
        training2.setTrainingId(2);
        training2.setTrainingName("training2");
        training2.setTrainingType(cardio);
        training2.setTrainingDate(date2);
        training2.setTrainer(trainer2);
    }

    @Test
    void testCreateInTrainingService() {

        lenient().when(trainerDAO.findByUsername(trainer1.getUsername())).thenReturn(Optional.of(trainer1));
        lenient().when(trainingDAO.create(training1)).thenReturn(Optional.of(training1));
        lenient().when(trainingDAO.getById(1)).thenReturn(Optional.of(training1));
        Optional<Training> result = Optional.ofNullable(trainingService.create(training1));

        assertEquals(result, Optional.ofNullable(training1), "created training should be in trainings list");
    }

    @Test
    void TestGetByIdInTrainingService() {

        when(trainingDAO.getById(1)).thenReturn(Optional.of(training1));
        when(trainingDAO.getById(2)).thenReturn(Optional.of(training2));
        Optional<Training> result1 = Optional.ofNullable(trainingService.getById(1));
        Optional<Training> result2 = Optional.ofNullable(trainingService.getById(2));

        assertAll(
                () -> assertEquals(result1, Optional.of(training1), "training should be returned"),
                () -> assertEquals(result2, Optional.of(training2), "training should be returned")
                 );

        TrainingType HIIT = new TrainingType();
        HIIT.setTrainingType(TrainingType.TrainingTypeEnum.HIIT);
    }

    @Test
    void getTrainingsByTraineeAndOtherFilters() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MARCH, 8);
        Date dateFrom = cal.getTime();
        cal.set(2025, Calendar.MARCH, 8);
        Date dateTo = cal.getTime();
        java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);

        when(trainingDAO.getTrainingsByTraineeAndOtherFilters(
                "John.Doe",
                sqlDateFrom,
                sqlDateTo,
                "Sam.Smith",
                "cardio")).thenReturn(trainings);

        List<Training> result = trainingService
                .getTrainingsByTraineeAndOtherFilters(
                        "John.Doe",
                        sqlDateFrom,
                        sqlDateTo,
                        "Sam.Smith",
                        "cardio");

        assertEquals(result, trainings, "training should be returned");
    }

    @Test
    void getTrainingsByTrainerAndOtherFilters() {

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MARCH, 8);
        Date dateFrom = cal.getTime();
        cal.set(2025, Calendar.MARCH, 8);
        Date dateTo = cal.getTime();
        java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);

        when(trainingDAO.getTrainingsByTrainerAndOtherFilters(
                "John.Doe",
                sqlDateFrom,
                sqlDateTo,
                "Sam.Smith")).thenReturn(trainings);

        List<Training> result = trainingService
                .getTrainingsByTrainerAndOtherFilters(
                        "John.Doe",
                        sqlDateFrom,
                        sqlDateTo,
                        "Sam.Smith");

        assertEquals(result, trainings, "training should be returned");
    }
}
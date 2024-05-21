package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.model.Training;
import com.epam.gym_app_main_mq.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingDAODatabaseImplTest {
    TrainingDAODatabaseImpl trainingDB;
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private Training training1;

    @Mock
    private Training training2;

    @BeforeEach
    void setUp() {
        trainingDB = new TrainingDAODatabaseImpl(trainingRepository);
    }

    @Test
    void create() {
        when(trainingRepository.save(training1)).thenReturn(training1);
        assertEquals(trainingDB.create(training1), Optional.of(training1));
    }

    @Test
    void getById() {
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training1));
        assertEquals(trainingDB.getById(1), Optional.of(training1));
    }

    @Test
    void getTrainings() {
        when(trainingRepository.findAll()).thenReturn(List.of(training1, training2));
        assertEquals(trainingDB.getTrainings(), List.of(training1, training2));
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

        when(trainingRepository.findIdsByTraineeAndTrainerAndType(
                "Sam.Smith",
                sqlDateFrom,
                sqlDateTo,
                "Sam.Smith",
                "CARDIO")).thenReturn(List.of(1, 2));
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training1));
        when(trainingRepository.findById(2)).thenReturn(Optional.of(training2));

        List<Training> result = trainingDB.getTrainingsByTraineeAndOtherFilters(
                "Sam.Smith",
                sqlDateFrom,
                sqlDateTo,
                "Sam.Smith", "CARDIO");
        assertEquals(result, List.of(training1, training2), "training should be returned");
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

        when(trainingRepository.findIdsByTrainerAndTrainerAndType(
                "Sam.Smith",
                sqlDateFrom,
                sqlDateTo,
                "Sam.Smith")).thenReturn(List.of(1, 2));
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training1));
        when(trainingRepository.findById(2)).thenReturn(Optional.of(training2));

        List<Training> result = trainingDB.getTrainingsByTrainerAndOtherFilters(
                "Sam.Smith",
                sqlDateFrom,
                sqlDateTo,
                "Sam.Smith");
        assertEquals(result, List.of(training1, training2), "training should be returned");
    }
}
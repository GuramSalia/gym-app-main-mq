package com.epam.gym_app_main_mq.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrainingRepositoryTest {

    @Autowired
    TrainingRepository trainingRepository;

    java.sql.Date sqlDateFrom;
    java.sql.Date sqlDateTo;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.set(1980, Calendar.MARCH, 8);
        Date dateFrom = cal.getTime();
        cal.set(2050, Calendar.MARCH, 8);
        Date dateTo = cal.getTime();
        sqlDateFrom = new java.sql.Date(dateFrom.getTime());
        sqlDateTo = new java.sql.Date(dateTo.getTime());
    }

    @Test
    void findIdsByTraineeAndTrainerAndType() {
        String traineeUsername = "Jane.Smith";
        java.sql.Date from = sqlDateFrom;
        java.sql.Date to = sqlDateTo;
        String trainerUsername = "Sam.Jones";
        String trainingTypeName = "STRENGTH";

        List<Integer> trainingIds = trainingRepository.findIdsByTraineeAndTrainerAndType(
                traineeUsername,
                null,
                null,
                null,
                null);

        assertEquals(List.of(2),trainingIds,  "training should be returned");
    }

    @Test
    void findIdsByTrainerAndTrainerAndType() {
        String traineeUsername = "Jane.Smith";
        java.sql.Date from = sqlDateFrom;
        java.sql.Date to = sqlDateTo;
        String trainerUsername = "Sam.Jones";
        String trainingTypeName = "STRENGTH";
        List<Integer> trainingIds = trainingRepository.findIdsByTrainerAndTrainerAndType(
                trainerUsername,
                null,
                null,
                null);

        assertEquals(List.of(2),trainingIds,  "training should be returned");
    }
}
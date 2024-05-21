package com.epam.gym_app_main_mq.utils;

import com.epam.gym_app_main_mq.dao.TraineeDAO;
import com.epam.gym_app_main_mq.dao.TrainerDAO;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.model.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("When generating username with generateUsername method")
@ExtendWith(MockitoExtension.class)
class UsernameGeneratorTest {
    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private TrainerDAO trainerDAO;

    private Trainee traineeInDb1;

    private Trainee traineeNew;

    private Trainer trainerInDb1;

    private Trainer trainerNew;

    @InjectMocks
    private UsernameGenerator usernameGenerator;

    @BeforeEach
    void setUp() {
        traineeInDb1 = new Trainee();
        traineeInDb1.setUsername("John.Doe");
        traineeInDb1.setUserId(10);

        traineeNew = new Trainee();
        traineeNew.setFirstName("John");
        traineeNew.setLastName("Doe");
        traineeNew.setUserId(11);

        trainerInDb1 = new Trainer();
        trainerInDb1.setUsername("John.Doe");
        trainerInDb1.setUserId(20);

        trainerNew = new Trainer();
        trainerNew.setFirstName("John");
        trainerNew.setLastName("Doe");
        trainerNew.setUserId(21);
    }

    @Nested
    @DisplayName("for trainee")
    class TestUsernameGeneratorForTrainee {
        @Test
        @DisplayName("when first.last name is unique in trainees and trainers")
        void testTraineeUniqueName() {
            when(traineeDAO.getTrainees()).thenReturn(List.of());
            when(trainerDAO.getTrainers()).thenReturn(List.of());
            assertEquals("John.Doe", usernameGenerator.generateUsername(traineeNew));
        }

        @Test
        @DisplayName("when first.last name repeats in trainees")
        void testTraineeUsernameRepeatsInTrainees() {
            when(traineeDAO.getTrainees()).thenReturn(List.of(traineeInDb1));
            when(trainerDAO.getTrainers()).thenReturn(List.of());
            assertEquals("John.Doe1", usernameGenerator.generateUsername(traineeNew));
        }

        @Test
        @DisplayName("when first.last name repeats in trainers")
        void testTraineeUsernameRepeatsInTrainers() {
            when(traineeDAO.getTrainees()).thenReturn(List.of());
            when(trainerDAO.getTrainers()).thenReturn(List.of(trainerInDb1));
            assertEquals("John.Doe1", usernameGenerator.generateUsername(traineeNew));
        }
    }

    @Nested
    @DisplayName("for trainer")
    class TestUsernameGeneratorForTrainer {
        @Test
        @DisplayName("when first.last name is unique in trainees and trainers")
        void testTraineeUniqueName() {
            when(traineeDAO.getTrainees()).thenReturn(List.of());
            when(trainerDAO.getTrainers()).thenReturn(List.of());
            assertEquals("John.Doe", usernameGenerator.generateUsername(trainerNew));
        }

        @Test
        @DisplayName("when first.last name repeats in trainees")
        void testTraineeUsernameRepeatsInTrainees() {
            when(traineeDAO.getTrainees()).thenReturn(List.of(traineeInDb1));
            when(trainerDAO.getTrainers()).thenReturn(List.of());
            assertEquals("John.Doe1", usernameGenerator.generateUsername(trainerNew));
        }

        @Test
        @DisplayName("when first.last name repeats in trainers")
        void testTraineeUsernameRepeatsInTrainers() {
            when(traineeDAO.getTrainees()).thenReturn(List.of());
            when(trainerDAO.getTrainers()).thenReturn(List.of(trainerInDb1));
            assertEquals("John.Doe1", usernameGenerator.generateUsername(trainerNew));
        }
    }
}
package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDAODatabaseImplTest {

    TrainerDAODatabaseImpl trainerDB;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private Trainer trainer1;

    @Mock
    private Trainer trainer2;

    @BeforeEach
    void setUp() {
        trainerDB = new TrainerDAODatabaseImpl(trainerRepository);
    }

    @Test
    void create() {
        when(trainerRepository.save(trainer1)).thenReturn(trainer1);
        assertEquals(trainerDB.create(trainer1), Optional.of(trainer1));
    }

    @Test
    void update() {
        when(trainerRepository.save(trainer1)).thenReturn(trainer1);
        assertEquals(trainerDB.update(trainer1), Optional.of(trainer1));
    }

    @Test
    void getById() {
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer1));
        assertEquals(trainerDB.getById(1), Optional.of(trainer1));
    }

    @Test
    void getTrainers() {
        when(trainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));
        assertEquals(trainerDB.getTrainers(), List.of(trainer1, trainer2));
    }

    @Test
    void findByUsername() {
        when(trainerRepository.findByUsername("Sam.Smith")).thenReturn(Optional.of(trainer1));
        assertEquals(trainerDB.findByUsername("Sam.Smith"), Optional.of(trainer1));
    }

    @Test
    void findByUsernameAndPassword() {
        when(trainerRepository.findByUsernameAndPassword("Sam.Smith", "123")).thenReturn(Optional.of(trainer1));
        assertEquals(trainerDB.findByUsernameAndPassword("Sam.Smith", "123"), Optional.of(trainer1));
    }

    @Test
    void findUnassignedTrainersByTraineeUsername() {
        when(trainerRepository.findIdsOfUnassignedTrainersByTraineeUsername("Sam.Smith")).thenReturn(List.of(1, 2));
        assertEquals(trainerDB.findIdsOfUnassignedTrainersByTraineeUsername("Sam.Smith"), List.of(1, 2));
    }
}
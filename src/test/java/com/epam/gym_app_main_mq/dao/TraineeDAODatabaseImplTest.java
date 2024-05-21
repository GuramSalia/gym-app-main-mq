package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeDAODatabaseImplTest {
    TraineeDAODatabaseImpl traineeDB;
    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private Trainee trainee1;

    @Mock
    private Trainee trainee2;

    @BeforeEach
    void setUp() {
        traineeDB = new TraineeDAODatabaseImpl(traineeRepository);
    }

    @Test
    void create() {
        when(traineeRepository.save(trainee1)).thenReturn(trainee1);
        assertEquals(traineeDB.create(trainee1), Optional.of(trainee1));
    }

    @Test
    void update() {
        when(traineeRepository.save(trainee1)).thenReturn(trainee1);
        assertEquals(traineeDB.update(trainee1), Optional.of(trainee1));
    }

    @Test
    void delete() {
        trainee1.setUserId(1);
        doNothing().when(traineeRepository).delete(trainee1);
        assertTrue(traineeDB.delete(trainee1));
    }

    @Test
    void getById() {
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee1));
        assertEquals(traineeDB.getById(1), Optional.of(trainee1));
    }

    @Test
    void getTrainees() {
        when(traineeRepository.findAll()).thenReturn(List.of(trainee1, trainee2));
        assertEquals(traineeDB.getTrainees(), List.of(trainee1, trainee2));
    }

    @Test
    void findByUsername() {
        when(traineeRepository.findByUsername("Sam.Smith")).thenReturn(Optional.of(trainee1));
        assertEquals(traineeDB.findByUsername("Sam.Smith"), Optional.of(trainee1));
    }

    @Test
    void findByUsernameAndPassword() {
        when(traineeRepository.findByUsernameAndPassword("Sam.Smith", "123")).thenReturn(Optional.of(trainee1));
        assertEquals(traineeDB.findByUsernameAndPassword("Sam.Smith", "123"), Optional.of(trainee1));
    }

    @Test
    void findByUsernameWithQuery() {
        when(traineeRepository.findByUsernameWithQuery("Sam.Smith")).thenReturn(Optional.of(trainee1));
        assertEquals(traineeDB.findByUsernameWithQuery("Sam.Smith"), Optional.of(trainee1));
    }
}
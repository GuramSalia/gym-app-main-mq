package com.epam.gym_app_main_mq.repository;

import com.epam.gym_app_main_mq.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrainerRepositoryTest {
    @Autowired
    TrainerRepository trainerRepository;

    private Trainer trainer1;
    private Trainer trainer2;

    @BeforeEach
    void setUp() {

        trainer1 = new Trainer();
        trainer1.setUserId(100);
        trainer1.setFirstName("George");
        trainer1.setLastName("Lee");
        trainer1.setUsername("George.Lee");
        trainer1.setPassword("123");
        trainer1.setIsActive(true);


        trainer2 = new Trainer();
        trainer2.setUserId(200);
        trainer2.setFirstName("Sven");
        trainer2.setLastName("Wang");
        trainer2.setUsername("Sven.Wang");
        trainer2.setPassword("123");
        trainer2.setIsActive(true);

        trainerRepository.save(trainer1);
    }

    @Test
    void findByUsername() {
        assertEquals(trainer1, trainerRepository.findByUsername("George.Lee").get());
    }

    @Test
    void findByUsernameAndPassword() {
        assertEquals(trainer1, trainerRepository.findByUsernameAndPassword("George.Lee", "123").get());
    }

    @Test
    void findIdsOfUnassignedTrainersByTraineeUsername() {
        assertEquals(1, trainerRepository.findIdsOfUnassignedTrainersByTraineeUsername("John.Doe").size());
    }
}
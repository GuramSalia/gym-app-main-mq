package com.epam.gym_app_main_mq.repository;

import com.epam.gym_app_main_mq.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TraineeRepositoryTest {
    @Autowired
    private TraineeRepository traineeRepository;

    private Trainee trainee1;
    private Trainee trainee2;

    @BeforeEach
    void setUp() {
        trainee1 = new Trainee();
        trainee1.setUserId(100);
        trainee1.setFirstName("George");
        trainee1.setLastName("Lee");
        trainee1.setUsername("George.Lee");
        trainee1.setPassword("123");
        trainee1.setIsActive(true);
        trainee1.setAddress("address 1");

        trainee2 = new Trainee();
        trainee2.setUserId(200);
        trainee2.setFirstName("Sven");
        trainee2.setLastName("Wang");
        trainee2.setUsername("Sven.Wang");
        trainee2.setPassword("123");
        trainee2.setIsActive(true);
        trainee2.setAddress("address 2");

        traineeRepository.save(trainee1);
    }

    @Test
    void findByUsername() {
        assertEquals(trainee1, traineeRepository.findByUsername("George.Lee").get());
    }

    @Test
    void findByUsernameAndPassword() {
        assertEquals(trainee1, traineeRepository.findByUsernameAndPassword("George.Lee", "123").get());
    }

    @Test
    void findByUsernameWithQuery() {
        assertEquals(trainee1, traineeRepository.findByUsernameWithQuery("George.Lee").get());
    }
}
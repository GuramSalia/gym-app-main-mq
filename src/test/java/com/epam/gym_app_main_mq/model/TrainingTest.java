package com.epam.gym_app_main_mq.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TrainingTest {

    private Training training1;
    private Training training2;
    private Training training3;
    private Training training4;

    @BeforeEach
    void setUp() {
        training1 = new Training();
        training2 = new Training();
        training3 = new Training();
        training4 = new Training();
    }

    @Test
    void testSettersGetId() {
        training1.setTrainingId(1);
        training2.setTrainingId(2);
        training3.setTrainingId(3);
        training4.setTrainingId(4);

        assertAll(
                () -> assertEquals(1, training1.getTrainingId()),
                () -> assertEquals(2, training2.getTrainingId()),
                () -> assertEquals(3, training3.getTrainingId()),
                () -> assertEquals(4, training4.getTrainingId())
                 );
    }

    @Test
    void testSettersGetTraineeId() {

        Trainee trainee1 = new Trainee();
        trainee1.setUserId(1);
        Trainee trainee2 = new Trainee();
        trainee2.setUserId(2);
        Trainee trainee3 = new Trainee();
        trainee3.setUserId(3);
        Trainee trainee4 = new Trainee();
        trainee4.setUserId(4);

        training1.setTrainee(trainee1);
        training2.setTrainee(trainee2);
        training3.setTrainee(trainee3);
        training4.setTrainee(trainee4);

        assertAll(
                () -> assertEquals(1, training1.getTrainee().getUserId()),
                () -> assertEquals(2, training2.getTrainee().getUserId()),
                () -> assertEquals(3, training3.getTrainee().getUserId()),
                () -> assertEquals(4, training4.getTrainee().getUserId())
                 );
    }

    @Test
    void testSettersGetTrainerId() {

        Trainer trainer1 = new Trainer();
        trainer1.setUserId(1);
        Trainer trainer2 = new Trainer();
        trainer2.setUserId(2);
        Trainer trainer3 = new Trainer();
        trainer3.setUserId(3);
        Trainer trainer4 = new Trainer();
        trainer4.setUserId(4);

        training1.setTrainer(trainer1);
        training2.setTrainer(trainer2);
        training3.setTrainer(trainer3);
        training4.setTrainer(trainer4);

        assertAll(
                () -> assertEquals(1, training1.getTrainer().getUserId()),
                () -> assertEquals(2, training2.getTrainer().getUserId()),
                () -> assertEquals(3, training3.getTrainer().getUserId()),
                () -> assertEquals(4, training4.getTrainer().getUserId())
                 );
    }

    @Test
    void testSettersGetName() {
        training1.setTrainingName("Training 1");
        training2.setTrainingName("Training 2");
        training3.setTrainingName("Training 3");
        training4.setTrainingName("Training 4");
        assertAll(
                () -> assertEquals("Training 1", training1.getTrainingName()),
                () -> assertEquals("Training 2", training2.getTrainingName()),
                () -> assertEquals("Training 3", training3.getTrainingName()),
                () -> assertEquals("Training 4", training4.getTrainingName())
                 );
    }

    @Test
    void testSettersGetType() {

        TrainingType YOGA = new TrainingType();
        YOGA.setTrainingType(TrainingType.TrainingTypeEnum.YOGA);
        TrainingType PERSONAL = new TrainingType();
        PERSONAL.setTrainingType(TrainingType.TrainingTypeEnum.PERSONAL);
        TrainingType CARDIO = new TrainingType();
        CARDIO.setTrainingType(TrainingType.TrainingTypeEnum.CARDIO);
        TrainingType GROUP = new TrainingType();
        GROUP.setTrainingType(TrainingType.TrainingTypeEnum.GROUP);



        training1.setTrainingType(YOGA);
        training2.setTrainingType(PERSONAL);
        training3.setTrainingType(GROUP);
        training4.setTrainingType(CARDIO);
        assertAll(
                () -> assertEquals(YOGA, training1.getTrainingType()),
                () -> assertEquals(PERSONAL, training2.getTrainingType()),
                () -> assertEquals(GROUP, training3.getTrainingType()),
                () -> assertEquals(CARDIO, training4.getTrainingType())
                 );
    }


    @Test
    void testSettersGetDurationInMinutes() {
        training1.setTrainingDurationInMinutes(10);
        training2.setTrainingDurationInMinutes(20);
        training3.setTrainingDurationInMinutes(30);
        training4.setTrainingDurationInMinutes(40);
        assertAll(
                () -> assertEquals(10, training1.getTrainingDurationInMinutes()),
                () -> assertEquals(20, training2.getTrainingDurationInMinutes()),
                () -> assertEquals(30, training3.getTrainingDurationInMinutes()),
                () -> assertEquals(40, training4.getTrainingDurationInMinutes())
                 );
    }
}
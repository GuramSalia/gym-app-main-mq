package com.epam.gym_app_main_mq.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

    private Trainer trainer1;
    private Trainer trainer2;
    private Trainer trainer3;
    private Trainer trainer4;

    @BeforeEach
    void setUp() {
        trainer1 = new Trainer();
        trainer2 = new Trainer();
        trainer3 = new Trainer();
        trainer4 = new Trainer();
    }

    @Nested
    @DisplayName("when testing getters and setters")
    class TestSettersAndGetters {
        @Test
        void testSettersFirstName() {
            trainer1.setFirstName("John");
            trainer2.setFirstName("George");
            trainer3.setFirstName("Sam");
            trainer4.setFirstName("Kate");
            assertAll(
                    () -> assertEquals("John", trainer1.getFirstName()),
                    () -> assertEquals("George", trainer2.getFirstName()),
                    () -> assertEquals("Sam", trainer3.getFirstName()),
                    () -> assertEquals("Kate", trainer4.getFirstName())
                     );
        }

        @Test
        void testSettersLastName() {
            trainer1.setLastName("Doe");
            trainer2.setLastName("Fisher");
            trainer3.setLastName("Smith");
            trainer4.setLastName("James");
            assertAll(
                    () -> assertEquals("Doe", trainer1.getLastName()),
                    () -> assertEquals("Fisher", trainer2.getLastName()),
                    () -> assertEquals("Smith", trainer3.getLastName()),
                    () -> assertEquals("James", trainer4.getLastName())
                     );
        }

        @Test
        void testSettersUsername() {
            trainer1.setUsername("John.Doe");
            trainer2.setUsername("George.Fisher");
            trainer3.setUsername("Sam.Smith");
            trainer4.setUsername("Kate.James");
            assertAll(
                    () -> assertEquals("John.Doe", trainer1.getUsername()),
                    () -> assertEquals("George.Fisher", trainer2.getUsername()),
                    () -> assertEquals("Sam.Smith", trainer3.getUsername()),
                    () -> assertEquals("Kate.James", trainer4.getUsername())
                     );
        }

        @Test
        void testSettersPassword() {
            trainer1.setPassword("1234567890");
            trainer2.setPassword("abcdefghij");
            trainer3.setPassword("klmnopqrst");
            trainer4.setPassword("ababababab");
            assertAll(
                    () -> assertEquals("1234567890", trainer1.getPassword()),
                    () -> assertEquals("abcdefghij", trainer2.getPassword()),
                    () -> assertEquals("klmnopqrst", trainer3.getPassword()),
                    () -> assertEquals("ababababab", trainer4.getPassword())
                     );
        }

        @Test
        void testSettersActive() {
            trainer1.setIsActive(true);
            trainer2.setIsActive(false);
            trainer3.setIsActive(true);
            trainer4.setIsActive(false);
            assertAll(
                    () -> assertTrue(trainer1.getIsActive()),
                    () -> assertFalse(trainer2.getIsActive()),
                    () -> assertTrue(trainer3.getIsActive()),
                    () -> assertFalse(trainer4.getIsActive())
                     );
        }

        @Test
        void testSettersId() {
            trainer1.setUserId(1);
            trainer2.setUserId(2);
            trainer3.setUserId(3);
            trainer4.setUserId(4);

            assertAll(
                    () -> assertEquals(1, trainer1.getUserId()),
                    () -> assertEquals(2, trainer2.getUserId()),
                    () -> assertEquals(3, trainer3.getUserId()),
                    () -> assertEquals(4, trainer4.getUserId())
                     );
        }

        @Test
        void testSettersSpecialization() {

            TrainingType YOGA = new TrainingType();
            YOGA.setTrainingType(TrainingType.TrainingTypeEnum.YOGA);
            TrainingType PERSONAL = new TrainingType();
            PERSONAL.setTrainingType(TrainingType.TrainingTypeEnum.PERSONAL);
            TrainingType CARDIO = new TrainingType();
            CARDIO.setTrainingType(TrainingType.TrainingTypeEnum.CARDIO);
            TrainingType STRENGTH = new TrainingType();
            STRENGTH.setTrainingType(TrainingType.TrainingTypeEnum.STRENGTH);

            trainer1.setSpecialization(YOGA);
            trainer2.setSpecialization(PERSONAL);
            trainer3.setSpecialization(CARDIO);
            trainer4.setSpecialization(STRENGTH);
            assertAll(
                    () -> assertEquals(YOGA, trainer1.getSpecialization()),
                    () -> assertEquals(PERSONAL, trainer2.getSpecialization()),
                    () -> assertEquals(CARDIO, trainer3.getSpecialization()),
                    () -> assertEquals(STRENGTH, trainer4.getSpecialization())
                     );
        }
    }
}
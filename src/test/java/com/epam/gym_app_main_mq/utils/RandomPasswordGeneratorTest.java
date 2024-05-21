package com.epam.gym_app_main_mq.utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Testing RandomPasswordGenerator static method generateRandomPassword()")
class RandomPasswordGeneratorTest {
    String password1;
    String password2;
    String password3;
    String password4;

    @DisplayName("When checking that created password is a String")
    @Test
    void generateRandomPasswordString() {

        try {
            password1 = RandomPasswordGenerator.generateRandomPassword();
            password2 = RandomPasswordGenerator.generateRandomPassword();
            password3 = RandomPasswordGenerator.generateRandomPassword();
            password4 = RandomPasswordGenerator.generateRandomPassword();
        } catch (Exception e) {
            fail("Should generate String without throwing an error: " + e.getMessage());
        }
    }

    @DisplayName("When checking that password is 10 long")
    @Test
    void generateRandomPasswordLength() {
        password1 = RandomPasswordGenerator.generateRandomPassword();
        password2 = RandomPasswordGenerator.generateRandomPassword();
        password3 = RandomPasswordGenerator.generateRandomPassword();
        password4 = RandomPasswordGenerator.generateRandomPassword();

        assertAll(
                () -> assertEquals(10, password1.length()),
                () -> assertEquals(10, password2.length()),
                () -> assertEquals(10, password3.length()),
                () -> assertEquals(10, password4.length())
                 );
    }

    @DisplayName("When checking that passwords are randomly generated")
    @Test
    void generateRandomPasswords() {
        password1 = RandomPasswordGenerator.generateRandomPassword();
        password2 = RandomPasswordGenerator.generateRandomPassword();
        password3 = RandomPasswordGenerator.generateRandomPassword();
        password4 = RandomPasswordGenerator.generateRandomPassword();

        assertAll(
                () -> assertNotEquals(password1, password2, () -> "each password should be unique, not equal"),
                () -> assertNotEquals(password1, password3, () -> "each password should be unique, not equal"),
                () -> assertNotEquals(password1, password4, () -> "each password should be unique, not equal"),
                () -> assertNotEquals(password2, password3, () -> "each password should be unique, not equal"),
                () -> assertNotEquals(password2, password4, () -> "each password should be unique, not equal"),
                () -> assertNotEquals(password3, password4, () -> "each password should be unique, not equal")
                 );
    }
}
package com.epam.gym_app_main_mq.exception;

public class UserNotDeletedException extends RuntimeException{
    public UserNotDeletedException(String message) {
        super(message);
    }
}

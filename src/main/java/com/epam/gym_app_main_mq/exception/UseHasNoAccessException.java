package com.epam.gym_app_main_mq.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UseHasNoAccessException extends RuntimeException {
    public UseHasNoAccessException(String message) {
        super(message);
    }
}

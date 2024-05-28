package com.epam.gym_app_main_mq.exception.dlqs;

import org.springframework.jms.JmsException;

public class dlqTriggeringException extends JmsException {
    public dlqTriggeringException(String msg) {
        super(msg);
    }

    public dlqTriggeringException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public dlqTriggeringException(Throwable cause) {
        super(cause);
    }
}

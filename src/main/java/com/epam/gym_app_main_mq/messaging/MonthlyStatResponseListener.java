package com.epam.gym_app_main_mq.messaging;

import com.epam.gym_app_main_mq.exception.dlqs.dlqTriggeringException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MonthlyStatResponseListener {

    private final ObjectMapper objectMapper;

    @Autowired
    public MonthlyStatResponseListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "${spring.jms.monthlyStatResponse}")
    public void receiveMonthlyStat(
            String jsonMessage,
            @Header("gym_app_correlation_id") String correlationId
    ) {

        try {
            Map<String, Integer> message = objectMapper.readValue(
                    jsonMessage,
                    new TypeReference<Map<String, Integer>>() {});
            log.info("\n\n MAIN-APP -> MONTHLY STAT Listener -> Received message: {}, with correlationId: {}\n\n",
                     message, correlationId);
        } catch (Exception e) {
            String errorMessage = String.format(
                    "MAIN-APP -> MONTHLY STAT Listener -> Error processing message: %s",
                    e.getMessage());
            log.error(errorMessage);
            throw new dlqTriggeringException(errorMessage);
        }
    }
}

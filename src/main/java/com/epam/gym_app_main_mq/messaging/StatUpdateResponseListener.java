package com.epam.gym_app_main_mq.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StatUpdateResponseListener {

    private final ObjectMapper objectMapper;

    @Autowired
    public StatUpdateResponseListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @JmsListener(destination = "${spring.jms.statUpdateResponse}")
    public void receiveFullStat(
            String jsonMessage,
            @Header("gym_app_correlation_id") String correlationId
    ) {
        try {
            Map<String, Integer> message = objectMapper.readValue(
                    jsonMessage,
                    new TypeReference<Map<String, Integer>>() {});
            log.info("\n\n MAIN-APP -> STAT UPDATE Listener -> Received message: {}, with correlationId: {}\n\n",
                     message, correlationId);
        } catch (Exception e) {
            log.error("MAIN-APP -> MONTHLY STAT UPDATE Listener -> Error processing message: {}", e.getMessage());
        }
    }

}

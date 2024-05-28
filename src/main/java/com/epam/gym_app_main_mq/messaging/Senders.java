package com.epam.gym_app_main_mq.messaging;


import com.epam.gym_app_main_mq.api.stat.FullStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.MonthlyStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.UpdateStatRequestInMainApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class Senders {
    private final JmsTemplate jmsTemplate;

    @Value("${spring.jms.requestStatUpdate}")
    private String requestStatUpdateQueue;
    @Value("${spring.jms.requestMonthlyStat}")
    private String requestMonthlyStatQueue;
    @Value("${spring.jms.requestFullStat}")
    private String requestFullStatQueue;

    @Autowired
    public Senders(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void requestStatUpdate(UpdateStatRequestInMainApp updateStatRequestInMainApp, String correlationId) {
        Map<String, String> requestMap = updateStatRequestInMainApp.toMap();
        String jsonRequest = convertMapToJson(requestMap);
        jmsTemplate.convertAndSend(requestStatUpdateQueue, jsonRequest, message -> {
            message.setStringProperty("gym_app_correlation_id", correlationId);
            return message;
        });
    }

    public void requestMonthlyStat(MonthlyStatRequestInMainApp monthlyStatRequestInMainApp, String correlationId) {
        Map<String, String> requestMap = monthlyStatRequestInMainApp.toMap();
        String jsonRequest = convertMapToJson(requestMap);
        jmsTemplate.convertAndSend(requestMonthlyStatQueue, jsonRequest, message -> {
            message.setStringProperty("gym_app_correlation_id", correlationId);
            return message;
        });
    }

    public void requestFullStat(FullStatRequestInMainApp fullStatRequestInMainApp, String correlationId) {
        Map<String, String> requestMap = fullStatRequestInMainApp.toMap();
        String jsonRequest = convertMapToJson(requestMap);
        jmsTemplate.convertAndSend(requestFullStatQueue, jsonRequest, message -> {
            message.setStringProperty("gym_app_correlation_id", correlationId);
            return message;
        });
    }

    private String convertMapToJson(Map<String, String> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("Error converting map to JSON: {}", e.getMessage());
            return "{}";
        }
    }
}

package com.epam.gym_app_main_mq.messaging;

import com.epam.gym_app_main_mq.api.stat.FullStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.MonthlyStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.UpdateStatRequestInMainApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

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
        jmsTemplate.convertAndSend(requestStatUpdateQueue, requestMap, message -> {
            message.setStringProperty("gym_app_correlation_id", correlationId);
            return message;
        });
    }

    public void requestMonthlyStat(MonthlyStatRequestInMainApp monthlyStatRequestInMainApp, String correlationId) {
        Map<String, String> requestMap = monthlyStatRequestInMainApp.toMap();
        jmsTemplate.convertAndSend(requestMonthlyStatQueue, requestMap, message -> {
            message.setStringProperty("gym_app_correlation_id", correlationId);
            return message;
        });
    }

    public void requestFullStat(FullStatRequestInMainApp fullStatRequestInMainApp, String correlationId) {
        Map<String, String> requestMap = fullStatRequestInMainApp.toMap();
        jmsTemplate.convertAndSend(requestFullStatQueue, requestMap, message -> {
            message.setStringProperty("gym_app_correlation_id", correlationId);
            return message;
        });
    }
}

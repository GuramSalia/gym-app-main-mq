package com.epam.gym_app_main_mq.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MonthlyStatResponseListener {
    @JmsListener(destination = "${spring.jms.monthlyStatResponse}")
    public void receiveMonthlyStat(
            Map<String, Integer> message,
            @Header("gym_app_correlation_id") String correlationId
    ) {
        log.info("\n\n MAIN-APP -> MONTHLY STAT Listener -> Received message: {}, with correlationId: {}\n\n",
                 message,
                 correlationId);
    }
}

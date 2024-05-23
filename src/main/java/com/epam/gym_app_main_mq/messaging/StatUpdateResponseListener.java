package com.epam.gym_app_main_mq.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class StatUpdateResponseListener {
    @JmsListener(destination = "${spring.jms.statUpdateResponse}")
    public void receiveFullStat(
            Map<String, Integer> message,
            @Header("gym_app_correlation_id") String correlationId
    ) {
        // implement, change parameter !
        log.info("\n\n MAIN-APP -> STAT UPDATE Listener -> Received message: {}, with correlationId: {}\n\n",
                 message,
                 correlationId);
    }
}

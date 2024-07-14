package com.epam.gym_app_main_mq.utils;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Entity(name = "TEST_HELPER")
@AllArgsConstructor
@NoArgsConstructor
public class TestCorrelationIdHolder {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HELPER_ID")
    private Integer id;
    @Column(name = "UPDATE_STAT")
    private String statUpdateCorrelationId = "statUpdateCorrelationId";
    @Column(name = "FULL_STAT")
    private String fullStatCorrelationId = "fullStatCorrelationId";
    @Column(name = "MONTHLY_STAT")
    private String monthlyStatCorrelationId = "monthlyStatCorrelationId";

    public String getStatUpdateCorrelationId() {
        log.info("getStatUpdateCorrelationId: {}", statUpdateCorrelationId);
        return statUpdateCorrelationId;
    }

    public void setStatUpdateCorrelationId(String statUpdateCorrelationId) {
        log.info("setStatUpdateCorrelationId: {}", statUpdateCorrelationId);
        this.statUpdateCorrelationId = statUpdateCorrelationId;
    }

    public String getFullStatCorrelationId() {
        log.info("getFullStatCorrelationId: {}", fullStatCorrelationId);
        return fullStatCorrelationId;
    }

    public void setFullStatCorrelationId(String fullStatCorrelationId) {
        log.info("setFullStatCorrelationId: {}", fullStatCorrelationId);
        this.fullStatCorrelationId = fullStatCorrelationId;
    }

    public String getMonthlyStatCorrelationId() {
        log.info("getMonthlyStatCorrelationId: {}", monthlyStatCorrelationId);
        return monthlyStatCorrelationId;
    }

    public void setMonthlyStatCorrelationId(String monthlyStatCorrelationId) {
        log.info("setMonthlyStatCorrelationId: {}", monthlyStatCorrelationId);
        this.monthlyStatCorrelationId = monthlyStatCorrelationId;
    }
}

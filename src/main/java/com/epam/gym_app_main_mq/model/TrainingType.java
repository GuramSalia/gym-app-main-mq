package com.epam.gym_app_main_mq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "TRAINING_TYPES")
@Setter
@Getter
public class TrainingType {

    public enum TrainingTypeEnum {
        CARDIO, STRENGTH, HIIT, YOGA, PILATES, GROUP, PERSONAL,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAINING_TYPE_ID")
    @JsonProperty(value = "id")
    private int trainingTypeId;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "TRAINING_TYPE_NAME")
    @JsonProperty(value = "training_type")
    private TrainingTypeEnum trainingType;

    public TrainingType() {}

    public TrainingType(String trainingType) {
        this.trainingType = TrainingTypeEnum.valueOf(trainingType.toUpperCase());
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + trainingTypeId +
                ", training_type=" + trainingType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingType that = (TrainingType) o;
        return trainingType == that.trainingType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingType);
    }
}

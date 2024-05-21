package com.epam.gym_app_main_mq.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.time.LocalDate.now;

@Entity
@Table(name = "TRAININGS")
@Setter
@Getter
@Slf4j
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAINING_ID")
    private Integer trainingId;

    @ManyToOne
    @JoinColumn(name = "TRAINEE_ID")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "TRAINER_ID")
    private Trainer trainer;

    @Column(name = "TRAINING_NAME")
    private String trainingName;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "TRAINING_TYPE_ID", referencedColumnName = "TRAINING_TYPE_ID", nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private TrainingType trainingType;

    @Column(name = "TRAINING_DATE")
    private Date trainingDate;

    @Column(name = "TRAINING_DURATION")
    private int trainingDurationInMinutes;

    public void setTrainingDateEasy(Integer year, Integer month, Integer day) {
        if (year > now().getYear()) {
            log.info("birth year cannot be in the future");
            return;
        }

        if (month > 11 || month < 0) {
            log.info("birth month cannot be in the future");
            return;
        }

        if (day > 31 || day < 0) {
            log.info("birth day cannot be in the future");
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        setTrainingDate(cal.getTime());
    }

    @Override
    public String toString() {return "Training{" + "trainingId=" + trainingId + ", trainingName='" + trainingName + '\'' + '}';}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return getTrainingId() == training.getTrainingId() && Objects.equals(getTrainingName(),
                                                                             training.getTrainingName());
    }

    @Override
    public int hashCode() {return Objects.hash(getTrainingId(), getTrainingName());}
}

package com.epam.gym_app_main_mq.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TRAINERS")
@PrimaryKeyJoinColumn(name = "USER_ID")
@Getter
@Setter
@Slf4j
public class Trainer extends User {

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = true)
    @Column(name = "USER_ID")
    private Integer userId;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "TRAINING_TYPE_ID", referencedColumnName = "TRAINING_TYPE_ID", nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees = new HashSet<>();

    @Override
    public String toString() {
        return "Trainer{" + "trainerId=" + userId + " " + getFirstName() + " " + getLastName() + " username: " + getUsername() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return userId == trainer.getUserId();
    }

    @Override
    public int hashCode() {return Objects.hash(getUserId());}

}

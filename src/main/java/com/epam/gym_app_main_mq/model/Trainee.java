package com.epam.gym_app_main_mq.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static java.time.LocalDate.now;

@Entity
@Table(name = "TRAINEES")
@PrimaryKeyJoinColumn(name = "USER_ID")
@Getter
@Setter
@Slf4j
public class Trainee extends User {

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = true)
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "DATE_OF_BIRTH")
    private Date dob;

    @Column(name = "ADDRESS")
    private String address;

    @ManyToMany
    @JoinTable(name = "TRAINEES_TRAINERS", joinColumns = {@JoinColumn(name = "TRAINEE_ID")}, inverseJoinColumns =
            {@JoinColumn(name = "TRAINER_ID")})
    Set<Trainer> trainers = new HashSet<>();

    public void setDobEasy(Integer year, Integer month, Integer day) {
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
        setDob(cal.getTime());
    }

    @Override
    public String toString() {return "Trainee{" + "traineeId=" + userId + " " + getFirstName() + " " + getLastName() + " username: " + getUsername() + '}';}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return userId == trainee.getUserId();
    }

    @Override
    public int hashCode() {return Objects.hash(getUserId(), getDob(), getAddress());}

}

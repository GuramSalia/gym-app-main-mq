package com.epam.gym_app_main_mq.repository;

import com.epam.gym_app_main_mq.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUsername(String username);

    Optional<Trainer> findByUsernameAndPassword(String username, String password);


    @Query(value = "SELECT USER_ID FROM TRAINERS WHERE USER_ID NOT IN (SELECT gu.USER_ID FROM TRAINEES_TRAINERS tt " +
            "JOIN GYM_USERS gu ON tt.TRAINER_ID = gu.USER_ID WHERE tt.TRAINEE_ID = (SELECT tr.USER_ID FROM TRAINEES " +
            "tr JOIN GYM_USERS u ON tr.USER_ID = u.USER_ID WHERE u.USERNAME = ?1))", nativeQuery = true)
    List<Integer> findIdsOfUnassignedTrainersByTraineeUsername(@Param("username") String traineeUsername);

}

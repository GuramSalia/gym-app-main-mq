package com.epam.gym_app_main_mq.repository;

import com.epam.gym_app_main_mq.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface  TrainingRepository extends JpaRepository<Training, Integer> {

    @Query(value = "SELECT TRAINING_ID FROM TRAININGS t WHERE t.TRAINEE_ID = (SELECT tr.USER_ID FROM TRAINEES tr JOIN" +
            " GYM_USERS u ON tr.USER_ID = u.USER_ID WHERE u.USERNAME = ?1) AND ((?2 IS NULL OR t.TRAINING_DATE >= ?2)" +
            " AND (?3 IS NULL OR t.TRAINING_DATE <= ?3)) AND (?4 IS NULL OR t.TRAINER_ID = (SELECT gu.USER_ID FROM " +
            "GYM_USERS gu WHERE gu.USERNAME = ?4)) AND (?5 IS NULL OR t.TRAINING_TYPE_ID = (SELECT tt" +
            ".TRAINING_TYPE_ID FROM TRAINING_TYPES tt WHERE tt.TRAINING_TYPE_NAME = ?5))", nativeQuery = true)
    List<Integer> findIdsByTraineeAndTrainerAndType(
            @Param("traineeUsername") String traineeUsername,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("trainerUsername") String trainerUsername,
            @Param("trainingTypeName") String trainingTypeName
    );

    @Query(value = "SELECT TRAINING_ID FROM TRAININGS t WHERE t.TRAINER_ID = (SELECT tr.USER_ID FROM TRAINERS tr JOIN" +
            " GYM_USERS u ON tr.USER_ID = u.USER_ID WHERE u.USERNAME = ?1) AND (" +
            "(?2 IS NULL OR t.TRAINING_DATE >= ?2) AND (?3 IS NULL OR t.TRAINING_DATE <= ?3))" +
            " AND (?4 IS NULL OR t.TRAINEE_ID = (SELECT gu.USER_ID FROM GYM_USERS gu WHERE gu.USERNAME = ?4))",
            nativeQuery = true)
    List<Integer> findIdsByTrainerAndTrainerAndType(
            @Param("trainerUsername") String trainerUsername,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("traineeUsername") String traineeUsername
    );
}


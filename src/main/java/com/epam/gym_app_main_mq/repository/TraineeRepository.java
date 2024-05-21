package com.epam.gym_app_main_mq.repository;

import com.epam.gym_app_main_mq.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    Optional<Trainee> findByUsername(String username);

    Optional<Trainee> findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT * FROM TRAINEES tr JOIN GYM_USERS u ON tr.USER_ID = u.USER_ID WHERE u.USERNAME = ?1",
            nativeQuery = true)
    Optional<Trainee> findByUsernameWithQuery(String username);
}

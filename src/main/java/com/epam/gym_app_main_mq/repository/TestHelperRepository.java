package com.epam.gym_app_main_mq.repository;

import com.epam.gym_app_main_mq.utils.TestCorrelationIdHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestHelperRepository extends JpaRepository<TestCorrelationIdHolder, Integer> {
}
